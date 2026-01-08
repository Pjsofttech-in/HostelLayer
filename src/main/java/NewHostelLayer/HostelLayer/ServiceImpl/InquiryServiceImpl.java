package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.HostelInquiryFilterRequest;
import NewHostelLayer.HostelLayer.DTO.HostelInquiryResponse;
import NewHostelLayer.HostelLayer.DTO.InquiryToRegistrationDTO;
import NewHostelLayer.HostelLayer.Entity.HostelInquiry;
import NewHostelLayer.HostelLayer.Pagination.InquirySpecification;
import NewHostelLayer.HostelLayer.Repository.InquiryRepository;
import NewHostelLayer.HostelLayer.Service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InquiryServiceImpl implements InquiryService
{
    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private StaffService staffService;


    @Override
    public HostelInquiry createInquiry(String role, String email, HostelInquiry inquiry) {

        if (!staffService.hasPermission(role, email, "Post")) {
            throw new RuntimeException("You don't have permission to create Inquiry");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        inquiry.setBranchCode(branchCode);
        inquiry.setCreatedByEmail(email);
        inquiry.setRole(role);

        return inquiryRepository.save(inquiry);
    }

    @Override
    public HostelInquiry getInquiryById(Long id, String role, String email) {

        if (!staffService.hasPermission(role, email, "Get")) {
            throw new RuntimeException("You don't have permission to view Inquiry");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        return inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
    }


        @Override
        public HostelInquiryResponse getAllInquiries(String role, String email, HostelInquiryFilterRequest filter,
                                                     String timeFrame, LocalDate customStartDate, LocalDate customEndDate, int page, int size)
        {
            if (!staffService.hasPermission(role, email, "Get")) {
                throw new RuntimeException("You don't have permission to view Inquiries");
            }

            String branchCode = staffService.fetchBranchCodeByRole(role, email);

            LocalDate startDate = null;
            LocalDate endDate = null;
            LocalDate today = LocalDate.now();

            if (timeFrame != null) {
                switch (timeFrame.toLowerCase()) {

                    case "today" -> {
                        startDate = today;
                        endDate = today;
                    }

                    case "7days" -> {
                        startDate = today.minusDays(6);
                        endDate = today;
                    }

                    case "30days" -> {
                        startDate = today.minusDays(29);
                        endDate = today;
                    }

                    case "365days" -> {
                        startDate = today.minusDays(364);
                        endDate = today;
                    }

                    case "custom" -> {
                        startDate = customStartDate;
                        endDate = customEndDate;
                    }
                }
            }

            Specification<HostelInquiry> specification =
                    InquirySpecification.withFilters(
                            branchCode,
                            filter.getRoomType(),
                            filter.getBedType(),
                            filter.getDistrict(),
                            filter.getState(),
                            filter.getCurrentStatus(),
                            startDate,
                            endDate
                    );
            Pageable pageable =
                    PageRequest.of(page, size, Sort.by("inquiryDate").descending());

            Page<HostelInquiry> inquiryPage =
                    inquiryRepository.findAll(specification, pageable);


            long totalInquiries =
                    inquiryRepository.count(specification);

            Map<String, Long> statusWiseCount = new HashMap<>();

            List<Object[]> statusCountResult =
                    inquiryRepository.countByStatus(
                            branchCode,
                            startDate,
                            endDate,
                            filter.getRoomType(),
                            filter.getBedType(),
                            filter.getDistrict(),
                            filter.getState()
                    );

            for (Object[] row : statusCountResult) {
                String status = (String) row[0];
                Long count = (Long) row[1];
                statusWiseCount.put(status, count);
            }

            return new HostelInquiryResponse(
                    totalInquiries,
                    statusWiseCount,
                    inquiryPage
            );
        }

    @Override
    public HostelInquiry updateInquiry(Long id, String role, String email, HostelInquiry updatedInquiry) {

        if (!staffService.hasPermission(role, email, "Put")) {
            throw new RuntimeException("You don't have permission to update Inquiry");
        }

        HostelInquiry existing = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        if (updatedInquiry.getStudentName() != null)
            existing.setStudentName(updatedInquiry.getStudentName());

        if (updatedInquiry.getFatherName() != null)
            existing.setFatherName(updatedInquiry.getFatherName());

        if (updatedInquiry.getFatherMobileNo() != null)
            existing.setFatherMobileNo(updatedInquiry.getFatherMobileNo());

        if (updatedInquiry.getStudentMobileNo() != null)
            existing.setStudentMobileNo(updatedInquiry.getStudentMobileNo());

        if (updatedInquiry.getEmail() != null)
            existing.setEmail(updatedInquiry.getEmail());

        if (updatedInquiry.getAddress() != null)
            existing.setAddress(updatedInquiry.getAddress());

        if (updatedInquiry.getGender() != null)
            existing.setGender(updatedInquiry.getGender());

        if (updatedInquiry.getCity() != null)
            existing.setCity(updatedInquiry.getCity());

        if (updatedInquiry.getTaluka() != null)
            existing.setTaluka(updatedInquiry.getTaluka());

        if (updatedInquiry.getDistrict() != null)
            existing.setDistrict(updatedInquiry.getDistrict());

        if (updatedInquiry.getState() != null)
            existing.setState(updatedInquiry.getState());

        if (updatedInquiry.getPincode() != null)
            existing.setPincode(updatedInquiry.getPincode());

        if (updatedInquiry.getNumberOfMembers() != null)
            existing.setNumberOfMembers(updatedInquiry.getNumberOfMembers());

        if (updatedInquiry.getCourseOfStudy() != null)
            existing.setCourseOfStudy(updatedInquiry.getCourseOfStudy());

        if (updatedInquiry.getInquiryDate() != null)
            existing.setInquiryDate(updatedInquiry.getInquiryDate());

        if (updatedInquiry.getCurrentStatus() != null)
            existing.setCurrentStatus(updatedInquiry.getCurrentStatus());

        if (updatedInquiry.getRoomType() != null)
            existing.setRoomType(updatedInquiry.getRoomType());

        if (updatedInquiry.getBedType() != null)
            existing.setBedType(updatedInquiry.getBedType());

        if (updatedInquiry.getReceptionName() != null)
            existing.setReceptionName(updatedInquiry.getReceptionName());

        if (updatedInquiry.getSource() != null)
            existing.setSource(updatedInquiry.getSource());

        if (updatedInquiry.getConductedBy() != null)
            existing.setConductedBy(updatedInquiry.getConductedBy());

        if (updatedInquiry.getMonthName() != null)
            existing.setMonthName(updatedInquiry.getMonthName());

        if (updatedInquiry.getYear() != null)
            existing.setYear(updatedInquiry.getYear());

        return inquiryRepository.save(existing);
    }

    @Override
    public void deleteInquiry(Long id, String role, String email) {

        if (!staffService.hasPermission(role, email, "Delete")) {
            throw new RuntimeException("You don't have permission to delete Inquiry");
        }

        inquiryRepository.deleteById(id);
    }


    @Override
    public Map<String,Long> getInquiryCount(String role, String email)
    {
        if(!staffService.hasPermission(role,email,"Get"))
        {
            throw new RuntimeException("You don't have permission to get data");
        }
        String branchCode = staffService.fetchBranchCodeByRole(role, email);
        LocalDate today = LocalDate.now();

        Map<String, Long> response = new HashMap<>();

        response.put("today", inquiryRepository.countBetweenDates(branchCode,today,today));

        response.put("last7Days",
                inquiryRepository.countBetweenDates(branchCode, today.minusDays(6), today));

            response.put("last30Days",
                    inquiryRepository.countBetweenDates(branchCode, today.minusDays(29), today));

            response.put("last365Days",
                    inquiryRepository.countBetweenDates(branchCode, today.minusDays(364), today));

            response.put("total", inquiryRepository.countTotal(branchCode));

            return response;
        }


    @Override
    public List<Map<String, Object>> getInquiryCountByMonth(String role, String email, int month, int year)
    {
        if(!staffService.hasPermission(role,email,"Get"))
        {
            throw  new RuntimeException("You don't have Permission to get Data");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        List<Object[]> results =
                inquiryRepository.countByMonthYear(branchCode, month, year);

        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("inquiryCount", row[1]);
            response.add(map);
        }
        return response;
    }


    @Override
    public List<Map<String, Object>> getInquiryCountByYear(String role, String email, int year)
    {

        if(!staffService.hasPermission(role,email,"Get"))
        {
            throw new RuntimeException("You don't have permission to get Data");
        }

        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        List<Object[]> dbResult = inquiryRepository.countMonthWiseByYear(branchCode, year);

        Map<Integer, Long> monthCountMap = new HashMap<>();
        for (Object[] row : dbResult)
        {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            monthCountMap.put(month, count);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Month month : Month.values())
        {
            Map<String, Object> map = new HashMap<>();
            map.put("month", month.name().substring(0, 3));
            map.put("inquiryCount", monthCountMap.getOrDefault(month.getValue(), 0L));
            response.add(map);
        }

        return response;
    }

    @Override
    public InquiryToRegistrationDTO getInquiryForRegistration(String role, String email,Long inquiryId)
    {
        if(!staffService.hasPermission(role,email,"GET"))
        {
            throw new RuntimeException("You Don't have permission to Get Data");
        }

        HostelInquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        InquiryToRegistrationDTO dto = new InquiryToRegistrationDTO();

        dto.setInquiryId(inquiryId);
        dto.setFullName(inquiry.getStudentName());
        dto.setGender(inquiry.getGender());
        dto.setEmail(inquiry.getEmail());
        dto.setParentsMobNo(inquiry.getFatherMobileNo());
        dto.setPermanentAddress(inquiry.getAddress());
        dto.setCity(inquiry.getCity());
        dto.setState(inquiry.getState());
        dto.setPincode(inquiry.getPincode());
        dto.setCourseEnrolled(inquiry.getCourseOfStudy());
        dto.setRoomType(inquiry.getRoomType());
        dto.setBedType(inquiry.getBedType());
        dto.setCreatedByEmail(inquiry.getCreatedByEmail());
        dto.setBranchCode(inquiry.getBranchCode());
        dto.setRole(inquiry.getRole());

        return dto;
    }



    @Override
    public List<Map<String, Object>> getInquiryCountBySource(String role, String email)
    {
        if(!staffService.hasPermission(role,email,"Get"))
        {
            throw new RuntimeException("You Don't have Permission to get data");
        }
        String branchCode = staffService.fetchBranchCodeByRole(role, email);

        List<Object[]> results = inquiryRepository.countBySource(branchCode);

        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("source", row[0] != null ? row[0] : "UNKNOWN");
            map.put("inquiryCount", row[1]);
            response.add(map);
        }

        return response;
    }
}
