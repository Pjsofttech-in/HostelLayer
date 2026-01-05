package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.DTO.HostelRegistrationPageResponse;
import NewHostelLayer.HostelLayer.DTO.HostelRegistrationRequestDTO;
import NewHostelLayer.HostelLayer.DTO.HostelRegistrationResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelRegistrationFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface HostelRegistrationFormService
{

    HostelRegistrationRequestDTO saveOrUpdateRegistration(String role, String email, HostelRegistrationRequestDTO data,
                                                 MultipartFile personalPhoto, MultipartFile addressProofFile,
                                                 MultipartFile educationProofFile, MultipartFile signature, MultipartFile parentSignature);

    HostelRegistrationRequestDTO getRegistrationById(Long id, String role, String email);

    HostelRegistrationPageResponse getAllRegistrationsWithSummary(String role, String email, int page, int size,
                                                                  String dateRange, LocalDate customStartDate, LocalDate customEndDate,
                                                                  HostelRegistrationFilterRequest filter);
}
