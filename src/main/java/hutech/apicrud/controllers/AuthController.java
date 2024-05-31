package hutech.apicrud.controllers;

import hutech.apicrud.dto.request.ApiResponse;
import hutech.apicrud.dto.request.AuthRequest;
import hutech.apicrud.dto.response.AuthResponse;
import hutech.apicrud.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> auth(@RequestBody AuthRequest authRequest) {
        boolean result = authService.authenticate(authRequest);
        ApiResponse response = new ApiResponse<>();
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticated(result);
        response.setData(authResponse);
        return response;
    }
}
