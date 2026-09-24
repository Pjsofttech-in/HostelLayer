package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.DTO.HostelAdmissionRequestDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionSummaryDTO;
import NewHostelLayer.HostelLayer.Enum.BedType;
import NewHostelLayer.HostelLayer.Enum.DateRange;
import NewHostelLayer.HostelLayer.Enum.PaymentStatus;
import NewHostelLayer.HostelLayer.Enum.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface HostelAdmissionFormService {

    HostelAdmissionResponseDTO createAdmissionForm(
            String role,
            String email,
            HostelAdmissionRequestDTO requestDTO,
            MultipartFile authorizedSignature
    );


    List<HostelAdmissionResponseDTO> getAllAdmissions(
            String role,
            String email
    );


    HostelAdmissionResponseDTO getAdmissionById(
            Long id,
            String role,
            String email
    );



    HostelAdmissionSummaryDTO getAdmissionSummary(
            String role,
            String email
    );


    HostelAdmissionResponseDTO updateAdmission(
            Long id,
            String role,
            String email,
            HostelAdmissionRequestDTO requestDTO,
            MultipartFile authorizedSignature
    );


    Page<HostelAdmissionResponseDTO> filterAdmissions(
            String role,
            String email,
            DateRange dateRange,
            LocalDate startDate,
            LocalDate endDate,
            String userNo,
            RoomType roomType,
            BedType bedType,
            String paymentMode,
            PaymentStatus paymentStatus,
            Pageable pageable);


    void deleteAdmission(
            Long id,
            String role,
            String email);

}