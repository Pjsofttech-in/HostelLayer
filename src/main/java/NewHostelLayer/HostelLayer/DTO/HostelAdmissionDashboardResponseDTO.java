package NewHostelLayer.HostelLayer.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostelAdmissionDashboardResponseDTO {

    private DashboardMetricDTO today;

    private DashboardMetricDTO last7Days;

    private DashboardMetricDTO last30Days;

    private DashboardMetricDTO last365Days;

    private DashboardMetricDTO total;
}
