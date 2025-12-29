package NewHostelLayer.HostelLayer.ServiceImpl;

import NewHostelLayer.HostelLayer.DTO.BranchAddressDTO;
import NewHostelLayer.HostelLayer.DTO.InstituteClientWrapperResponse;
import NewHostelLayer.HostelLayer.DTO.InstituteLoginResponse;
import NewHostelLayer.HostelLayer.Security.LoginRequest;
import NewHostelLayer.HostelLayer.Security.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class StaffService
{
    @Autowired
    WebClient webClient;

    @Autowired
    public StaffService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<LoginResponse> loginStaff(LoginRequest request) {
        return webClient.post()
                .uri("/stafflogin")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Login Failed: " + error)))
                )
                .bodyToMono(LoginResponse.class);
    }

    public Mono<LoginResponse> departmentLogin(LoginRequest request) {

        return webClient
                .post()
                .uri("/departmentlogin")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        res -> Mono.error(new RuntimeException("Invalid department credentials"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        res -> Mono.error(new RuntimeException("Client service unavailable"))
                )
                .bodyToMono(LoginResponse.class);
    }

    public Map<String, Boolean> getPermissionsByEmail(String email) {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/permissionForStaff")
                        .queryParam("staffEmail", email)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, token)  // pass it as-is
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Boolean>>() {})
                .block();
    }

    public Map<String, Object> getCrudPermissionForDepartmentByEmail(String email) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/permissionForDepartment")
                        .queryParam("email", email)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, token)  // Pass token directly
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

    }


    public List<String> getBranchCodesByInstituteEmail(String instituteEmail) {
        Map<String, String> branchMap = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getBranchCodesByinstituteEmail")
                        .queryParam("instituteEmail", instituteEmail)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (branchMap == null || branchMap.isEmpty()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(branchMap.values());
    }


    public boolean hasPermission(String role, String email, String action) {
        if (role == null || action == null) {
            return false;
        }

        switch (role.toUpperCase()) {
            case "SUPERADMIN" -> {
                if ("GET".equalsIgnoreCase(action)) {
                    try {
                        Boolean exists = webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/checkClientEmailExist")
                                        .queryParam("email", email)
                                        .build())
                                .retrieve()
                                .bodyToMono(Boolean.class)
                                .block();
                        return Boolean.TRUE.equals(exists);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return false; // SUPERADMIN only has GET
            }

            case "USER" -> {
                return "GET".equalsIgnoreCase(action) || "POST".equalsIgnoreCase(action);
            }

            case "BRANCH" -> {
                try {
                    Boolean exists = webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/existBranchbyemail")
                                    .queryParam("email", email)
                                    .build())
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .block();
                    return Boolean.TRUE.equals(exists);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            case "STAFF" -> {
                Map<String, Boolean> perms = getPermissionsByEmail(email);
                return switch (action.toUpperCase()) {
                    case "GET" -> Boolean.TRUE.equals(perms.get("cansGet"));
                    case "POST" -> Boolean.TRUE.equals(perms.get("cansPost"));
                    case "PUT" -> Boolean.TRUE.equals(perms.get("cansPut"));
                    case "DELETE" -> Boolean.TRUE.equals(perms.get("cansDelete"));
                    default -> false;
                };
            }

            case "DEPARTMENT" -> {
                Map<String, Object> perms = getCrudPermissionForDepartmentByEmail(email);
                return switch (action.toUpperCase()) {
                    case "GET" -> Boolean.TRUE.equals(perms.get("candGet"));
                    case "POST" -> Boolean.TRUE.equals(perms.get("candPost"));
                    case "PUT" -> Boolean.TRUE.equals(perms.get("candPut"));
                    case "DELETE" -> Boolean.TRUE.equals(perms.get("candDelete"));
                    default -> false;
                };
            }

            default -> {
                // Unknown role
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        }
    }


    public String fetchBranchCodeByRole(String role, String email) {
        String endpoint = switch (role.toLowerCase()) {
            case "branch" -> "/branch/getbranchcode";
            case "department" -> "/department/getbranchcode";
            case "staff" -> "/staff/getbranchcode";
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpoint)
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public List<InstituteLoginResponse> getInstituteDetailsOnly(String email) {
        InstituteClientWrapperResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getLayerClientByClientEmail")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(InstituteClientWrapperResponse.class)
                .block();

        return response != null ? response.getInstituteResponseDTOS() : Collections.emptyList();
    }



    public Mono<String> getInstituteEmailByBranchCode(String branchCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/instituteEmailByBranchCode")
                        .queryParam("branchCode", branchCode)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Map<String, String> getBranchCodesWithNameByInstituteEmail(String instituteEmail) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getBranchCodesByinstituteEmail")
                        .queryParam("instituteEmail", instituteEmail)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }

    public BranchAddressDTO getBranchAddressDetails(String branchCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/branchAddressDetailsByBranchCode")
                        .queryParam("branchCode", branchCode)
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    System.err.println("SuperAdmin error: " + error);
                                    return Mono.error(new RuntimeException("Failed to fetch branch details"));
                                })
                )
                .bodyToMono(BranchAddressDTO.class)
                .block(); // ✅ convert reactive response to blocking for MVC app
    }

}
