package NewHostelLayer.HostelLayer.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HostelGuardianMedical
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guardianFullName;
    private String guardianPermanentAddress;
    private String mobileNo;

    private String medicalCondition;
    private String allergies;
    private String familyDoctorFullName;
    private String doctormobileNo;

    private String vehicleType;
    private String registrationNo;

    @OneToOne
    @JoinColumn(name = "registration_id")
    @JsonBackReference
    private HostelRegistrationForm registration;
}
