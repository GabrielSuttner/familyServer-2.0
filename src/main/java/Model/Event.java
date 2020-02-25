package Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;
import java.util.UUID;

public class Event {
    @SerializedName("Event_ID")
    private String EventID;
    @SerializedName("Username")
    private String AssociatedUsername;
    @SerializedName("Person_ID")
    private String PersonID;
    private double Latitude;
    private double Longitude;
    private String Country;
    private String City;
    @SerializedName("Event_Type")
    private String EventType;
    private int Year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.Latitude, Latitude) == 0 &&
                Double.compare(event.Longitude, Longitude) == 0 &&
                Year == event.Year &&
                EventID.equals(event.EventID) &&
                AssociatedUsername.equals(event.AssociatedUsername) &&
                PersonID.equals(event.PersonID) &&
                Country.equals(event.Country) &&
                City.equals(event.City) &&
                EventType.equals(event.EventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EventID, AssociatedUsername, PersonID, Latitude, Longitude, Country, City, EventType, Year);
    }


    /**
     *
     * @param associatedUsername
     * @param personID
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String associatedUsername, String personID, double latitude, double longitude, String country, String city, String eventType, int year) {
        this.EventID = UUID.randomUUID().toString();
        this.AssociatedUsername = associatedUsername;
        this.PersonID = personID;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Country = country;
        this.City = city;
        this.EventType = eventType;
        this.Year = year;
    }

    public String getEventID() {
        return EventID;
    }

    public String getAssociatedUsername() {
        return AssociatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        AssociatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return PersonID;
    }

    public void setPersonID(String personID) {
        PersonID = personID;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(int latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(int longitude) {
        Longitude = longitude;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }
}
