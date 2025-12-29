package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelRoom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface
RoomRepository extends JpaRepository<HostelRoom,Long>
{
    @Query("SELECT r FROM HostelRoom r WHERE r.floor.id = :floorId AND r.branchCode = :branchCode AND r.active = true ORDER BY r.roomNumber")
    List<HostelRoom> findActiveRoomsByFloor(@Param("floorId") Long floorId, @Param("branchCode") String branchCode);


    @Modifying
    @Transactional
    @Query("UPDATE HostelRoom r SET r.active = false WHERE r.id = :id AND r.branchCode = :branchCode")
    int softDeleteRoom(@Param("id") Long id,
            @Param("branchCode") String branchCode
    );

    @Query("SELECT COUNT(r) > 0 FROM HostelRoom r WHERE r.roomNumber = :roomNumber AND r.floor.id = :floorId AND r.branchCode = :branchCode AND r.active = true")
    boolean existsRoomInFloor(@Param("roomNumber") String roomNumber, @Param("floorId") Long floorId, @Param("branchCode") String branchCode);

}
