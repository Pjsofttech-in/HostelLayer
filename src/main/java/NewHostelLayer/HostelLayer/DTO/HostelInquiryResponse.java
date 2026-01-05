package NewHostelLayer.HostelLayer.DTO;

import NewHostelLayer.HostelLayer.Entity.HostelInquiry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelInquiryResponse
{
    private long totalInquiries;
    private Map<String, Long> statusWiseCount;
    private Page<HostelInquiry> inquiries;
}
