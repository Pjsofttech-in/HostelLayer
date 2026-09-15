package NewHostelLayer.HostelLayer.Pagination;


import NewHostelLayer.HostelLayer.Entity.HostelAdmissionForm;
import NewHostelLayer.HostelLayer.Enum.BedType;
import NewHostelLayer.HostelLayer.Enum.PaymentStatus;
import NewHostelLayer.HostelLayer.Enum.RoomType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class HostelAdmissionSpecification {


    public static Specification<HostelAdmissionForm> hasUserNo(
            String userNo) {

        return (root, query, criteriaBuilder) -> {

            if (userNo == null || userNo.trim().isEmpty()) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("userNo")),
                    "%" + userNo.toLowerCase() + "%"
            );
        };
    }


    public static Specification<HostelAdmissionForm> hasRoomType(
            RoomType roomType) {

        return (root, query, criteriaBuilder) -> {

            if (roomType == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("roomType"),
                    roomType
            );
        };
    }


    public static Specification<HostelAdmissionForm> hasBedType(
            BedType bedType) {

        return (root, query, criteriaBuilder) -> {

            if (bedType == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("bedType"),
                    bedType
            );
        };
    }


    public static Specification<HostelAdmissionForm> hasPaymentMode(
            String paymentMode) {

        return (root, query, criteriaBuilder) -> {

            if (paymentMode == null || paymentMode.trim().isEmpty()) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("paymentMode"),
                    paymentMode
            );
        };
    }


    public static Specification<HostelAdmissionForm> hasPaymentStatus(
            PaymentStatus paymentStatus) {

        return (root, query, criteriaBuilder) -> {

            if (paymentStatus == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("paymentStatus"),
                    paymentStatus
            );
        };
    }


    public static Specification<HostelAdmissionForm> dateBetween(
            LocalDate startDate,
            LocalDate endDate) {

        return (root, query, criteriaBuilder) -> {

            if (startDate == null || endDate == null) {
                return null;
            }

            return criteriaBuilder.between(
                    root.get("admissionDate"),
                    startDate,
                    endDate
            );
        };
    }
}