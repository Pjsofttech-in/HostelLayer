package NewHostelLayer.HostelLayer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostelRegistrationFilterRequest
{
    private String bedType;
    private String roomType;
    private String paymentMode;
    private String paymentStatus;
    private String searchNumber;
}

