package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDateWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDashboardResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionMonthWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionTimeframeDataDTO;

import java.util.List;

public interface HostelAdmissionDashboardService {

    HostelAdmissionDashboardResponseDTO getDashboardSummary(
            String role,
            String email
    );


    List<HostelAdmissionDateWiseDataDTO> getDateWiseData(
            Integer month,
            Integer year,
            String role,
            String email
    );


    List<HostelAdmissionTimeframeDataDTO> getTimeframeData(
            String role,
            String email
    );


    List<HostelAdmissionMonthWiseDataDTO> getMonthWiseData(
            Integer year,
            String role,
            String email
    );
}