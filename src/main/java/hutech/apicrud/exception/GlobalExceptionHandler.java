package hutech.apicrud.exception;

import hutech.apicrud.dto.request.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> exceptionHandler(Exception e) {
        ApiResponse response = new ApiResponse();
        response.setMessage(ErrorCode.INVALID_UNCATEGORIZED.getMessage());
        response.setCode(ErrorCode.INVALID_UNCATEGORIZED.getCode());
        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse response = new ApiResponse();
        response.setMessage(errorCode.getMessage());
        response.setCode(errorCode.getCode());
        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ApiResponse response = new ApiResponse();
        String enumKey = e.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY_ENUM;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        }catch (IllegalArgumentException exception){
            log.info(exception.getMessage());
        }


        response.setMessage(errorCode.getMessage());
        response.setCode(errorCode.getCode());
        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }
}
