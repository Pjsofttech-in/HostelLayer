package NewHostelLayer.HostelLayer.DTO;

import NewHostelLayer.HostelLayer.Entity.BedStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelBedResponseDTO
{
    private Long id;
    private String bedNumber;
    private String bedType;
    private BedStatus status;
    private Long roomId;

    private String createdByEmail;
    private String role;
    private String branchCode;
}
