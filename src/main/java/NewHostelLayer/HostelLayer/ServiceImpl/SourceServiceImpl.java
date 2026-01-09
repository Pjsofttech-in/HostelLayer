package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.Entity.HostelSource;
import NewHostelLayer.HostelLayer.Repository.SourceRepository;
import NewHostelLayer.HostelLayer.Service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceServiceImpl implements SourceService
{

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    StaffService staffService;

    @Override
    public HostelSource createSource(HostelSource source, String role, String email)
    {
        if (!staffService.hasPermission(role,email,"Post"))
        {
            throw new RuntimeException("You Don't have permission to create Source");
        }
        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        source.setRole(role);
        source.setCreatedByEmail(email);
        source.setBranchCode(branchCode);

        return sourceRepository.save(source);
    }

    @Override
    public HostelSource updateSource(Long id,HostelSource source, String role, String email)
    {
        if (!staffService.hasPermission(role,email,"Put"))
        {
            throw new RuntimeException("You Don't have permission to Update Source");
        }

        HostelSource existing = sourceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Source Not Found"));

        existing.setSourceName(source.getSourceName());

        return sourceRepository.save(existing);

    }

    @Override
    public HostelSource getSourceById(Long id, String role, String email)
    {
        if (!staffService.hasPermission(role,email,"Get"))
        {
            throw new RuntimeException("You Don't have permission to Get Source");
        }

        HostelSource source = sourceRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Source Not Found"));

        return source;

    }

    @Override
    public List<HostelSource> getAllSourceByBranchCode(String role, String email)
    {
        if (!staffService.hasPermission(role,email,"Get"))
        {
            throw new RuntimeException("You Don't have permission to Get Source");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        return sourceRepository.findAllSourceByBranch(branchCode);

    }

    @Override
    public void deleteSource(Long id,String role, String email)
    {
        if (!staffService.hasPermission(role,email,"Delete"))
        {
            throw new RuntimeException("You Don't have permission to Get Source");
        }

        HostelSource source = sourceRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Source Not Found"));

        sourceRepository.deleteById(id);
    }
}
