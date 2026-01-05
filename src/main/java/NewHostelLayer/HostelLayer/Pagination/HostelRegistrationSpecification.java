package NewHostelLayer.HostelLayer.Pagination;

import NewHostelLayer.HostelLayer.DTO.HostelRegistrationFilterRequest;
import NewHostelLayer.HostelLayer.Entity.HostelRegistrationForm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HostelRegistrationSpecification
{

    public static Specification<HostelRegistrationForm> filterBy(
            String branchCode,
            HostelRegistrationFilterRequest filter,
            LocalDate startDate,
            LocalDate endDate
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // mandatory branch filter
            predicates.add(cb.equal(root.get("branchCode"), branchCode));

            if (filter != null) {

                if (filter.getBedType() != null) {
                    predicates.add(cb.equal(root.get("bedType"), filter.getBedType()));
                }

                if (filter.getRoomType() != null) {
                    predicates.add(cb.equal(root.get("roomType"), filter.getRoomType()));
                }

                if (filter.getPaymentMode() != null) {
                    predicates.add(cb.equal(root.get("paymentMode"), filter.getPaymentMode()));
                }

                if (filter.getPaymentStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), filter.getPaymentStatus()));
                }

                if (filter.getSearchNumber() != null) {
                    predicates.add(
                            cb.like(
                                    root.get("parentsMobNo"),
                                    "%" + filter.getSearchNumber() + "%"
                            )
                    );
                }
            }

            // ✅ DATE RANGE FILTER USING admissionDate
            if (startDate != null && endDate != null) {
                predicates.add(
                        cb.between(
                                root.get("admissionDate"),
                                startDate,
                                endDate
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
