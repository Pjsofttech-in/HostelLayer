package NewHostelLayer.HostelLayer.Pagination;

import NewHostelLayer.HostelLayer.Entity.HostelInquiry;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InquirySpecification
{
    public static Specification<HostelInquiry> withFilters(
            String branchCode,
            String roomType,
            String bedType,
            String district,
            String state,
            String currentStatus,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("branchCode"), branchCode));

            if (roomType != null)
                predicates.add(cb.equal(root.get("roomType"), roomType));

            if (bedType != null)
                predicates.add(cb.equal(root.get("bedType"), bedType));

            if (district != null)
                predicates.add(cb.equal(root.get("district"), district));

            if (state != null)
                predicates.add(cb.equal(root.get("state"), state));

            if (currentStatus != null)
                predicates.add(cb.equal(root.get("currentStatus"), currentStatus));

            if (startDate != null && endDate != null)
                predicates.add(cb.between(root.get("inquiryDate"), startDate, endDate));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
