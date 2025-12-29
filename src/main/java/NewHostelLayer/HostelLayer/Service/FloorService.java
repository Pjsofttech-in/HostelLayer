package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.DTO.HostelFloorResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelFloor;

import java.util.List;

public interface FloorService
{
    HostelFloorResponseDTO createFloor(String role, String email, HostelFloor floor);

    HostelFloorResponseDTO updateFloor(Long id, String role, String email, HostelFloor floor);

    List<HostelFloorResponseDTO> getAllFloors(String role, String email);

    HostelFloorResponseDTO getFloorById(Long id, String role, String email);

    void deleteFloor(Long id, String role, String email);
}
