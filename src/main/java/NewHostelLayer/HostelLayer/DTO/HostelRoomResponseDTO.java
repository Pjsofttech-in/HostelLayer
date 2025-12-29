package NewHostelLayer.HostelLayer.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelRoomResponseDTO
{
    private Long id;
    private String roomNumber;
    private String roomType;
    private Integer totalBeds;
    private Long floorId;
    private String createdByEmail;
    private String role;
    private String branchCode;
}
