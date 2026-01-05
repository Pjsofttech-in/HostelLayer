package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelInquiry;
import NewHostelLayer.HostelLayer.Entity.HostelRegistrationForm;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostelRegistrationFormRepository extends JpaRepository<HostelRegistrationForm,Long>, JpaSpecificationExecutor<HostelRegistrationForm>
{
    @Query("SELECT h FROM HostelAdmission h WHERE h.email = :email")
    Optional<HostelRegistrationForm> findByEmail(@Param("email") String email);

    @Query("SELECT h FROM HostelAdmission h WHERE h.status = :status")
    List<HostelRegistrationForm> findByStatus(@Param("status") String status);


    @Query("SELECT h FROM HostelRegistrationForm h WHERE h.branchCode = :branchCode")
    List<HostelRegistrationForm> findAllByBranchCode(@Param("branchCode") String branchCode);

}
