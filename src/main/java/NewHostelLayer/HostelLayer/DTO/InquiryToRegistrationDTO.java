package NewHostelLayer.HostelLayer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryToRegistrationDTO
{
    private String fullName;
    private String gender;
    private String email;
    private String parentsMobNo;
    private String permanentAddress;
    private String city;
    private String state;
    private String pincode;
    private String courseEnrolled;
    private String roomType;
    private String bedType;
    private boolean isRegister;
    private Long inquiryId;
    private String createdByEmail;
    private String branchCode;
    private String role;
}
