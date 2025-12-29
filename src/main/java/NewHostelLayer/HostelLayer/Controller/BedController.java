package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.HostelBedResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelBed;
import NewHostelLayer.HostelLayer.Service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BedController
{

    @Autowired
    BedService bedService;

    @PostMapping("/createBed")
    public ResponseEntity<HostelBedResponseDTO> createBed(@RequestBody HostelBed bed, @RequestParam String role, @RequestParam String email)
    {
        HostelBedResponseDTO responseDTO =
                bedService.createBed(role, email, bed);

        return ResponseEntity.ok(responseDTO);

    }

    @PutMapping("/updateBed/{id}")
    public ResponseEntity<HostelBedResponseDTO> updateBed(@PathVariable Long id, @RequestBody HostelBed bed,
                                               @RequestParam String role,@RequestParam String email)
    {
        HostelBedResponseDTO bed1 = bedService.updateBed(id, role, email, bed);
        return ResponseEntity.ok(bed1);
    }

    @GetMapping("/getAllBed")
    public ResponseEntity<List<HostelBedResponseDTO>> getAllBed(@RequestParam String role,@RequestParam String email)
    {
        List<HostelBedResponseDTO> bed = bedService.getAllBeds(role, email);
        return ResponseEntity.ok(bed);
    }

    @GetMapping("/getBedById/{id}")
    public ResponseEntity<HostelBedResponseDTO> getBedById(@PathVariable Long id,@RequestParam String role,
                                                @RequestParam String email)
    {
        HostelBedResponseDTO bed = bedService.getBedById(id, role, email);
        return ResponseEntity.ok(bed);
    }

    @DeleteMapping("/deleteBedById/{id}")
    public ResponseEntity<String> deleteBed(@PathVariable Long id,@RequestParam String role,
                            @RequestParam String email)
    {
        bedService.deleteBed(id, role, email);
        return ResponseEntity.ok("Bed Delete SuccessFully");
    }

    @GetMapping("/getBedsByRoom")
    public ResponseEntity<List<HostelBedResponseDTO>> getBedsByRoom(
            @RequestParam Long roomId,
            @RequestParam String role,
            @RequestParam String email) {

        List<HostelBedResponseDTO> beds =
                bedService.getBedsByRoomId(roomId, role, email);

        return ResponseEntity.ok(beds);
    }



}
