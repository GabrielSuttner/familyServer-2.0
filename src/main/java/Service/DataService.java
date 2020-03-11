package Service;

import DAO.EventAO;
import DAO.PersonAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import HelperClass.Utility;
import Model.Event;
import Model.Person;
import Response.EventsResponse;
import Response.PersonResponse;
import Response.PersonsResponse;
import Response.EventResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * retrieves events and persons from the database
 */
public class DataService{
    Utility util = new Utility();

    /**
     * retreive one person from the database.
     * @param personID
     * @return
     */
    public PersonResponse getPerson(String personID) {
        Person person = null;
        DataBase db = new DataBase();
        PersonAO pa = new PersonAO();
        PersonResponse pr = new PersonResponse();

        try {
            person = pa.getPerson(db.getPersonConnection(), personID);
            db.closePersonConnection();
            pr.setFields(person);
        } catch (DataAccessException e) {
            pr.setSuccess(false);
            pr.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return pr;
    }


    /**
     * retreive all people that are associated with a personID
     * @param personID
     * @return
     */
    public PersonsResponse getPeople(String personID) {
        PersonsResponse response = new PersonsResponse();
        List<Person> persons;
        DataBase db = new DataBase();
        PersonAO pa = new PersonAO();

        try {
            Person p = pa.getPerson(db.getPersonConnection(), personID);
            persons = pa.getPersonByAssociated(db.getPersonConnection(), p.getUsername());
            db.closePersonConnection();

            response.setData(persons);
            response.setSuccess(true);

        } catch (DataAccessException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

    /**
     * retreive event by ID.
     * @param eventID
     * @return
     */
    public EventResponse getEvent(String eventID) {
        Event event;
        DataBase db = new DataBase();
        EventAO pa = new EventAO();
        EventResponse pr = new EventResponse();

        try {
            event = pa.getEvent(db.getEventConnection(), eventID);
            db.closeEventConnection();
            pr.setField(event);
        } catch (DataAccessException e) {
            pr.setSuccess(false);
            pr.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closeEventConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return pr;
    }

    /**
     * get all events that have given associated userName
     * @param personID
     * @return
     */
    public EventsResponse getEvents(String personID) {
        EventsResponse response = new EventsResponse();
        List<Event> events;
        DataBase db = new DataBase();
        EventAO ea = new EventAO();
        Person p = util.getPerson(personID);
        try {
            events = ea.getUserEventsByUserName(db.getEventConnection(), p.getUsername());

            response.setData(events);
            response.setSuccess(true);
        } catch (DataAccessException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closeEventConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

}
