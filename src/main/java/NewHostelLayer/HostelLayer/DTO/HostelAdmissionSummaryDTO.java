package NewHostelLayer.HostelLayer.DTO;

import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class HostelAdmissionSummaryDTO {

        private Long totalAdmissions;

        private Double total;

        private Double totalWithGst;

        private Double depositWithGst;

        private Double pending;

        private Double cancel;

        private Double refund;
    }

