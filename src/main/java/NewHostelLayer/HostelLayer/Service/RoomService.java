package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.DTO.HostelRoomResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelRoom;

import java.util.List;

public interface RoomService
{
    HostelRoomResponseDTO createRoom(String role, String email, Long floorId, HostelRoom room);

    HostelRoomResponseDTO updateRoom(Long id, String role, String email, HostelRoom room);

    List<HostelRoomResponseDTO> getRoomsByFloor(Long floorId, String role, String email);

    HostelRoomResponseDTO getRoomById(Long id, String role, String email);

    void deleteRoom(Long id, String role, String email);
}
