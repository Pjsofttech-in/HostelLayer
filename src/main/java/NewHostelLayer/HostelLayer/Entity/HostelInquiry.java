package NewHostelLayer.HostelLayer.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HostelInquiry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentName;
    private String fatherName;
    private String fatherMobileNo;
    private String studentMobileNo;
    private String email;
    private String address;
    private String gender;

    private String city;

    private String taluka;

    private String district;
    private String state;

    private String pincode;
    private Integer numberOfMembers;
    private String courseOfStudy;

    private LocalDate inquiryDate;

    private String currentStatus;
    private String roomType;
    private String bedType;
    private String receptionName;
    private String source;
    private String conductedBy;
    private String monthName;
    private String year;

    @Email
    private String createdByEmail;
    private String branchCode;
    private String role;
}
