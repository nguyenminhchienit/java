package hutech.apicrud.controllers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import hutech.apicrud.dto.request.ApiResponse;
import hutech.apicrud.dto.request.AuthRequest;
import hutech.apicrud.dto.request.LogoutRequest;
import hutech.apicrud.dto.request.VerifyRequest;
import hutech.apicrud.dto.response.AuthResponse;
import hutech.apicrud.dto.response.VerifyResponse;
import hutech.apicrud.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> auth(@RequestBody AuthRequest authRequest) throws KeyLengthException {
        var result = authService.authenticate(authRequest);
        ApiResponse response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(result);
        return response;
    }

    @PostMapping("/verify")
    ApiResponse<VerifyResponse> auth(@RequestBody VerifyRequest request) throws JOSEException, ParseException {
        var result = authService.verifyToken(request);
        ApiResponse response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(result);
        return response;
    }

    @PostMapping("/logout")
    ApiResponse<Void> auth(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authService.logout(request);
        ApiResponse response = new ApiResponse<>();
        response.setSuccess(true);
        return response;
    }
}
