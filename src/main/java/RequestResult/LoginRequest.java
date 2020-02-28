package RequestResult;

public class LoginRequest {
    private String userName;
    private String password;


    public LoginRequest(String u, String p) {
        this.userName = u;
        this.password = p;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
