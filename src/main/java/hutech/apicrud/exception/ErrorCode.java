package hutech.apicrud.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    INVALID_UNCATEGORIZED(9999, "Invalid Uncategorized Value", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY_ENUM(9998, "Invalid Key Enum", HttpStatus.BAD_REQUEST),
    USER_EXISTS(1001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1004, "User not exists", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1002, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission", HttpStatus.FORBIDDEN),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
