package RequestResult;

import Model.Person;

import java.util.List;

public class PersonsResponse {
    private List<Person> data;
    private boolean success;

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
}
