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


    @PostMapping("/createAdmissionForm")
    public ResponseEntity<HostelAdmissionResponseDTO> createAdmissionForm(

            @RequestPart("admissionForm") String admissionFormJson,

            @RequestParam(
                    value = "authorizedSignature",
                    required = false
            ) MultipartFile authorizedSignature,

            @RequestParam String role,

            @RequestParam String email

    ) throws Exception {

        HostelAdmissionRequestDTO requestDTO =
                objectMapper.readValue(
                        admissionFormJson,
                        HostelAdmissionRequestDTO.class
                );

        return ResponseEntity.ok(
                hostelAdmissionFormService.createAdmissionForm(
                        role,
                        email,
                        requestDTO,
                        authorizedSignature
                )
        );
    }


    @GetMapping("/getAllAdmissionForms")
    public ResponseEntity<List<HostelAdmissionResponseDTO>> getAllAdmissions(

            @RequestParam String role,

            @RequestParam String email

    ) {

        return ResponseEntity.ok(
                hostelAdmissionFormService.getAllAdmissions(
                        role,
                        email
                )
        );
    }


    @GetMapping("/getAdmissionFormById/{id}")
    public ResponseEntity<HostelAdmissionResponseDTO> getAdmissionById(

            @PathVariable Long id,

            @RequestParam String role,

            @RequestParam String email

    ) {

        return ResponseEntity.ok(
                hostelAdmissionFormService.getAdmissionById(
                        id,
                        role,
                        email
                )
        );
    }


    @GetMapping("/getAdmissionSummary")
    public ResponseEntity<HostelAdmissionSummaryDTO> getAdmissionSummary(

            @RequestParam String role,

            @RequestParam String email

    ) {

        return ResponseEntity.ok(
                hostelAdmissionFormService.getAdmissionSummary(
                        role,
                        email
                )
        );
    }


    @PutMapping("/updateAdmissionForm/{id}")
    public ResponseEntity<HostelAdmissionResponseDTO> updateAdmission(

            @PathVariable Long id,

            @RequestPart("admissionForm") String admissionFormJson,

            @RequestParam(
                    value = "authorizedSignature",
                    required = false
            ) MultipartFile authorizedSignature,

            @RequestParam String role,

            @RequestParam String email

    ) throws Exception {

        HostelAdmissionRequestDTO requestDTO =
                objectMapper.readValue(
                        admissionFormJson,
                        HostelAdmissionRequestDTO.class
                );

        return ResponseEntity.ok(
                hostelAdmissionFormService.updateAdmission(
                        id,
                        role,
                        email,
                        requestDTO,
                        authorizedSignature
                )
        );
    }


    @GetMapping("/filterAdmissionForms")
    public ResponseEntity<Page<HostelAdmissionResponseDTO>> filterAdmissions(

            @RequestParam String role,

            @RequestParam String email,

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
                        role,
                        email,
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


    @DeleteMapping("/deleteAdmissionForm/{id}")
    public ResponseEntity<String> deleteAdmission(

            @PathVariable Long id,

            @RequestParam String role,

            @RequestParam String email

    ) {

        hostelAdmissionFormService.deleteAdmission(
                id,
                role,
                email
        );

        return ResponseEntity.ok(
                "Hostel admission deleted successfully"
        );
    }
}