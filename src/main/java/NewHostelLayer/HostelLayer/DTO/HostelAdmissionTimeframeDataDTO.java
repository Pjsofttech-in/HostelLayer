package NewHostelLayer.HostelLayer.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostelAdmissionTimeframeDataDTO {

    private String timeframe;

    private Long admissions;

    private Double revenue;
}
