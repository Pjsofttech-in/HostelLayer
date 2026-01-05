package NewHostelLayer.HostelLayer.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelRegistrationPageResponse
{
    private Page<HostelRegistrationResponseDTO> registrations;
    private HostelAmountSummaryDTO amountSummary;
}
