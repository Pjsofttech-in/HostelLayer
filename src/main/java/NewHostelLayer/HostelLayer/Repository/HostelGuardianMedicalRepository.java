package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelGuardianMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HostelGuardianMedicalRepository extends JpaRepository<HostelGuardianMedical,Long> {
}
