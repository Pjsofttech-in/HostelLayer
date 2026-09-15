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

    // Create Admission Form
    HostelAdmissionResponseDTO createAdmissionForm(
            HostelAdmissionRequestDTO requestDTO,
            MultipartFile authorizedSignature);

    // Get All Admission Forms
    List<HostelAdmissionResponseDTO> getAllAdmissions();

    // Get Admission Form By ID
    HostelAdmissionResponseDTO getAdmissionById(Long id);

    HostelAdmissionSummaryDTO getAdmissionSummary();

    // Update Admission Form
    HostelAdmissionResponseDTO updateAdmission(
            Long id,
            HostelAdmissionRequestDTO requestDTO,
            MultipartFile authorizedSignature);

    Page<HostelAdmissionResponseDTO> filterAdmissions(
            DateRange dateRange,
            LocalDate startDate,
            LocalDate endDate,
            String userNo,
            RoomType roomType,
            BedType bedType,
            String paymentMode,
            PaymentStatus paymentStatus,
            Pageable pageable
    );

    // Delete Admission Form
    void deleteAdmission(Long id);

}