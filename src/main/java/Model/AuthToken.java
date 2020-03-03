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

    /**
     * @param userName
     */
    public AuthToken(String userName) {
        this.tokenID = UUID.randomUUID().toString();
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return tokenID.equals(authToken.tokenID) &&
                userName.equals(authToken.userName);
    }

    public boolean equals(String s) {
        if (s == this.tokenID) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenID, userName);
    }




    /**
     * @param tokenID
     * @param name
     */
    public AuthToken(String tokenID, String name) {
        this.tokenID = tokenID;
        this.userName = name;
    }

    /**
     * @return username
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @return token
     */

    public String getTokenID() {
        return this.tokenID;
    }

    /**
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
}
