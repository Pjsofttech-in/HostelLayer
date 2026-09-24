package NewHostelLayer.HostelLayer.Service;



import NewHostelLayer.HostelLayer.DTO.DashboardMetricDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDateWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDashboardResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionMonthWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionTimeframeDataDTO;
import NewHostelLayer.HostelLayer.Enum.DashboardType;

import java.util.List;

public interface HostelAdmissionDashboardService {


    HostelAdmissionDashboardResponseDTO getDashboardSummary();


    List<HostelAdmissionDateWiseDataDTO> getDateWiseData(
            Integer month,
            Integer year
    );


    List<HostelAdmissionTimeframeDataDTO> getTimeframeData();

    List<HostelAdmissionMonthWiseDataDTO> getMonthWiseData(
            Integer year
    );
}
