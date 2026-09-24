package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDashboardResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDateWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionMonthWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionTimeframeDataDTO;
import NewHostelLayer.HostelLayer.Service.HostelAdmissionDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admissionDashboard")
@RequiredArgsConstructor
public class HostelAdmissionDashboardController {

    private final HostelAdmissionDashboardService dashboardService;


    @GetMapping("/summary")
    public ResponseEntity<HostelAdmissionDashboardResponseDTO> getDashboardSummary(
            @RequestParam String role,
            @RequestParam String email) {

        return ResponseEntity.ok(
                dashboardService.getDashboardSummary(role, email)
        );
    }


    @GetMapping("/date-wise")
    public ResponseEntity<List<HostelAdmissionDateWiseDataDTO>> getDateWiseData(
            @RequestParam Integer month,
            @RequestParam Integer year,
            @RequestParam String role,
            @RequestParam String email) {

        return ResponseEntity.ok(
                dashboardService.getDateWiseData(month, year, role, email)
        );
    }


    @GetMapping("/timeframe")
    public ResponseEntity<List<HostelAdmissionTimeframeDataDTO>> getTimeframeData(
            @RequestParam String role,
            @RequestParam String email) {

        return ResponseEntity.ok(
                dashboardService.getTimeframeData(role, email)
        );
    }


    @GetMapping("/month-wise")
    public ResponseEntity<List<HostelAdmissionMonthWiseDataDTO>> getMonthWiseData(
            @RequestParam Integer year,
            @RequestParam String role,
            @RequestParam String email) {

        return ResponseEntity.ok(
                dashboardService.getMonthWiseData(year, role, email)
        );
    }

}