package hutech.apicrud.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hutech.apicrud.dto.request.ApiResponse;
import hutech.apicrud.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> Apiresponse = new ApiResponse<>();
        Apiresponse.setMessage(errorCode.getMessage());
        Apiresponse.setCode(errorCode.getCode());
        Apiresponse.setSuccess(false);

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(Apiresponse));
        response.flushBuffer();
    }
}
