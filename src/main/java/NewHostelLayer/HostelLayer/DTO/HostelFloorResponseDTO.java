package NewHostelLayer.HostelLayer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelFloorResponseDTO
{
    private Long id;
    private Integer floorNumber;
    private String hostelBuildingName;
    private Boolean active;
    private String createdByEmail;
    private String role;
    private String branchCode;
}
