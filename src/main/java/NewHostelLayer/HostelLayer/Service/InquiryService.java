package NewHostelLayer.HostelLayer.Service;

import NewHostelLayer.HostelLayer.DTO.HostelInquiryFilterRequest;
import NewHostelLayer.HostelLayer.DTO.HostelInquiryResponse;
import NewHostelLayer.HostelLayer.DTO.InquiryToRegistrationDTO;
import NewHostelLayer.HostelLayer.Entity.HostelInquiry;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface InquiryService
{
    HostelInquiry createInquiry(String role, String email, HostelInquiry inquiry);

    HostelInquiry getInquiryById(Long id, String role, String email);

    HostelInquiryResponse getAllInquiries(String role, String email, HostelInquiryFilterRequest filter,
                                          String timeFrame, LocalDate customStartDate, LocalDate customEndDate, int page, int size);

    HostelInquiry updateInquiry(Long id, String role, String email, HostelInquiry inquiry);

    void deleteInquiry(Long id, String role, String email);

    Map<String,Long> getInquiryCount(String role, String email);

    List<Map<String, Object>> getInquiryCountByMonth(String role, String email, int month, int year);

    List<Map<String, Object>> getInquiryCountByYear(String role, String email, int year);

    List<Map<String, Object>> getInquiryCountBySource(String role, String email);

    InquiryToRegistrationDTO getInquiryForRegistration(String role, String email, Long inquiryId);
}
