package Service;

import DAO.EventAO;
import DAO.PersonAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Event;
import Model.Person;
import RequestResult.EventReponse;
import RequestResult.EventsResponse;
import RequestResult.PersonResponse;
import RequestResult.PersonsResponse;

import java.util.List;

public class DataService{
    public PersonResponse getPerson(String personID) {
        Person person = null;
        DataBase db = new DataBase();
        PersonAO pa = new PersonAO();
        PersonResponse pr = new PersonResponse();

        try {
            person = pa.getPerson(db.getPersonConnection(), personID);
            db.closePersonConnection(true);
            pr.setFields(person);
        } catch (DataAccessException e) {
            pr.setSuccess(false);
            pr.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closePersonConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return pr;
    }
    public PersonsResponse getPeople() {
        PersonsResponse people = new PersonsResponse();
        List<Person> persons = null;
        DataBase db = new DataBase();
        PersonAO pa = new PersonAO();

        try {
            persons = pa.getPersons(db.getPersonConnection());
            db.closePersonConnection(true);
            people.setData(persons);
        } catch (DataAccessException e) {
            people.setSuccess(false);
            people.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closePersonConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return people;
    }
    public EventReponse getEvent(String eventID) {
        Event event = null;
        DataBase db = new DataBase();
        EventAO pa = new EventAO();
        EventReponse pr = new EventReponse();

        try {
            event = pa.getEvent(db.getPersonConnection(), eventID);
            db.closeEventConnection(true);
            pr.setField(event);
        } catch (DataAccessException e) {
            pr.setSuccess(false);
            pr.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closeEventConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return pr;
    }

    public EventsResponse getEvents() {
        EventsResponse people = new EventsResponse();
        List<Event> events = null;
        DataBase db = new DataBase();
        EventAO pa = new EventAO();

        try {
            events = pa.getEvents(db.getEventConnection());
            db.closeEventConnection(true);
            people.setData(events);
        } catch (DataAccessException e) {
            people.setSuccess(false);
            people.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closeEventConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return people;
    }

}
