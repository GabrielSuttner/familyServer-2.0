package Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class User {

    @SerializedName("Username")
    private String userName;
    @SerializedName("Password")
    private String password;
    @SerializedName("Email")
    private String email;
    @SerializedName("First_Name")
    private String firstName;
    @SerializedName("Last_Name")
    private String lastName;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("Person_ID")
    private String personID;

    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.personID = personID;
    }
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public boolean checkPassword(String password) {
        if(this.password.equals(password) ){
            return true;
        } else {
            return false;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }


}
