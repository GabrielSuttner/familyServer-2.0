package Response;

import Model.Person;

import java.util.List;
import java.util.Objects;

public class PersonsResponse {
    private List<Person> data;
    private boolean success;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonsResponse that = (PersonsResponse) o;
        return success == that.success &&
                Objects.equals(data, that.data) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, success, message);
    }

    private String message = null;

    public PersonsResponse() {

    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PersonsResponse(List<Person> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
