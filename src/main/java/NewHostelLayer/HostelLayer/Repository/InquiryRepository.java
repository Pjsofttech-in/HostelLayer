package NewHostelLayer.HostelLayer.Repository;

import NewHostelLayer.HostelLayer.Entity.HostelInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<HostelInquiry,Long>, JpaSpecificationExecutor<HostelInquiry>
{
    @Query("SELECT h FROM HostelInquiry h WHERE h.branchCode = :branchCode")
    List<HostelInquiry> findAllByBranchCode(@Param("branchCode") String branchCode);

    @Query("SELECT COUNT(h) FROM HostelInquiry h WHERE h.inquiryDate = :today AND h.branchCode = :branchCode")
    Long countToday(LocalDate today, String branchCode);

    @Query(" SELECT COUNT(h) FROM HostelInquiry h WHERE h.branchCode = :branchCode AND h.inquiryDate BETWEEN :startDate AND :endDate")
    Long countBetweenDates(@Param("branchCode") String branchCode, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(h) FROM HostelInquiry h WHERE h.branchCode = :branchCode")
    Long countTotal(String branchCode);

    @Query("""
        SELECT h.inquiryDate, COUNT(h)
        FROM HostelInquiry h
        WHERE h.branchCode = :branchCode
          AND YEAR(h.inquiryDate) = :year
          AND MONTH(h.inquiryDate) = :month
        GROUP BY h.inquiryDate
        ORDER BY h.inquiryDate
    """)
    List<Object[]> countByMonthYear(
            @Param("branchCode") String branchCode,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT MONTH(h.inquiryDate), COUNT(h)
        FROM HostelInquiry h
        WHERE h.branchCode = :branchCode
          AND YEAR(h.inquiryDate) = :year
        GROUP BY MONTH(h.inquiryDate)
        ORDER BY MONTH(h.inquiryDate)
    """)
    List<Object[]> countMonthWiseByYear(
            @Param("branchCode") String branchCode,
            @Param("year") int year
    );

    @Query("""
        SELECT h.source, COUNT(h)
        FROM HostelInquiry h
        WHERE h.branchCode = :branchCode
        GROUP BY h.source
        ORDER BY COUNT(h) DESC
    """)
    List<Object[]> countBySource(
            @Param("branchCode") String branchCode
    );
}
