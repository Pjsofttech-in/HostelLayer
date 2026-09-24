package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.HostelAdmissionRequestDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionSummaryDTO;
import NewHostelLayer.HostelLayer.Entity.HostelAdmissionForm;
import NewHostelLayer.HostelLayer.Enum.BedType;
import NewHostelLayer.HostelLayer.Enum.DateRange;
import NewHostelLayer.HostelLayer.Enum.PaymentStatus;
import NewHostelLayer.HostelLayer.Enum.RoomType;
import NewHostelLayer.HostelLayer.Pagination.HostelAdmissionSpecification;
import NewHostelLayer.HostelLayer.Repository.HostelAdmissionFormRepository;
import NewHostelLayer.HostelLayer.Service.HostelAdmissionFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HostelAdmissionFormServiceImpl
        implements HostelAdmissionFormService {

    private final HostelAdmissionFormRepository repository;

    private final S3Service s3Service;

    private final StaffService staffService;


    @Override
    public HostelAdmissionResponseDTO createAdmissionForm(
            String role,
            String email,
            HostelAdmissionRequestDTO dto,
            MultipartFile authorizedSignature) {

        if (!staffService.hasPermission(role, email, "Post")) {
            throw new RuntimeException(
                    "You don't have permission to create Admission"
            );
        }

        String branchCode =
                staffService.fetchBranchCodeByRole(role, email);

        HostelAdmissionForm admission =
                new HostelAdmissionForm();

        mapRequestToEntity(dto, admission);

        // Set logged-in user details
        admission.setBranchCode(branchCode);
        admission.setCreatedByEmail(email);
        admission.setRole(role);


        // Upload authorized signature
        if (authorizedSignature != null &&
                !authorizedSignature.isEmpty()) {

            try {

                String signatureUrl =
                        s3Service.uploadFile(
                                authorizedSignature,
                                branchCode
                        );

                admission.setAuthorizedSignature(signatureUrl);

            } catch (Exception e) {

                throw new RuntimeException(
                        "Failed to upload authorized signature",
                        e
                );
            }
        }


        // Calculate GST and total amount
        calculateAmount(admission);


        HostelAdmissionForm savedAdmission =
                repository.save(admission);

        return mapToResponseDTO(savedAdmission);
    }


    @Override
    public List<HostelAdmissionResponseDTO> getAllAdmissions(
            String role,
            String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException(
                    "You don't have permission to view Admissions"
            );
        }

        String branchCode =
                staffService.fetchBranchCodeByRole(role, email);

        return repository.findAll()
                .stream()
                .filter(admission ->
                        branchCode.equals(admission.getBranchCode()))
                .map(this::mapToResponseDTO)
                .toList();
    }


    @Override
    public HostelAdmissionResponseDTO getAdmissionById(
            Long id,
            String role,
            String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException(
                    "You don't have permission to view Admission"
            );
        }

        String branchCode =
                staffService.fetchBranchCodeByRole(role, email);

        HostelAdmissionForm admission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admission form not found with id: " + id
                                )
                        );

        // Branch-level access check
        if (!branchCode.equals(admission.getBranchCode())) {
            throw new RuntimeException(
                    "You don't have permission to view this Admission"
            );
        }

        return mapToResponseDTO(admission);
    }


    @Override
    public HostelAdmissionSummaryDTO getAdmissionSummary(
            String role,
            String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException(
                    "You don't have permission to view Admission Summary"
            );
        }

        String branchCode =
                staffService.fetchBranchCodeByRole(role, email);

        List<HostelAdmissionForm> admissions =
                repository.findAll()
                        .stream()
                        .filter(admission ->
                                branchCode.equals(admission.getBranchCode()))
                        .toList();


        HostelAdmissionSummaryDTO summary =
                new HostelAdmissionSummaryDTO();


        // Total Admissions
        summary.setTotalAdmissions(
                (long) admissions.size()
        );


        double total = 0.0;
        double totalWithGst = 0.0;
        double depositWithGst = 0.0;
        double pending = 0.0;
        double cancel = 0.0;
        double refund = 0.0;


        for (HostelAdmissionForm admission : admissions) {

            // Total without GST
            double baseAmount = 0.0;

            if (admission.getTotalMonths() != null &&
                    admission.getMonthlyRent() != null) {

                baseAmount =
                        admission.getTotalMonths()
                                * admission.getMonthlyRent();
            }

            total += baseAmount;


            // Total with GST
            double finalAmount =
                    admission.getTotalAmount() != null
                            ? admission.getTotalAmount()
                            : 0.0;

            totalWithGst += finalAmount;


            // Deposit (+GST) - PAID
            if (admission.getPaymentStatus() ==
                    PaymentStatus.PAID) {

                depositWithGst += finalAmount;
            }


            // Pending
            if (admission.getPaymentStatus() ==
                    PaymentStatus.PENDING) {

                pending += finalAmount;
            }


            // Cancel
            if (admission.getPaymentStatus() ==
                    PaymentStatus.CANCELLED) {

                cancel += finalAmount;
            }


            // Refund
            if (admission.getPaymentStatus() ==
                    PaymentStatus.REFUNDED) {

                refund += finalAmount;
            }
        }


        summary.setTotal(total);
        summary.setTotalWithGst(totalWithGst);
        summary.setDepositWithGst(depositWithGst);
        summary.setPending(pending);
        summary.setCancel(cancel);
        summary.setRefund(refund);

        return summary;
    }


    @Override
    public HostelAdmissionResponseDTO updateAdmission(
            Long id,
            String role,
            String email,
            HostelAdmissionRequestDTO dto,
            MultipartFile authorizedSignature) {

        if (!staffService.hasPermission(role, email, "Put")) {
            throw new RuntimeException(
                    "You don't have permission to update Admission"
            );
        }

        String branchCode =
                staffService.fetchBranchCodeByRole(role, email);


        HostelAdmissionForm admission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admission form not found with id: " + id
                                )
                        );


        // Branch-level access check
        if (!branchCode.equals(admission.getBranchCode())) {
            throw new RuntimeException(
                    "You don't have permission to update this Admission"
            );
        }


        // Update admission fields
        mapRequestToEntity(dto, admission);


        // Do NOT allow update request to change branch
        admission.setBranchCode(branchCode);


        // Keep original creator information
        // unless you specifically want to change it.
        if (admission.getCreatedByEmail() == null) {
            admission.setCreatedByEmail(email);
        }

        if (admission.getRole() == null) {
            admission.setRole(role);
        }


        // Update signature only if a new file is provided
        if (authorizedSignature != null &&
                !authorizedSignature.isEmpty()) {

            try {

                // Delete old signature from S3
                if (admission.getAuthorizedSignature() != null &&
                        !admission.getAuthorizedSignature().isBlank()) {

                    s3Service.deleteFileFromUrl(
                            admission.getAuthorizedSignature()
                    );
                }


                // Upload new signature
                String signatureUrl =
                        s3Service.uploadFile(
                                authorizedSignature,
                                branchCode
                        );

                admission.setAuthorizedSignature(signatureUrl);

            } catch (Exception e) {

                throw new RuntimeException(
                        "Failed to update authorized signature",
                        e
                );
            }
        }


        // Calculate GST and total amount
        calculateAmount(admission);


        HostelAdmissionForm updatedAdmission =
                repository.save(admission);

        return mapToResponseDTO(updatedAdmission);
    }


    @Override
    public void deleteAdmission(
            Long id,
            String role,
            String email) {

        if (!staffService.hasPermission(role, email, "Delete")) {
            throw new RuntimeException(
                    "You don't have permission to delete Admission"
            );
        }

        String branchCode =
                staffService.fetchBranchCodeByRole(role, email);


        HostelAdmissionForm admission =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admission form not found with id: " + id
                                )
                        );


        // Branch-level access check
        if (!branchCode.equals(admission.getBranchCode())) {
            throw new RuntimeException(
                    "You don't have permission to delete this Admission"
            );
        }


        // Delete signature from S3
        if (admission.getAuthorizedSignature() != null &&
                !admission.getAuthorizedSignature().isBlank()) {

            s3Service.deleteFileFromUrl(
                    admission.getAuthorizedSignature()
            );
        }


        repository.delete(admission);
    }


    @Override
    public Page<HostelAdmissionResponseDTO> filterAdmissions(
            String role,
            String email,
            DateRange dateRange,
            LocalDate startDate,
            LocalDate endDate,
            String userNo,
            RoomType roomType,
            BedType bedType,
            String paymentMode,
            PaymentStatus paymentStatus,
            Pageable pageable) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException(
                    "You don't have permission to view Admissions"
            );
        }

        String branchCode =
                staffService.fetchBranchCodeByRole(role, email);


        /*
         * Branch filter
         */
        Specification<HostelAdmissionForm> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(
                                root.get("branchCode"),
                                branchCode
                        );


        /*
         * Other filters
         */
        specification = specification.and(
                Specification.allOf(
                        HostelAdmissionSpecification.hasUserNo(userNo),
                        HostelAdmissionSpecification.hasRoomType(roomType),
                        HostelAdmissionSpecification.hasBedType(bedType),
                        HostelAdmissionSpecification.hasPaymentMode(paymentMode),
                        HostelAdmissionSpecification.hasPaymentStatus(paymentStatus)
                )
        );


        // Date filter
        LocalDate today = LocalDate.now();

        if (dateRange != null) {

            LocalDate filterStartDate = null;
            LocalDate filterEndDate = today;


            switch (dateRange) {

                case TODAY:
                    filterStartDate = today;
                    filterEndDate = today;
                    break;

                case YESTERDAY:
                    filterStartDate = today.minusDays(1);
                    filterEndDate = today.minusDays(1);
                    break;

                case LAST_7_DAYS:
                    filterStartDate = today.minusDays(6);
                    filterEndDate = today;
                    break;

                case LAST_30_DAYS:
                    filterStartDate = today.minusDays(29);
                    filterEndDate = today;
                    break;

                case LAST_365_DAYS:
                    filterStartDate = today.minusDays(364);
                    filterEndDate = today;
                    break;

                case CUSTOM:
                    filterStartDate = startDate;
                    filterEndDate = endDate;
                    break;
            }


            if (filterStartDate != null &&
                    filterEndDate != null) {

                specification = specification.and(
                        HostelAdmissionSpecification.dateBetween(
                                filterStartDate,
                                filterEndDate
                        )
                );
            }
        }


        Page<HostelAdmissionForm> admissions =
                repository.findAll(
                        specification,
                        pageable
                );


        return admissions.map(this::mapToResponseDTO);
    }


    private void mapRequestToEntity(
            HostelAdmissionRequestDTO dto,
            HostelAdmissionForm admission) {

        // Student Details
        admission.setUserNo(dto.getUserNo());
        admission.setStudentName(dto.getStudentName());
        admission.setEmail(dto.getEmail());
        admission.setMobileNo(dto.getMobileNo());


        // Room Details
        admission.setRoomNumber(dto.getRoomNumber());
        admission.setFloor(dto.getFloor());
        admission.setRoomType(dto.getRoomType());
        admission.setBedType(dto.getBedType());
        admission.setBedNumber(dto.getBedNumber());


        // Admission Dates
        admission.setAdmissionDate(dto.getAdmissionDate());
        admission.setStayFrom(dto.getStayAtHostelFrom());
        admission.setStayTo(dto.getStayAtHostelTo());


        // Payment Details
        admission.setTotalMonths(dto.getTotalMonths());
        admission.setMonthlyRent(dto.getMonthlyRent());
        admission.setPaymentStatus(dto.getPaymentStatus());
        admission.setPaymentMode(dto.getPaymentMode());

        admission.setTransactionIdOrChequeNo(
                dto.getTransactionIdOrChequeNo()
        );


        // GST Details
        admission.setIncludeGst(dto.getIncludeGst());
        admission.setGstPercentage(dto.getGstPercentage());
        admission.setGstNumber(dto.getGstNumber());


        // Other Details
        admission.setSource(dto.getSource());
    }


    // ================= CALCULATE AMOUNT =================

    private void calculateAmount(
            HostelAdmissionForm admission) {

        double baseAmount = 0.0;


        if (admission.getTotalMonths() != null &&
                admission.getMonthlyRent() != null) {

            baseAmount =
                    admission.getTotalMonths()
                            * admission.getMonthlyRent();
        }


        double gstAmount = 0.0;


        if (Boolean.TRUE.equals(admission.getIncludeGst()) &&
                admission.getGstPercentage() != null) {

            gstAmount =
                    (baseAmount *
                            admission.getGstPercentage()) / 100;
        }


        admission.setGstAmount(gstAmount);

        admission.setTotalAmount(
                baseAmount + gstAmount
        );
    }


    private HostelAdmissionResponseDTO mapToResponseDTO(
            HostelAdmissionForm admission) {

        HostelAdmissionResponseDTO response =
                new HostelAdmissionResponseDTO();


        response.setId(admission.getId());


        // Student Details
        response.setUserNo(admission.getUserNo());
        response.setStudentName(admission.getStudentName());
        response.setEmail(admission.getEmail());
        response.setMobileNo(admission.getMobileNo());


        // Room Details
        response.setRoomNumber(admission.getRoomNumber());
        response.setFloor(admission.getFloor());
        response.setRoomType(admission.getRoomType());
        response.setBedType(admission.getBedType());
        response.setBedNumber(admission.getBedNumber());


        // Admission Dates
        response.setAdmissionDate(admission.getAdmissionDate());
        response.setStayAtHostelFrom(admission.getStayFrom());
        response.setStayAtHostelTo(admission.getStayTo());


        // Payment Details
        response.setTotalMonths(admission.getTotalMonths());
        response.setMonthlyRent(admission.getMonthlyRent());
        response.setPaymentStatus(admission.getPaymentStatus());
        response.setPaymentMode(admission.getPaymentMode());
        response.setTransactionIdOrChequeNo(
                admission.getTransactionIdOrChequeNo()
        );


        // GST Details
        response.setIncludeGst(admission.getIncludeGst());
        response.setGstPercentage(admission.getGstPercentage());
        response.setGstAmount(admission.getGstAmount());
        response.setGstNumber(admission.getGstNumber());


        // Final Amount
        response.setTotalAmount(
                admission.getTotalAmount()
        );


        // Other Details
        response.setSource(admission.getSource());


        // Authorized Signature
        response.setAuthorizedSignature(
                admission.getAuthorizedSignature()
        );


        return response;
    }
}