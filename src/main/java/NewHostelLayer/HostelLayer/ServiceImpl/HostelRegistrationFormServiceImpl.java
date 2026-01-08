package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.*;
import NewHostelLayer.HostelLayer.Entity.HostelInquiry;
import NewHostelLayer.HostelLayer.Entity.HostelRegistrationForm;
import NewHostelLayer.HostelLayer.Entity.HostelDocuments;
import NewHostelLayer.HostelLayer.Entity.HostelGuardianMedical;
import NewHostelLayer.HostelLayer.Mapper.HostelRegistrationMapper;
import NewHostelLayer.HostelLayer.Pagination.HostelRegistrationSpecification;
import NewHostelLayer.HostelLayer.Repository.HostelRegistrationFormRepository;
import NewHostelLayer.HostelLayer.Repository.HostelDocumentsRepository;
import NewHostelLayer.HostelLayer.Repository.HostelGuardianMedicalRepository;
import NewHostelLayer.HostelLayer.Repository.InquiryRepository;
import NewHostelLayer.HostelLayer.Service.HostelRegistrationFormService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class HostelRegistrationFormServiceImpl implements HostelRegistrationFormService
{
    @Autowired
    StaffService staffService;

    @Autowired
    HostelRegistrationFormRepository registrationRepository;

    @Autowired
    HostelDocumentsRepository documentsRepository;

    @Autowired
    InquiryRepository inquiryRepository;

    @Autowired
    HostelGuardianMedicalRepository guardianMedicalRepository;

    @Autowired
    S3Service s3Service;

    @Autowired
    HostelRegistrationMapper mapper;

    @Override
    public HostelRegistrationRequestDTO saveOrUpdateRegistration(String role, String email, @Nullable Long inquiryId,
            HostelRegistrationRequestDTO data, MultipartFile personalPhoto,
            MultipartFile addressProofFile, MultipartFile educationProofFile,
            MultipartFile signature, MultipartFile parentSignature)
    {

        if (!staffService.hasPermission(role, email, "Post")) {
            throw new RuntimeException("No permission");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);
        HostelRegistrationForm entity;

        if (data.getId() == null) {

            entity = HostelRegistrationMapper.toEntity(data);
            entity.setRole(role);
            entity.setCreatedByEmail(email);
            entity.setBranchCode(branchCode);

            if(inquiryId != null)
            {
                entity.setInquiryId(inquiryId);
                HostelInquiry inquiry = inquiryRepository.findById(inquiryId)
                        .orElseThrow(()-> new RuntimeException("Inquiry Not Found"));
                inquiry.setRegister(true);

            }
        }
        else {

            entity = registrationRepository.findById(data.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Hostel form not found"));

            mergeRegistration(entity, data);
        }

        handleDocuments(entity, personalPhoto, addressProofFile, educationProofFile,
                signature, parentSignature, branchCode);

        HostelRegistrationForm registrationForm = registrationRepository.save(entity);

        return HostelRegistrationMapper.toDTO(registrationForm);
    }


    @Override
    public HostelRegistrationRequestDTO getRegistrationById(Long id, String role, String email)
    {
        if(!staffService.hasPermission(role,email,"Get"))
        {
            throw new RuntimeException("You don't have Permission to get Data");
        }

        HostelRegistrationForm form = registrationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Id not Found"));

        return HostelRegistrationMapper.toDTO(form);

    }


    @Override
    public HostelRegistrationPageResponse getAllRegistrationsWithSummary(String role, String email, int page, int size,
            String dateRange, LocalDate customStartDate, LocalDate customEndDate,
            HostelRegistrationFilterRequest filter)
    {
        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException("You don't have Permission");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        LocalDate startDate = null;
        LocalDate endDate = LocalDate.now();

        if (dateRange != null) {
            switch (dateRange.toUpperCase()) {
                case "TODAY" -> startDate = LocalDate.now();
                case "7DAYS" -> startDate = LocalDate.now().minusDays(7);
                case "30DAYS" -> startDate = LocalDate.now().minusDays(30);
                case "365DAYS" -> startDate = LocalDate.now().minusDays(365);
                case "CUSTOM" -> {
                    startDate = customStartDate;
                    endDate = customEndDate;
                }
            }
        }

        Specification<HostelRegistrationForm> specification =
                HostelRegistrationSpecification.filterBy(branchCode,
                        filter, startDate, endDate);
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("admissionDate").descending()
        );

        Page<HostelRegistrationForm> pagedResult =
                registrationRepository.findAll(specification, pageable);

        Page<HostelRegistrationResponseDTO> dtoPage =
                pagedResult.map(mapper::mapToDTO);

        List<HostelRegistrationForm> fullResult =
                registrationRepository.findAll(specification);

        HostelAmountSummaryDTO summary =
                calculateSummary(fullResult);

        return new HostelRegistrationPageResponse(dtoPage, summary);
    }

    private HostelAmountSummaryDTO calculateSummary(
            List<HostelRegistrationForm> records
    ) {

        double totalAmount = 0;
        double deposit = 0;
        double pending = 0;
        double cancel = 0;
        double refund = 0;

        for (HostelRegistrationForm r : records) {

            totalAmount += safe(r.getTotalFees());
            deposit += safe(r.getDepositCollected());

            switch (r.getStatus()) {
                case "PENDING" -> pending += safe(r.getRemainingFees());
                case "CANCEL" -> cancel += safe(r.getTotalFees());
                case "REFUND" -> refund += safe(r.getDepositCollected());
            }
        }

        return new HostelAmountSummaryDTO(
                totalAmount,
                deposit,
                pending,
                cancel,
                refund
        );
    }

    private double safe(Double val) {
        return val == null ? 0.0 : val;
    }


    private void mergeRegistration(HostelRegistrationForm existing, HostelRegistrationRequestDTO request) {

        if (request.getFullName() != null)
            existing.setFullName(request.getFullName());

        if (request.getDateOfBirth() != null)
            existing.setDateOfBirth(request.getDateOfBirth());

        if (request.getGender() != null)
            existing.setGender(request.getGender());

        if (request.getEmail() != null)
            existing.setEmail(request.getEmail());

        if (request.getMaritalStatus() != null)
            existing.setMaritalStatus(request.getMaritalStatus());

        if (request.getBloodGroup() != null)
            existing.setBloodGroup(request.getBloodGroup());

        if (request.getReligion() != null)
            existing.setReligion(request.getReligion());

        // -------- ACADEMIC / JOB --------
        if (request.getYearOfAdmission() != null)
            existing.setYearOfAdmission(request.getYearOfAdmission());

        if (request.getCurrentInstitution() != null)
            existing.setCurrentInstitution(request.getCurrentInstitution());

        if (request.getCourseEnrolled() != null)
            existing.setCourseEnrolled(request.getCourseEnrolled());

        if (request.getCurrentEmployer() != null)
            existing.setCurrentEmployer(request.getCurrentEmployer());

        if (request.getDesignation() != null)
            existing.setDesignation(request.getDesignation());

        if (request.getYearOfJoining() != null)
            existing.setYearOfJoining(request.getYearOfJoining());

        if (request.getParentsName() != null)
            existing.setParentsName(request.getParentsName());

        if (request.getParentsMobNo() != null)
            existing.setParentsMobNo(request.getParentsMobNo());

        if (request.getPermanentAddress() != null)
            existing.setPermanentAddress(request.getPermanentAddress());

        if (request.getCity() != null)
            existing.setCity(request.getCity());

        if (request.getState() != null)
            existing.setState(request.getState());

        if (request.getPincode() != null)
            existing.setPincode(request.getPincode());

        if (request.getStayFrom() != null)
            existing.setStayFrom(request.getStayFrom());

        if (request.getStayTo() != null)
            existing.setStayTo(request.getStayTo());

        if (request.getFloor() != null)
            existing.setFloor(request.getFloor());

        if (request.getRoomNumber() != null)
            existing.setRoomNumber(request.getRoomNumber());

        if (request.getBedType() != null)
            existing.setBedType(request.getBedType());

        if (request.getBedNumber() != null)
            existing.setBedNumber(request.getBedNumber());

        if (request.getRoomType() != null)
            existing.setRoomType(request.getRoomType());

        if (request.getDepositCollected() != null)
            existing.setDepositCollected(request.getDepositCollected());

        if (request.getTotalFees() != null)
            existing.setTotalFees(request.getTotalFees());

        if (request.getRemainingFees() != null)
            existing.setRemainingFees(request.getRemainingFees());

        if (request.getPaymentMode() != null)
            existing.setPaymentMode(request.getPaymentMode());

        if (request.getTransactionNumber() != null)
            existing.setTransactionNumber(request.getTransactionNumber());

        if (request.getRemark() != null)
            existing.setRemark(request.getRemark());

        if (request.getStatus() != null)
            existing.setStatus(request.getStatus());

        if(request.getAddressProofType() != null)
            existing.setAddressProofType(request.getAddressProofType());

        if(request.getEducationProofType() != null)
            existing.setEducationProofType(request.getEducationProofType());

        if (request.getAdmissionDate() != null)
            existing.setAdmissionDate(request.getAdmissionDate());

        if (request.getGuardianMedical() != null) {

            HostelGuardianMedical existingGM = existing.getGuardianMedical();

            if (existingGM == null) {
                request.getGuardianMedical().setRegistration(existing);
                existing.setGuardianMedical(request.getGuardianMedical());
            } else {
                mergeGuardian(existingGM, request.getGuardianMedical());
            }
        }

    }


    private void mergeGuardian(HostelGuardianMedical existing, HostelGuardianMedical request) {

        if (request.getGuardianFullName() != null)
            existing.setGuardianFullName(request.getGuardianFullName());

        if (request.getGuardianPermanentAddress() != null)
            existing.setGuardianPermanentAddress(
                    request.getGuardianPermanentAddress());

        if (request.getMobileNo() != null)
            existing.setMobileNo(request.getMobileNo());

        if (request.getMedicalCondition() != null)
            existing.setMedicalCondition(request.getMedicalCondition());

        if (request.getAllergies() != null)
            existing.setAllergies(request.getAllergies());

        if (request.getFamilyDoctorFullName() != null)
            existing.setFamilyDoctorFullName(request.getFamilyDoctorFullName());

        if (request.getDoctormobileNo() != null)
            existing.setDoctormobileNo(request.getDoctormobileNo());

        if (request.getVehicleType() != null)
            existing.setVehicleType(request.getVehicleType());

        if (request.getRegistrationNo() != null)
            existing.setRegistrationNo(request.getRegistrationNo());
    }

    private void handleDocuments(HostelRegistrationForm registration, MultipartFile personalPhoto,
            MultipartFile addressProofFile, MultipartFile educationProofFile,
            MultipartFile signature, MultipartFile parentSignature, String branchCode)
    {

        HostelDocuments documents = registration.getDocuments();

        if (documents == null) {
            documents = new HostelDocuments();
            documents.setRegistration(registration);
            registration.setDocuments(documents);
        }

        if (personalPhoto != null && !personalPhoto.isEmpty()) {
            documents.setPersonalPhoto(
                    s3Service.uploadFile(personalPhoto, branchCode)
            );
        }

        if (addressProofFile != null && !addressProofFile.isEmpty()) {
            documents.setAddressProofFile(
                    s3Service.uploadFile(addressProofFile, branchCode)
            );
        }

        if (educationProofFile != null && !educationProofFile.isEmpty()) {
            documents.setEducationProofFile(
                    s3Service.uploadFile(educationProofFile, branchCode)
            );
        }

        if (signature != null && !signature.isEmpty()) {
            documents.setSignature(
                    s3Service.uploadFile(signature, branchCode)
            );
        }

        if (parentSignature != null && !parentSignature.isEmpty()) {
            documents.setParentSignature(
                    s3Service.uploadFile(parentSignature, branchCode)
            );
        }
    }


}
