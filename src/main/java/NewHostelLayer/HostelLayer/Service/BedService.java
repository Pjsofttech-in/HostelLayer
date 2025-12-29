package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.DTO.HostelBedResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelBed;

import java.util.List;

public interface BedService
{

    HostelBedResponseDTO createBed(String role, String email, HostelBed bed);
    HostelBedResponseDTO updateBed(Long id,String role,String email, HostelBed bed);
    List<HostelBedResponseDTO> getAllBeds(String role,String email);
    HostelBedResponseDTO getBedById(Long id,String role,String email);
    void deleteBed(Long id,String role,String email);
    List<HostelBedResponseDTO> getBedsByRoomId(Long roomId, String role, String email);

}
