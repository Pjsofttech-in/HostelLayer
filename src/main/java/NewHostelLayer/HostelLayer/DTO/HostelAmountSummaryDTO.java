package NewHostelLayer.HostelLayer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelAmountSummaryDTO
{
    private Double totalAmount;
    private Double depositAmount;
    private Double pendingAmount;
    private Double cancelAmount;
    private Double refundAmount;
}
