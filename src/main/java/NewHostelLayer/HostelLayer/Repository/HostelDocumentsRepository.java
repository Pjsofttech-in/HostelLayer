package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelDocumentsRepository extends JpaRepository<HostelDocuments,Long>
{


}
