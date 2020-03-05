package Response;

public class ClearResponse {
    private String message = "Clear Succeeded";
    private boolean success = true;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {this.message = message;}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean b) {
        success = b;
    }
}
