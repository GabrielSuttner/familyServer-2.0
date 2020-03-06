package Model;

import com.google.gson.annotations.SerializedName;

import javax.swing.plaf.PanelUI;
import java.util.Objects;
import java.util.UUID;

/**
 *
 */
public class Person {
    @SerializedName("personID")
    private String PersonID;

    @Override
    public int hashCode() {
        return Objects.hash(PersonID, associatedUsername, FirstName, LastName, Gender, FatherID, MotherID, SpouseID);
    }

    @SerializedName("associatedUsername")
    private String associatedUsername= null;
    @SerializedName("firstName")
    private String FirstName= null;
    @SerializedName("lastName")
    private String LastName= null;
    @SerializedName("gender")
    private String Gender= null;
    @SerializedName("fatherID")
    private String FatherID= null;
    @SerializedName("motherID")
    private String MotherID= null;
    @SerializedName("spouseID")
    private String SpouseID= null;
    public Person(){};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return PersonID.equals(person.PersonID) &&
                associatedUsername.equals(person.associatedUsername) &&
                FirstName.equals(person.FirstName) &&
                LastName.equals(person.LastName) &&
                Gender.equals(person.Gender) &&
                Objects.equals(FatherID, person.FatherID) &&
                Objects.equals(MotherID, person.MotherID) &&
                Objects.equals(SpouseID, person.SpouseID);
    }


    /**
     *
     * @param userName
     * @param firstName
     * @param lastName
     * @param gender
     */
    public Person(String userName, String firstName, String lastName, String gender){
        PersonID = UUID.randomUUID().toString();
        this.associatedUsername = userName;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Gender = gender;
    }

    /**
     *
     * @param personId
     * @param associatedUsername
     * @param firstName
     * @param lastName
     * @param gender
     */
    public Person(String personId, String associatedUsername, String firstName, String lastName, String gender) {
        this.PersonID = personId;
        this.associatedUsername = associatedUsername;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Gender = gender;
    }
    public Person(String personId, String associatedUsername, String firstName, String lastName, String gender, String dad, String mom, String spouse) {
        this.PersonID = personId;
        this.associatedUsername = associatedUsername;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Gender = gender;
        this.FatherID = dad;
        this.MotherID = mom;
        this.SpouseID = spouse;
    }

    /**
     * @return
     */
        public String getPersonID() {
        return PersonID;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return associatedUsername;
    }

    /**
     * @param username
     */
    public void setUsername(String username) { associatedUsername = username; }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        LastName = lastName;
    }

    /**
     *
     * @return
     */
    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getFatherID() {
        return FatherID;
    }

    public void setFatherID(String fatherID) {
        FatherID = fatherID;
    }

    public String getMotherID() {
        return MotherID;
    }

    public void setMotherID(String motherID) {
        MotherID = motherID;
    }

    public String getSpouseID() {
        return SpouseID;
    }

    public void setSpouseID(String spouseID) {
        SpouseID = spouseID;
    }

}
