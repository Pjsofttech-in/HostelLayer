package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.HostelFloorResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelFloor;
import NewHostelLayer.HostelLayer.Repository.FloorRepository;
import NewHostelLayer.HostelLayer.Service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImpl implements FloorService
{
    @Autowired
    FloorRepository floorRepository;
    
    @Autowired
    StaffService staffService;
    
    @Override
    public HostelFloorResponseDTO createFloor(String role, String email, HostelFloor floor) {

        if (!staffService.hasPermission(role, email, "Post")) {
            throw new RuntimeException("You don't have permission to create Floor");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        floor.setBranchCode(branchCode);
        floor.setCreatedByEmail(email);
        floor.setRole(role);
        floor.setActive(true);

        HostelFloor floor1 = floorRepository.save(floor);
        return mapToDTO(floor1);
    }

    @Override
    public HostelFloorResponseDTO updateFloor(Long id, String role, String email, HostelFloor floor) {

        if (!staffService.hasPermission(role, email, "Put")) {
            throw new RuntimeException("You don't have permission to update Floor");
        }

        HostelFloor existing = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hostel Floor not found with id: " + id));

        existing.setFloorNumber(floor.getFloorNumber());
        existing.setHostelBuildingName(floor.getHostelBuildingName());
        existing.setRooms(floor.getRooms());

        HostelFloor floor1 = floorRepository.save(existing);
        return mapToDTO(floor1);
    }

    @Override
    public List<HostelFloorResponseDTO> getAllFloors(String role, String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException("You don't have permission to view Floors");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);
        return floorRepository.findActiveFloorsByBranch(branchCode).stream().map(this::mapToDTO).toList();
    }

    @Override
    public HostelFloorResponseDTO getFloorById(Long id, String role, String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException("You don't have permission to view Floor");
        }

        HostelFloor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hostel Floor not found with id: " + id));

        return mapToDTO(floor);
    }

    @Override
    public void deleteFloor(Long id, String role, String email) {

        if (!staffService.hasPermission(role, email, "Delete")) {
            throw new RuntimeException("You don't have permission to delete Floor");
        }

        HostelFloor floor = floorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hostel Floor not found with id: " + id));

        floor.setActive(false);   // soft delete
        floorRepository.save(floor);
    }
    private HostelFloorResponseDTO mapToDTO(HostelFloor floor) {

        if (floor == null) {
            return null;
        }

        HostelFloorResponseDTO dto = new HostelFloorResponseDTO();

        dto.setId(floor.getId());
        dto.setFloorNumber(floor.getFloorNumber());
        dto.setHostelBuildingName(floor.getHostelBuildingName());
        dto.setActive(floor.getActive());

        dto.setCreatedByEmail(floor.getCreatedByEmail());
        dto.setRole(floor.getRole());
        dto.setBranchCode(floor.getBranchCode());

        return dto;
    }

}
