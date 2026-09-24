package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDashboardResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDateWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionMonthWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionTimeframeDataDTO;
import NewHostelLayer.HostelLayer.Enum.DashboardType;
import NewHostelLayer.HostelLayer.Service.HostelAdmissionDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admissionDashboard")
@RequiredArgsConstructor
public class HostelAdmissionDashboardController {

    private final HostelAdmissionDashboardService dashboardService;


    @GetMapping("/summary")
    public ResponseEntity<HostelAdmissionDashboardResponseDTO> getDashboardSummary() {

        return ResponseEntity.ok(
                dashboardService.getDashboardSummary()
        );
    }


    @GetMapping("/date-wise")
    public ResponseEntity<List<HostelAdmissionDateWiseDataDTO>> getDateWiseData(
            @RequestParam Integer month,
            @RequestParam Integer year) {

        return ResponseEntity.ok(
                dashboardService.getDateWiseData(month, year)
        );
    }



    @GetMapping("/timeframe")
    public ResponseEntity<List<HostelAdmissionTimeframeDataDTO>> getTimeframeData() {

        return ResponseEntity.ok(
                dashboardService.getTimeframeData()
        );
    }


    @GetMapping("/month-wise")
    public ResponseEntity<List<HostelAdmissionMonthWiseDataDTO>> getMonthWiseData(
            @RequestParam Integer year) {

        return ResponseEntity.ok(
                dashboardService.getMonthWiseData(year)
        );
    }

}
