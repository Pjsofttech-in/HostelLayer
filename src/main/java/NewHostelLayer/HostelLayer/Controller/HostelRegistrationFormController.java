package NewHostelLayer.HostelLayer.Controller;


import NewHostelLayer.HostelLayer.DTO.HostelRegistrationPageResponse;
import NewHostelLayer.HostelLayer.DTO.HostelRegistrationRequestDTO;
import NewHostelLayer.HostelLayer.DTO.HostelRegistrationResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelRegistrationFilterRequest;
import NewHostelLayer.HostelLayer.Service.HostelRegistrationFormService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
public class HostelRegistrationFormController
{

    @Autowired
    HostelRegistrationFormService registrationFormService;

    @PostMapping(value = "/createRegistrationForm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRegistrationForm(
            @RequestParam String role,
            @RequestParam String email,

            @RequestPart("data") String dataJson,
            @RequestParam(value = "inquiryId",required = false) Long inquiryId,
            @RequestPart(value = "personalPhoto", required = false) MultipartFile personalPhoto,
            @RequestPart(value = "addressProofFile", required = false) MultipartFile addressProofFile,
            @RequestPart(value = "educationProofFile", required = false) MultipartFile educationProofFile,
            @RequestPart(value = "signature", required = false) MultipartFile signature,
            @RequestPart(value = "parentSignature", required = false) MultipartFile parentSignature
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        HostelRegistrationRequestDTO data = mapper.readValue(dataJson, HostelRegistrationRequestDTO.class);

        HostelRegistrationRequestDTO response =
                registrationFormService.saveOrUpdateRegistration(
                        role, email,inquiryId, data,
                        personalPhoto,
                        addressProofFile,
                        educationProofFile,
                        signature,
                        parentSignature
                );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/getRegistrationFormById/{id}")
    public ResponseEntity<HostelRegistrationRequestDTO> getRegistrationFormById(@PathVariable Long id,@RequestParam String role,
                                                                                @RequestParam String email)
    {
        HostelRegistrationRequestDTO response = registrationFormService.getRegistrationById(id, role, email);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/getRegistrationForms")
    public HostelRegistrationPageResponse getAllRegistrations(@RequestParam String role, @RequestParam String email,

                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,

                                                                    @RequestParam(required = false) String dateRange,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

                                                                    @RequestBody(required = false) HostelRegistrationFilterRequest filter)
    {
        return registrationFormService.getAllRegistrationsWithSummary(role, email, page, size,
                dateRange, startDate, endDate, filter);
    }



}
