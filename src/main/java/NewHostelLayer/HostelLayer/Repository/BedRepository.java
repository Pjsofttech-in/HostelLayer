package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelBed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<HostelBed,Long>
{

    @Query("SELECT i FROM HostelBed i WHERE i.branchCode = :branchCode")
    List<HostelBed> findAllByBranchCode(String branchCode);

    @Query("SELECT b FROM HostelBed b WHERE b.room.id = :roomId AND b.branchCode = :branchCode")
    List<HostelBed> findBedsByRoomId(@Param("roomId") Long roomId, @Param("branchCode") String branchCode
    );
}
