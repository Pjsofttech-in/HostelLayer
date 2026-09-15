package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.HostelAdmissionRequestDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionSummaryDTO;
import NewHostelLayer.HostelLayer.Enum.BedType;
import NewHostelLayer.HostelLayer.Enum.DateRange;
import NewHostelLayer.HostelLayer.Enum.PaymentStatus;
import NewHostelLayer.HostelLayer.Enum.RoomType;
import NewHostelLayer.HostelLayer.Service.HostelAdmissionFormService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HostelAdmissionFormController {

    private final HostelAdmissionFormService hostelAdmissionFormService;
    private final ObjectMapper objectMapper;


    // Create Admission Form
    @PostMapping("/createAdmissionForm")
    public ResponseEntity<HostelAdmissionResponseDTO> createAdmissionForm(

            @RequestPart("admissionForm") String admissionFormJson,

            @RequestParam(
                    value = "authorizedSignature",
                    required = false
            ) MultipartFile authorizedSignature

    ) throws Exception {

        HostelAdmissionRequestDTO requestDTO =
                objectMapper.readValue(
                        admissionFormJson,
                        HostelAdmissionRequestDTO.class);

        return ResponseEntity.ok(
                hostelAdmissionFormService.createAdmissionForm(
                        requestDTO,
                        authorizedSignature));
    }


    // Get All Admission Forms
    @GetMapping("/getAllAdmissionForms")
    public ResponseEntity<List<HostelAdmissionResponseDTO>> getAllAdmissions() {

        return ResponseEntity.ok(
                hostelAdmissionFormService.getAllAdmissions()
        );
    }


    // Get Admission Form By ID
    @GetMapping("/getAdmissionFormById/{id}")
    public ResponseEntity<HostelAdmissionResponseDTO> getAdmissionById(@PathVariable Long id) {

        return ResponseEntity.ok(
                hostelAdmissionFormService.getAdmissionById(id)
        );
    }

    // ================= ADMISSION SUMMARY =================

    @GetMapping("/getAdmissionSummary")
    public ResponseEntity<HostelAdmissionSummaryDTO> getAdmissionSummary() {

        return ResponseEntity.ok(
                hostelAdmissionFormService.getAdmissionSummary()
        );
    }

    // Update Admission Form
    @PutMapping("/updateAdmissionForm/{id}")
    public ResponseEntity<HostelAdmissionResponseDTO> updateAdmission(

            @PathVariable Long id,

            @RequestPart("admissionForm") String admissionFormJson,

            @RequestParam(
                    value = "authorizedSignature",
                    required = false)
            MultipartFile authorizedSignature

    ) throws Exception {

        HostelAdmissionRequestDTO requestDTO =
                objectMapper.readValue(
                        admissionFormJson,
                        HostelAdmissionRequestDTO.class
                );

        return ResponseEntity.ok(
                hostelAdmissionFormService.updateAdmission(
                        id,
                        requestDTO,
                        authorizedSignature
                )
        );
    }

    @GetMapping("/filterAdmissionForms")
    public ResponseEntity<Page<HostelAdmissionResponseDTO>> filterAdmissions(

            @RequestParam(required = false)
            DateRange dateRange,

            @RequestParam(required = false)
            LocalDate startDate,

            @RequestParam(required = false)
            LocalDate endDate,

            @RequestParam(required = false)
            String userNo,

            @RequestParam(required = false)
            RoomType roomType,

            @RequestParam(required = false)
            BedType bedType,

            @RequestParam(required = false)
            String paymentMode,

            @RequestParam(required = false)
            PaymentStatus paymentStatus,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return ResponseEntity.ok(
                hostelAdmissionFormService.filterAdmissions(
                        dateRange,
                        startDate,
                        endDate,
                        userNo,
                        roomType,
                        bedType,
                        paymentMode,
                        paymentStatus,
                        pageable
                )
        );
    }


    // Delete Admission Form
    @DeleteMapping("/deleteAdmissionForm/{id}")
    public ResponseEntity<String> deleteAdmission(
            @PathVariable Long id) {

        hostelAdmissionFormService.deleteAdmission(id);

        return ResponseEntity.ok(
                "Hostel admission deleted successfully"
        );
    }
}