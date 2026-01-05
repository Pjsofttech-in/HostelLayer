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
public class HostelDocuments
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressProofType;
    private String educationProofType;

    private String personalPhoto;
    private String addressProofFile;
    private String educationProofFile;
    private String signature;
    private String parentSignature;

    @OneToOne
    @JoinColumn(name = "registration_id")
    @JsonBackReference
    private HostelRegistrationForm registration;
}
