package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloorRepository extends JpaRepository<HostelFloor,Long>
{

    @Query("SELECT f FROM HostelFloor f WHERE f.branchCode = :branchCode AND f.active = true ORDER BY f.floorNumber ASC")
    List<HostelFloor> findActiveFloorsByBranch(@Param("branchCode") String branchCode);

}
