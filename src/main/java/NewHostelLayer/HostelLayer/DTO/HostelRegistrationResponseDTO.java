package NewHostelLayer.HostelLayer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelRegistrationResponseDTO
{
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String maritalStatus;
    private String bloodGroup;
    private String religion;

    private String yearOfAdmission;
    private String currentInstitution;
    private String courseEnrolled;
    private String currentEmployer;
    private String designation;
    private String yearOfJoining;

    private String parentsName;
    private String parentsMobNo;
    private String permanentAddress;
    private String city;
    private String state;
    private String pincode;

    private String stayFrom;
    private String stayTo;

    private String floor;
    private String roomNumber;
    private String bedType;
    private String bedNumber;
    private String roomType;

    private Double depositCollected;
    private Double totalFees;
    private Double remainingFees;

    private String paymentMode;
    private String transactionNumber;
    private String remark;
    private String status;
    private LocalDate admissionDate;

    private String addressProofType;
    private String educationProofType;


    private String createdByEmail;
    private String role;
    private String branchCode;

}
