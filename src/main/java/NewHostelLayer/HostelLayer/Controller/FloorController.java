package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.HostelFloorResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelFloor;
import NewHostelLayer.HostelLayer.Service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FloorController
{

    @Autowired
    FloorService floorService;

    @PostMapping("/createFloor")
    public HostelFloorResponseDTO createFloor(@RequestParam String role, @RequestParam String email,
                                              @RequestBody HostelFloor floor) {

        return floorService.createFloor(role, email, floor);
    }

    @PutMapping("/updateFloor/{id}")
    public HostelFloorResponseDTO updateFloor(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email,
            @RequestBody HostelFloor floor) {

        return floorService.updateFloor(id, role, email, floor);
    }

    @GetMapping("/getAllFloor")
    public List<HostelFloorResponseDTO> getAllFloors(
            @RequestParam String role,
            @RequestParam String email) {

        return floorService.getAllFloors(role, email);
    }

    @GetMapping("/getFloorById/{id}")
    public HostelFloorResponseDTO getFloorById(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email) {

        return floorService.getFloorById(id, role, email);
    }

    @DeleteMapping("/deleteFloor/{id}")
    public String deleteFloor(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email) {

        floorService.deleteFloor(id, role, email);
        return "Hostel Floor deleted successfully";
    }

}
