package net.frebib.servlet;

public class LoginError {
    public static final String ERR_OBJ_ID = "login-error";

    private String message;
    private String title;

    public LoginError(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "LoginError{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
