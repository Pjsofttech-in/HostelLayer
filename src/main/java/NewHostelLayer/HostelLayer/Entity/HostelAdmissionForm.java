package NewHostelLayer.HostelLayer.Entity;

import NewHostelLayer.HostelLayer.Enum.BedType;
import NewHostelLayer.HostelLayer.Enum.PaymentStatus;
import NewHostelLayer.HostelLayer.Enum.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "hostel_admission_form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostelAdmissionForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student Details

    @NotBlank(message = "User number is required")
    private String userNo;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Mobile number is required")
    private String mobileNo;


    // Room Details

    @NotBlank(message = "Floor is required")
    private String floor;

    @NotNull(message = "Room type is required")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Bed type is required")
    @Enumerated(EnumType.STRING)
    private BedType bedType;

    @NotBlank(message = "Bed number is required")
    private String bedNumber;


    // Admission Details

    @NotNull(message = "Admission date is required")
    private LocalDate admissionDate;

    @NotNull(message = "Stay from date is required")
    private LocalDate stayFrom;

    @NotNull(message = "Stay to date is required")
    private LocalDate stayTo;

    private Integer totalMonths;

    private Double monthlyRent;


    // Payment Details
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentMode;

    private String transactionIdOrChequeNo;

    // GST Details
    private Boolean includeGst = false;

    private Double gstPercentage;

    private Double gstAmount;

    private String gstNumber;

    private Double totalAmount;


    private String source;

    // Authorized Signature
    private String authorizedSignature;

    @Email
    private String createdByEmail;

    private String branchCode;

    private String role;
}
