package NewHostelLayer.HostelLayer.Mapper;

import NewHostelLayer.HostelLayer.DTO.HostelRegistrationRequestDTO;
import NewHostelLayer.HostelLayer.DTO.HostelRegistrationResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelDocuments;
import NewHostelLayer.HostelLayer.Entity.HostelGuardianMedical;
import NewHostelLayer.HostelLayer.Entity.HostelRegistrationForm;
import org.springframework.stereotype.Component;


@Component
public class HostelRegistrationMapper
{

    public static HostelRegistrationForm toEntity(HostelRegistrationRequestDTO dto)
    {
        HostelRegistrationForm entity = new HostelRegistrationForm();

        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setEmail(dto.getEmail());
        entity.setMaritalStatus(dto.getMaritalStatus());
        entity.setBloodGroup(dto.getBloodGroup());
        entity.setReligion(dto.getReligion());

        entity.setYearOfAdmission(dto.getYearOfAdmission());
        entity.setCurrentInstitution(dto.getCurrentInstitution());
        entity.setCourseEnrolled(dto.getCourseEnrolled());
        entity.setCurrentEmployer(dto.getCurrentEmployer());
        entity.setDesignation(dto.getDesignation());
        entity.setYearOfJoining(dto.getYearOfJoining());

        entity.setParentsName(dto.getParentsName());
        entity.setParentsMobNo(dto.getParentsMobNo());
        entity.setPermanentAddress(dto.getPermanentAddress());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setPincode(dto.getPincode());

        entity.setStayFrom(dto.getStayFrom());
        entity.setStayTo(dto.getStayTo());
        entity.setFloor(dto.getFloor());
        entity.setRoomNumber(dto.getRoomNumber());
        entity.setBedType(dto.getBedType());
        entity.setBedNumber(dto.getBedNumber());
        entity.setRoomType(dto.getRoomType());

        entity.setDepositCollected(dto.getDepositCollected());
        entity.setTotalFees(dto.getTotalFees());
        entity.setRemainingFees(dto.getRemainingFees());

        entity.setPaymentMode(dto.getPaymentMode());
        entity.setTransactionNumber(dto.getTransactionNumber());
        entity.setRemark(dto.getRemark());
        entity.setStatus(dto.getStatus());
        entity.setAdmissionDate(dto.getAdmissionDate());

        entity.setCreatedByEmail(dto.getCreatedByEmail());
        entity.setRole(dto.getRole());
        entity.setBranchCode(dto.getBranchCode());

        if (dto.getGuardianMedical() != null) {
            HostelGuardianMedical gm = dto.getGuardianMedical();
            gm.setRegistration(entity);
            entity.setGuardianMedical(gm);
        }

        if (dto.getDocuments() != null) {
            HostelDocuments docs = dto.getDocuments();
            docs.setRegistration(entity);
            entity.setDocuments(docs);
        }

        return entity;
    }

    public static HostelRegistrationRequestDTO toDTO(HostelRegistrationForm entity) {

        HostelRegistrationRequestDTO dto =
                new HostelRegistrationRequestDTO();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setEmail(entity.getEmail());
        dto.setMaritalStatus(entity.getMaritalStatus());
        dto.setBloodGroup(entity.getBloodGroup());
        dto.setReligion(entity.getReligion());

        dto.setYearOfAdmission(entity.getYearOfAdmission());
        dto.setCurrentInstitution(entity.getCurrentInstitution());
        dto.setCourseEnrolled(entity.getCourseEnrolled());
        dto.setCurrentEmployer(entity.getCurrentEmployer());
        dto.setDesignation(entity.getDesignation());
        dto.setYearOfJoining(entity.getYearOfJoining());

        dto.setParentsName(entity.getParentsName());
        dto.setParentsMobNo(entity.getParentsMobNo());
        dto.setPermanentAddress(entity.getPermanentAddress());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPincode(entity.getPincode());

        dto.setStayFrom(entity.getStayFrom());
        dto.setStayTo(entity.getStayTo());
        dto.setFloor(entity.getFloor());
        dto.setRoomNumber(entity.getRoomNumber());
        dto.setBedType(entity.getBedType());
        dto.setBedNumber(entity.getBedNumber());
        dto.setRoomType(entity.getRoomType());

        dto.setDepositCollected(entity.getDepositCollected());
        dto.setTotalFees(entity.getTotalFees());
        dto.setRemainingFees(entity.getRemainingFees());

        dto.setPaymentMode(entity.getPaymentMode());
        dto.setTransactionNumber(entity.getTransactionNumber());
        dto.setRemark(entity.getRemark());
        dto.setStatus(entity.getStatus());
        dto.setAdmissionDate(entity.getAdmissionDate());

        dto.setCreatedByEmail(entity.getCreatedByEmail());
        dto.setBranchCode(entity.getBranchCode());
        dto.setRole(entity.getRole());

        dto.setGuardianMedical(entity.getGuardianMedical());
        dto.setDocuments(entity.getDocuments());

        return dto;
    }


    public HostelRegistrationResponseDTO mapToDTO(HostelRegistrationForm entity) {

        HostelRegistrationResponseDTO dto = new HostelRegistrationResponseDTO();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setEmail(entity.getEmail());
        dto.setMaritalStatus(entity.getMaritalStatus());
        dto.setBloodGroup(entity.getBloodGroup());
        dto.setReligion(entity.getReligion());

        dto.setYearOfAdmission(entity.getYearOfAdmission());
        dto.setCurrentInstitution(entity.getCurrentInstitution());
        dto.setCourseEnrolled(entity.getCourseEnrolled());
        dto.setCurrentEmployer(entity.getCurrentEmployer());
        dto.setDesignation(entity.getDesignation());
        dto.setYearOfJoining(entity.getYearOfJoining());

        dto.setParentsName(entity.getParentsName());
        dto.setParentsMobNo(entity.getParentsMobNo());
        dto.setPermanentAddress(entity.getPermanentAddress());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPincode(entity.getPincode());

        dto.setStayFrom(entity.getStayFrom());
        dto.setStayTo(entity.getStayTo());
        dto.setFloor(entity.getFloor());
        dto.setRoomNumber(entity.getRoomNumber());
        dto.setBedType(entity.getBedType());
        dto.setBedNumber(entity.getBedNumber());
        dto.setRoomType(entity.getRoomType());

        dto.setDepositCollected(entity.getDepositCollected());
        dto.setTotalFees(entity.getTotalFees());
        dto.setRemainingFees(entity.getRemainingFees());

        dto.setPaymentMode(entity.getPaymentMode());
        dto.setTransactionNumber(entity.getTransactionNumber());
        dto.setRemark(entity.getRemark());
        dto.setStatus(entity.getStatus());
        dto.setAdmissionDate(entity.getAdmissionDate());

        dto.setCreatedByEmail(entity.getCreatedByEmail());
        dto.setBranchCode(entity.getBranchCode());
        dto.setRole(entity.getRole());

        return dto;
    }
}
