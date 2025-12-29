package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.HostelRoomResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelFloor;
import NewHostelLayer.HostelLayer.Entity.HostelRoom;
import NewHostelLayer.HostelLayer.Repository.FloorRepository;
import NewHostelLayer.HostelLayer.Repository.RoomRepository;
import NewHostelLayer.HostelLayer.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    StaffService staffService;

    @Autowired
    FloorRepository floorRepository;

    @Override
    public HostelRoomResponseDTO createRoom(String role, String email, Long floorId, HostelRoom room) {

        if (!staffService.hasPermission(role, email, "Post"))
        {
            throw new RuntimeException("You don't have permission to create Room");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        HostelFloor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Hostel Floor not found"));

        if (roomRepository.existsRoomInFloor(
                room.getRoomNumber(), floorId, branchCode)) {
            throw new RuntimeException("Room already exists in this floor");
        }

        room.setFloor(floor);
        room.setBranchCode(branchCode);
        room.setCreatedByEmail(email);
        room.setRole(role);
        room.setActive(true);

         HostelRoom room1 =roomRepository.save(room);
         return mapToDTO(room1);
    }

    @Override
    public HostelRoomResponseDTO updateRoom(Long id, String role, String email, HostelRoom room) {

        if (!staffService.hasPermission(role, email, "Put")) {
            throw new RuntimeException("You don't have permission to update Room");
        }

        HostelRoom existing = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hostel Room not found"));

        existing.setRoomNumber(room.getRoomNumber());
        existing.setRoomType(room.getRoomType());
        existing.setTotalBeds(room.getTotalBeds());

        HostelRoom room1 = roomRepository.save(existing);
        return mapToDTO(room1);

    }

    @Override
    public List<HostelRoomResponseDTO> getRoomsByFloor(Long floorId, String role, String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException("You don't have permission to view Rooms");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);
        return roomRepository.findActiveRoomsByFloor(floorId, branchCode)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public HostelRoomResponseDTO getRoomById(Long id, String role, String email)
    {
        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException("You don't have permission to view Room");
        }

        HostelRoom room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found or inactive"));
        return mapToDTO(room);
    }

    @Override
    public void deleteRoom(Long id, String role, String email)
    {
        if (!staffService.hasPermission(role, email, "Delete")) {
            throw new RuntimeException("You don't have permission to delete Room");
        }
        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        int updated = roomRepository.softDeleteRoom(id, branchCode);
        if (updated == 0) {
            throw new RuntimeException("Room not found or already deleted");
        }
    }


    public HostelRoomResponseDTO mapToDTO(HostelRoom room) {
        if (room == null) {
            return null;
        }
        HostelRoomResponseDTO dto = new HostelRoomResponseDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setTotalBeds(room.getTotalBeds());
        dto.setBranchCode(room.getBranchCode());
        dto.setCreatedByEmail(room.getCreatedByEmail());
        dto.setRole(room.getRole());
        if (room.getFloor() != null) {
            dto.setFloorId(room.getFloor().getId());
        }

        return dto;
    }
}
