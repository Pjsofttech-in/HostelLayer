package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepository extends JpaRepository<HostelSource,Long>
{
    @Query("SELECT s FROM HostelSource s WHERE s.branchCode = :branchCode ORDER BY s.id ASC")
    List<HostelSource> findAllSourceByBranch(@Param("branchCode") String branchCode);

}
