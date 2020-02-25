package Service;

import Model.Event;
import Model.Person;

import java.util.ArrayList;
import java.util.List;

public class FillService {

    /**
     *
     * @return list of dummy persons
     */
    private List<Person> generatePersons() {
        List<Person> people = new ArrayList<>();
        people.add(new Person( "Gsutt", "Gabe", "Suttner", "m"));

        return people;
    }

    /**
     *
     * @return list of dummy events
     */
    private List<Event> generateEvents() {
        List<Event> events = new ArrayList<>();

        return events;
    }

}
