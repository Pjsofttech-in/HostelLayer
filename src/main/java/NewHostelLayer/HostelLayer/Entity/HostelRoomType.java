//package NewHostelLayer.HostelLayer.Entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class HostelRoomType
//{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String typeName; // SINGLE, DOUBLE, TRIPLE, FOUR
//
//    private Integer maxOccupancy;
//
//    private BigDecimal monthlyRent;
//
//    @Email
//    private String createdByEmail;
//    private String role;
//    private String branchCode;
//
//    private Boolean active = true;
//
//}
