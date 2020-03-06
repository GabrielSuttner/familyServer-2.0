package Service;

import DAO.EventAO;
import DAO.PersonAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataGenerator.Utility;
import Model.Event;
import Model.Person;
import Response.EventsResponse;
import Response.PersonResponse;
import Response.PersonsResponse;
import Response.EventResponse;
import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class DataService{
    Utility util = new Utility();
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
    public PersonsResponse getPeople(String personID) {
        PersonsResponse response = new PersonsResponse();
        List<Person> persons = new ArrayList<>();
        Person person = null;
        DataBase db = new DataBase();
        PersonAO pa = new PersonAO();
        try {

            util.isRelated(pa.getPerson(db.getPersonConnection(), personID), null);
            db.closePersonConnection(true);
            Set<String> ids = util.getPersonIDSet();
            ids.add(personID);

            for(String s : ids){
                person = pa.getPerson(db.getPersonConnection(), s);
                db.closePersonConnection(true);
                persons.add(person);
            }

            response.setData(persons);
            response.setSuccess(true);
        } catch (DataAccessException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            e.printStackTrace();
            try {
                db.closePersonConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }
    public EventResponse getEvent(String eventID) {
        Event event = null;
        DataBase db = new DataBase();
        EventAO pa = new EventAO();
        EventResponse pr = new EventResponse();

        try {
            event = pa.getEvent(db.getEventConnection(), eventID);
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
                db.closeEventConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

}
