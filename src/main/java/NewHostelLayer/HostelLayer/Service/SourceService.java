package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.Entity.HostelSource;

import java.util.List;

public interface SourceService
{

    HostelSource createSource(HostelSource source, String role, String email);
    HostelSource updateSource(Long id,HostelSource source, String role, String email);
    HostelSource getSourceById(Long id, String role, String email);
    List<HostelSource> getAllSourceByBranchCode(String role, String email);
    void deleteSource(Long id,String role, String email);

}
