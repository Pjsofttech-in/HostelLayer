package NewHostelLayer.HostelLayer.Controller;

import NewHostelLayer.HostelLayer.DTO.BranchAddressDTO;
import NewHostelLayer.HostelLayer.DTO.InstituteLoginResponse;
import NewHostelLayer.HostelLayer.Security.LoginRequest;
import NewHostelLayer.HostelLayer.Security.LoginResponse;
import NewHostelLayer.HostelLayer.ServiceImpl.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class StaffController
{

    @Autowired
    private StaffService staffLoginService;

    @PostMapping("/stafflogin")
    public Mono<ResponseEntity<LoginResponse>> loginStaff(@RequestBody LoginRequest request) {
        return staffLoginService.loginStaff(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    LoginResponse errorResponse = new LoginResponse();
                    errorResponse.setToken(null);
                    errorResponse.setData(Map.of("error", "Login failed: " + e.getMessage()));
                    return Mono.just(ResponseEntity.status(500).body(errorResponse));
                });
    }

    @PostMapping("/departmentlogin")
    public Mono<ResponseEntity<LoginResponse>> departmentLogin(
            @RequestBody LoginRequest request) {

        return staffLoginService.departmentLogin(request)
                .map(ResponseEntity::ok)
                .onErrorResume(ex ->
                        Mono.just(
                                ResponseEntity
                                        .status(HttpStatus.UNAUTHORIZED)
                                        .build()
                        )
                );
    }

    @GetMapping("/permissionForStaff")
    public Map<String, Boolean> getPermission(@RequestParam String staffEmail) {
        return staffLoginService.getPermissionsByEmail(staffEmail);
    }


    @GetMapping("/permissionForDepartment")
    public ResponseEntity<Map<String, Object>> getDepartmentPermissions(@RequestParam String departmentEmail) {

        Map<String, Object> permissions = staffLoginService.getCrudPermissionForDepartmentByEmail(departmentEmail);
        return ResponseEntity.ok(permissions);
    }
    @GetMapping("/getBranchCodeByInstituteEmail")
    public ResponseEntity<Map<String, String>> getBranchCodesByInstituteEmail(@RequestParam String instituteEmail)
    {
        Map<String, String> branchMap = staffLoginService.getBranchCodesWithNameByInstituteEmail(instituteEmail);
        return ResponseEntity.ok(branchMap);
    }


    @GetMapping("/getInstituteDetails")
    public ResponseEntity<List<InstituteLoginResponse>> getInstitute(@RequestParam String instituteEmail) {
        List<InstituteLoginResponse> response = staffLoginService.getInstituteDetailsOnly(instituteEmail);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getBranchAddress")
    public ResponseEntity<BranchAddressDTO> getBranchAddress(@RequestParam String branchCode) {
        try {
            BranchAddressDTO branchDetails = staffLoginService.getBranchAddressDetails(branchCode);
            return ResponseEntity.ok(branchDetails);
        } catch (Exception e) {
            // Log the error and return a friendly message
            System.err.println("Error fetching branch details: " + e.getMessage());
            return ResponseEntity
                    .status(500)
                    .body(null); // You can replace null with a custom error DTO if needed
        }
    }
}
