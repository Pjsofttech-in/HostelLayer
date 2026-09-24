package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.DashboardMetricDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDashboardResponseDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionDateWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionMonthWiseDataDTO;
import NewHostelLayer.HostelLayer.DTO.HostelAdmissionTimeframeDataDTO;
import NewHostelLayer.HostelLayer.Entity.HostelAdmissionForm;
import NewHostelLayer.HostelLayer.Enum.DashboardType;
import NewHostelLayer.HostelLayer.Enum.PaymentStatus;
import NewHostelLayer.HostelLayer.Repository.HostelAdmissionFormRepository;
import NewHostelLayer.HostelLayer.Service.HostelAdmissionDashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HostelAdmissionDashboardServiceImpl
        implements HostelAdmissionDashboardService {

    private final HostelAdmissionFormRepository admissionFormRepository;


    // =========================================================
    // DASHBOARD SUMMARY
    // =========================================================

    @Override
    public HostelAdmissionDashboardResponseDTO getDashboardSummary() {

        List<HostelAdmissionForm> admissions =
                admissionFormRepository.findAll();

        LocalDate today = LocalDate.now();

        // Today's data
        DashboardMetricDTO todayMetric =
                calculateMetric(
                        admissions,
                        today,
                        today
                );

        // Last 7 days including today
        DashboardMetricDTO last7DaysMetric =
                calculateMetric(
                        admissions,
                        today.minusDays(6),
                        today
                );

        // Last 30 days including today
        DashboardMetricDTO last30DaysMetric =
                calculateMetric(
                        admissions,
                        today.minusDays(29),
                        today
                );

        // Last 365 days including today
        DashboardMetricDTO last365DaysMetric =
                calculateMetric(
                        admissions,
                        today.minusDays(364),
                        today
                );

        // All-time data
        DashboardMetricDTO totalMetric =
                calculateTotalMetric(admissions);

        return new HostelAdmissionDashboardResponseDTO(
                todayMetric,
                last7DaysMetric,
                last30DaysMetric,
                last365DaysMetric,
                totalMetric
        );
    }


    // =========================================================
    // COMMON DATE RANGE CALCULATION
    // =========================================================

    /**
     * Calculates admission count and revenue
     * for the given date range.
     *
     * Admissions:
     * PAID + PENDING + CANCELLED + REFUNDED
     *
     * Revenue:
     * PAID only
     */
    private DashboardMetricDTO calculateMetric(
            List<HostelAdmissionForm> admissions,
            LocalDate startDate,
            LocalDate endDate) {

        Long admissionCount = 0L;
        Double revenue = 0.0;

        for (HostelAdmissionForm admission : admissions) {

            LocalDate admissionDate =
                    admission.getAdmissionDate();

            if (admissionDate == null) {
                continue;
            }

            // Check whether admission date
            // is inside requested range.
            if (admissionDate.isBefore(startDate)
                    || admissionDate.isAfter(endDate)) {
                continue;
            }

            PaymentStatus paymentStatus =
                    admission.getPaymentStatus();

            if (paymentStatus == null) {
                continue;
            }

            // Count valid admission statuses
            if (isValidAdmissionStatus(paymentStatus)) {
                admissionCount++;
            }

            // Revenue only from PAID admissions
            if (paymentStatus == PaymentStatus.PAID) {

                revenue += getRevenue(admission);
            }
        }

        return new DashboardMetricDTO(
                admissionCount,
                revenue
        );
    }


    // =========================================================
    // TOTAL CALCULATION
    // =========================================================

    /**
     * Calculates all-time admissions and revenue.
     */
    private DashboardMetricDTO calculateTotalMetric(
            List<HostelAdmissionForm> admissions) {

        Long admissionCount = 0L;
        Double revenue = 0.0;

        for (HostelAdmissionForm admission : admissions) {

            PaymentStatus paymentStatus =
                    admission.getPaymentStatus();

            if (paymentStatus == null) {
                continue;
            }

            // Count valid admission statuses
            if (isValidAdmissionStatus(paymentStatus)) {
                admissionCount++;
            }

            // Revenue only from PAID admissions
            if (paymentStatus == PaymentStatus.PAID) {

                revenue += getRevenue(admission);
            }
        }

        return new DashboardMetricDTO(
                admissionCount,
                revenue
        );
    }


    // =========================================================
    // DATE-WISE DATA
    // =========================================================

    @Override
    public List<HostelAdmissionDateWiseDataDTO> getDateWiseData(
            Integer month,
            Integer year) {

        List<HostelAdmissionForm> admissions =
                admissionFormRepository.findAll();

        List<HostelAdmissionDateWiseDataDTO> result =
                new ArrayList<>();

        YearMonth yearMonth =
                YearMonth.of(year, month);

        int numberOfDays =
                yearMonth.lengthOfMonth();

        for (int day = 1; day <= numberOfDays; day++) {

            LocalDate date =
                    LocalDate.of(year, month, day);

            Long admissionCount = 0L;
            Double revenue = 0.0;

            for (HostelAdmissionForm admission : admissions) {

                LocalDate admissionDate =
                        admission.getAdmissionDate();

                if (admissionDate == null) {
                    continue;
                }

                if (!admissionDate.equals(date)) {
                    continue;
                }

                PaymentStatus paymentStatus =
                        admission.getPaymentStatus();

                if (paymentStatus == null) {
                    continue;
                }

                // Count valid admission statuses
                if (isValidAdmissionStatus(paymentStatus)) {
                    admissionCount++;
                }

                // Revenue only from PAID admissions
                if (paymentStatus == PaymentStatus.PAID) {

                    revenue += getRevenue(admission);
                }
            }

            result.add(
                    new HostelAdmissionDateWiseDataDTO(
                            date,
                            admissionCount,
                            revenue
                    )
            );
        }

        return result;
    }


    // =========================================================
    // TIMEFRAME DATA
    // =========================================================

    @Override
    public List<HostelAdmissionTimeframeDataDTO> getTimeframeData() {

        List<HostelAdmissionForm> admissions =
                admissionFormRepository.findAll();

        LocalDate today = LocalDate.now();

        List<HostelAdmissionTimeframeDataDTO> result =
                new ArrayList<>();


        // TODAY
        DashboardMetricDTO todayMetric =
                calculateMetric(
                        admissions,
                        today,
                        today
                );

        result.add(
                new HostelAdmissionTimeframeDataDTO(
                        "TODAY",
                        todayMetric.getAdmissions(),
                        todayMetric.getRevenue()
                )
        );


        // LAST 7 DAYS
        DashboardMetricDTO last7DaysMetric =
                calculateMetric(
                        admissions,
                        today.minusDays(6),
                        today
                );

        result.add(
                new HostelAdmissionTimeframeDataDTO(
                        "LAST_7_DAYS",
                        last7DaysMetric.getAdmissions(),
                        last7DaysMetric.getRevenue()
                )
        );


        // LAST 30 DAYS
        DashboardMetricDTO last30DaysMetric =
                calculateMetric(
                        admissions,
                        today.minusDays(29),
                        today
                );

        result.add(
                new HostelAdmissionTimeframeDataDTO(
                        "LAST_30_DAYS",
                        last30DaysMetric.getAdmissions(),
                        last30DaysMetric.getRevenue()
                )
        );


        // LAST 365 DAYS
        DashboardMetricDTO last365DaysMetric =
                calculateMetric(
                        admissions,
                        today.minusDays(364),
                        today
                );

        result.add(
                new HostelAdmissionTimeframeDataDTO(
                        "LAST_365_DAYS",
                        last365DaysMetric.getAdmissions(),
                        last365DaysMetric.getRevenue()
                )
        );

        return result;
    }


    // =========================================================
    // MONTH-WISE DATA
    // =========================================================

    @Override
    public List<HostelAdmissionMonthWiseDataDTO> getMonthWiseData(
            Integer year){

        List<HostelAdmissionForm> admissions =
                admissionFormRepository.findAll();

        List<HostelAdmissionMonthWiseDataDTO> result =
                new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            Long admissionCount = 0L;
            Double revenue = 0.0;

            for (HostelAdmissionForm admission : admissions) {

                LocalDate admissionDate =
                        admission.getAdmissionDate();

                if (admissionDate == null) {
                    continue;
                }

                // Check requested year
                if (admissionDate.getYear() != year) {
                    continue;
                }

                // Check requested month
                if (admissionDate.getMonthValue() != month) {
                    continue;
                }

                PaymentStatus paymentStatus =
                        admission.getPaymentStatus();

                if (paymentStatus == null) {
                    continue;
                }

                // Count valid admission statuses
                if (isValidAdmissionStatus(paymentStatus)) {
                    admissionCount++;
                }

                // Revenue only from PAID admissions
                if (paymentStatus == PaymentStatus.PAID) {

                    revenue += getRevenue(admission);
                }
            }

            result.add(
                    new HostelAdmissionMonthWiseDataDTO(
                            Month.of(month).name(),
                            admissionCount,
                            revenue
                    )
            );
        }

        return result;
    }


    // =========================================================
    // COMMON HELPER METHODS
    // =========================================================

    /**
     * Checks whether the payment status
     * should be counted as an admission.
     */
    private boolean isValidAdmissionStatus(
            PaymentStatus paymentStatus) {

        return paymentStatus == PaymentStatus.PAID
                || paymentStatus == PaymentStatus.PENDING
                || paymentStatus == PaymentStatus.CANCELLED
                || paymentStatus == PaymentStatus.REFUNDED;
    }


    /**
     * Returns revenue amount for an admission.
     * Null amount is treated as 0.
     */
    private Double getRevenue(
            HostelAdmissionForm admission) {

        if (admission.getTotalAmount() == null) {
            return 0.0;
        }

        return admission.getTotalAmount();
    }
}
