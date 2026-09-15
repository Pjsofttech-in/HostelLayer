package NewHostelLayer.HostelLayer.Repository;



import NewHostelLayer.HostelLayer.Entity.HostelAdmissionForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HostelAdmissionFormRepository
        extends JpaRepository<HostelAdmissionForm, Long>,
        JpaSpecificationExecutor<HostelAdmissionForm> {
}
