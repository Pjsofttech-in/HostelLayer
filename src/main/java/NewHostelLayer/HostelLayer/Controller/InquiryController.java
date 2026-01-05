package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.HostelInquiryFilterRequest;
import NewHostelLayer.HostelLayer.DTO.HostelInquiryResponse;
import NewHostelLayer.HostelLayer.Entity.HostelInquiry;
import NewHostelLayer.HostelLayer.Service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class InquiryController
{

    @Autowired
    InquiryService inquiryService;

    @PostMapping("/createInquiry")
    public ResponseEntity<HostelInquiry> createInquiry(@RequestBody HostelInquiry inquiry, @RequestParam String role, @RequestParam String email) {

        return ResponseEntity.ok(
                inquiryService.createInquiry(role, email, inquiry)
        );
    }

    @GetMapping("/getInquiryById/{id}")
    public ResponseEntity<HostelInquiry> getInquiryById(@PathVariable Long id, @RequestParam String role, @RequestParam String email) {

        return ResponseEntity.ok(inquiryService.getInquiryById(id, role, email));
    }

    @PostMapping("/getAllHostelInquiry")
    public ResponseEntity<HostelInquiryResponse> getAllInquiries(
            @RequestParam String role,
            @RequestParam String email,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String timeFrame,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody HostelInquiryFilterRequest filter
    ) {
        HostelInquiryResponse inquiryResponse = inquiryService.getAllInquiries(role, email, filter,timeFrame,startDate,endDate, page, size);

        return ResponseEntity.ok(inquiryResponse);
    }


    @PutMapping("/updateInquiry/{id}")
    public ResponseEntity<HostelInquiry> updateInquiry(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email,
            @RequestBody HostelInquiry inquiry) {

        return ResponseEntity.ok(
                inquiryService.updateInquiry(id, role, email, inquiry)
        );
    }

    @DeleteMapping("/deleteInquiry/{id}")
    public ResponseEntity<String> deleteInquiry(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email) {

        inquiryService.deleteInquiry(id, role, email);
        return ResponseEntity.ok("Inquiry deleted successfully");
    }

    @GetMapping("/getInquiryCountForCards")
    public ResponseEntity<Map<String, Long>> getInquiryCounts(
            @RequestParam String role, @RequestParam String email)
    {
        return ResponseEntity.ok(inquiryService.getInquiryCount(role, email));
    }

    @GetMapping("/getInquiryCountByMonthAndYear")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyCounts(
            @RequestParam String role,
            @RequestParam String email,
            @RequestParam int month,
            @RequestParam int year)
    {
        return ResponseEntity.ok(
                inquiryService.getInquiryCountByMonth(role, email, month, year)
        );
    }

    @GetMapping("/getInquiryCountByYear")
    public ResponseEntity<List<Map<String, Object>>> getYearlyCounts(
            @RequestParam String role,
            @RequestParam String email,
            @RequestParam int year)
    {
        return ResponseEntity.ok(inquiryService.getInquiryCountByYear(role,email, year));
    }

    @GetMapping("/getInquiryCountBySource")
    public ResponseEntity<List<Map<String, Object>>> getInquiryCountBySources(@RequestParam String role,
                                                                              @RequestParam String email)
    {
        return ResponseEntity.ok(inquiryService.getInquiryCountBySource(role,email));
    }
}
