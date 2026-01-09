package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.HostelBedResponseDTO;
import NewHostelLayer.HostelLayer.Entity.HostelBed;
import NewHostelLayer.HostelLayer.Repository.BedRepository;
import NewHostelLayer.HostelLayer.Service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedServiceImpl implements BedService
{
    @Autowired
    BedRepository bedRepository;

    @Autowired
    StaffService staffService;

    @Override
    public HostelBedResponseDTO createBed(String role, String email, HostelBed bed)
    {
        if(!staffService.hasPermission(role,email,"Post"))
        {
            throw new RuntimeException("You don't have permission to create Bed");
        }
        String branchCode =staffService.fetchBranchCodeByRole(role, email);
        bed.setBranchCode(branchCode);
        bed.setCreatedByEmail(email);
        bed.setRole(role);

        HostelBed bed1 = bedRepository.save(bed);
        return mapToDTO(bed1);
    }
    @Override
    public HostelBedResponseDTO updateBed(Long id,String role,String email, HostelBed bed)
    {
        if(!staffService.hasPermission(role,email,"PUT"))
        {
            throw new RuntimeException("You don't have permission to update Bed");
        }
        HostelBed exising = bedRepository.findById(id).orElseThrow(()-> new RuntimeException("Bed Not Found with this Id"));

        if(bed.getBedNumber()!=null && !bed.getBedNumber().isEmpty())
        {
            exising.setBedNumber(bed.getBedNumber());
        }
        if(bed.getStatus()!=null)
        {
            exising.setStatus(bed.getStatus());
        }
        if(bed.getRoom().getId() != null)
        {
            exising.setRoom(bed.getRoom());
        }
        if(bed.getBedType()!=null)
        {
            exising.setBedType(bed.getBedType());
        }
        HostelBed bed1 = bedRepository.save(exising);
        return mapToDTO(bed1);
    }

    @Override
    public List<HostelBedResponseDTO> getAllBeds(String role, String email)
    {
        if(!staffService.hasPermission(role,email,"GET"))
        {
            throw new RuntimeException("You don't have permission to Get Bed");
        }
        String branchCode = staffService.fetchBranchCodeByRole(role, email);
        return bedRepository.findAllByBranchCode(branchCode).stream().map(this::mapToDTO).toList();

    }

    @Override
    public HostelBedResponseDTO getBedById(Long id,String role,String email)
    {
        if(!staffService.hasPermission(role,email,"GET"))
        {
            throw new RuntimeException("You don't have permission to Get Bed");
        }
        HostelBed bed = bedRepository.findById(id).orElseThrow(()-> new RuntimeException("Bed Not Found"));
        return mapToDTO(bed);
    }

    @Override
    public void deleteBed(Long id,String role,String email)
    {
        if(!staffService.hasPermission(role,email,"GET"))
        {
            throw new RuntimeException("You don't have permission to DELETE Bed");
        }

        HostelBed bed = bedRepository.findById(id).orElseThrow(()-> new RuntimeException("Bed Not Found"));

        bedRepository.deleteById(id);

    }

    @Override
    public List<HostelBedResponseDTO> getBedsByRoomId(Long roomId, String role, String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException("You don't have permission to view Beds");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        List<HostelBed> beds =
                bedRepository.findBedsByRoomId(roomId, branchCode);

        return beds.stream()
                .map(this::mapToDTO)
                .toList();
    }

    private HostelBedResponseDTO mapToDTO(HostelBed bed) {

        if (bed == null) {
            return null;
        }

        HostelBedResponseDTO dto = new HostelBedResponseDTO();

        dto.setId(bed.getId());
        dto.setBedNumber(bed.getBedNumber());
        dto.setStatus(bed.getStatus());
        dto.setBedType(bed.getBedType());
        dto.setCreatedByEmail(bed.getCreatedByEmail());
        dto.setRole(bed.getRole());
        dto.setBranchCode(bed.getBranchCode());

        if (bed.getRoom() != null) {
            dto.setRoomId(bed.getRoom().getId());
        }

        return dto;
    }

}
