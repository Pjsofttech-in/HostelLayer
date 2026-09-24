package NewHostelLayer.HostelLayer.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostelAdmissionDateWiseDataDTO {

    private LocalDate date;

    private Long admissions;

    private Double revenue;
}
