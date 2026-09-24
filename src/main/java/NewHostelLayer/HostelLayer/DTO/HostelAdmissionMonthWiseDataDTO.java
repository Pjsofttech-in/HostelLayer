package NewHostelLayer.HostelLayer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostelAdmissionMonthWiseDataDTO {

    private String month;

    private Long admissions;

    private Double revenue;
}
