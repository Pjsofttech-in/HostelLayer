package NewHostelLayer.HostelLayer.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HostelFloor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer floorNumber;
    private String hostelBuildingName;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<HostelRoom> rooms;

    private Boolean active = true;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;
}
