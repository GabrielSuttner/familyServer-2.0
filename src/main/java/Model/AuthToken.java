package Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class AuthToken {
    @SerializedName("Token_ID")
    private String tokenID;
    @SerializedName("Username")
    private String userName;
    @SerializedName("Token")
    private String token;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return tokenID.equals(authToken.tokenID) &&
                userName.equals(authToken.userName) &&
                token.equals(authToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenID, userName, token);
    }



    /**
     *
     * @param userName
     */
    public AuthToken(String userName) {
        this.tokenID = UUID.randomUUID().toString();
        this.token = randomString();
        this.userName = userName;
    }

    /**
     *
     * @param tokenID
     * @param name
     * @param token
     */
    public AuthToken(String tokenID, String name, String token) {
        this.tokenID = tokenID;
        this.userName = name;
        this.token = token;
    }

    /**
     *
     * @return username
     */
    public String getUserName() {
        return  this.userName;
    }

    /**
     *
     * @return token
     */
    public String getToken() {
        return this.token;
    }

    public String getTokenID() {return this.tokenID;}
    /**
     *
     * @return a random string
     */
    private String randomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    /**
     *
     * @param s
     * @return
     */
    public boolean validateToken(String s) {
        if(this.token == s) {
            return true;
        }
        return false;
    }
}
