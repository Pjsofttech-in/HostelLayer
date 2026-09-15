package NewHostelLayer.HostelLayer.DTO;

import NewHostelLayer.HostelLayer.Enum.BedType;
import NewHostelLayer.HostelLayer.Enum.PaymentStatus;
import NewHostelLayer.HostelLayer.Enum.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HostelAdmissionResponseDTO {

    private Long id;

    // Student Details
    private String userNo;
    private String studentName;
    private String email;
    private String mobileNo;

    // Hostel Details
    private String roomNumber;
    private String floor;
    private RoomType roomType;
    private BedType bedType;
    private String bedNumber;

    // Admission Dates
    private LocalDate admissionDate;
    private LocalDate stayAtHostelFrom;
    private LocalDate stayAtHostelTo;

    // Payment Details
    private Integer totalMonths;
    private Double monthlyRent;
    private PaymentStatus paymentStatus;
    private String paymentMode;
    private String transactionIdOrChequeNo;

    // GST Details
    private Boolean includeGst;
    private Double gstPercentage;
    private Double gstAmount;
    private String gstNumber;

    // Final Amount
    private Double totalAmount;

    // Other Details
    private String source;

    // Signature URL or file path
    private String authorizedSignature;
}
