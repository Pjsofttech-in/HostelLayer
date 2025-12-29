package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.HostelRoomResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelRoom;
import NewHostelLayer.HostelLayer.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController
{

    @Autowired
    RoomService roomService;

    @PostMapping("/createRoom")
    public HostelRoomResponseDTO createRoom(@RequestParam String role, @RequestParam String email,
            @RequestParam Long floorId, @RequestBody HostelRoom room) {

        return roomService.createRoom(role, email, floorId, room);
    }

    @PutMapping("/updateRoom/{id}")
    public HostelRoomResponseDTO updateRoom(@PathVariable Long id, @RequestParam String role,
            @RequestParam String email, @RequestBody HostelRoom room) {

        return roomService.updateRoom(id, role, email, room);
    }

    @GetMapping("/getRoomsByFloor/{floorId}")
    public List<HostelRoomResponseDTO> getRoomsByFloor(@PathVariable Long floorId, @RequestParam String role, @RequestParam String email) {

        return roomService.getRoomsByFloor(floorId, role, email);
    }

    @GetMapping("/getRoomById/{id}")
    public HostelRoomResponseDTO getRoomById(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email) {

        return roomService.getRoomById(id, role, email);
    }

    @DeleteMapping("/deleteRoomById/{id}")
    public String deleteRoom(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email) {

        roomService.deleteRoom(id, role, email);
        return "Hostel Room deleted successfully";
    }
    
    
}
