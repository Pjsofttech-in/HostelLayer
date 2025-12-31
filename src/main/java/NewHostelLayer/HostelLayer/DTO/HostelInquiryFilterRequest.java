package NewHostelLayer.HostelLayer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelInquiryFilterRequest
{
    private String currentStatus;
    private String district;
    private String state;
    private String roomType;
    private String bedType;
}
