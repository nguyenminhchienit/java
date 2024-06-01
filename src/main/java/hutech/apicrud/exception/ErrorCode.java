package hutech.apicrud.exception;


public enum ErrorCode {
    INVALID_UNCATEGORIZED(9999, "Invalid Uncategorized Value"),
    INVALID_KEY_ENUM(9998, "Invalid Key Enum"),
    USER_EXISTS(1001, "User already exists"),
    USER_NOT_EXISTS(1004, "User not exists"),
    USERNAME_INVALID(1002, "Username must be at least 3 characters"),
    PASSWORD_INVALID(1003, "Password must be at least 6 characters"),
    UNAUTHENTICATED(1005, "Unauthenticated"),
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
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
