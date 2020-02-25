package RequestResult;

import Model.Event;

import java.util.List;

public class EventsResponse {
    private List<Event> data;

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private boolean success;
}
