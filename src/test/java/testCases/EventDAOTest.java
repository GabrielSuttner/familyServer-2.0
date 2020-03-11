package testCases;

import DAO.EventAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class EventDAOTest {
    private DataBase db;
    private Event event;
    private Event diffEvent;


    @BeforeEach
    public void setUp() {
        this.db = new DataBase();
        this.event = new Event( "Gsutt", "adsdfasdf", 1000, 2312,
                "US", "ST George", "Baptism", 1996);
        this.diffEvent = new Event("cbush", "asdfasdfasdf", 10000, 2343,
                "US", "Provo", "Marriage", 1999);
    }

    @Test
    public void insertEvent() throws Exception {
        EventAO pd = null;
        Event newEvent = null;
        try {
            pd = new EventAO();
            pd.addEvent(this.db.getEventConnection(), this.event);
            newEvent = pd.getEvent(db.getEventConnection(), this.event.getEventID());
            db.closeEventConnection();
        } catch (DataAccessException e) {
            db.closeEventConnection();
        }

        Assertions.assertEquals(newEvent, this.event);
    }
    @Test
    public void getAllEvents() throws Exception {
        EventAO pd = null;
        List<Event> events = null;

        try {
            pd = new EventAO();
            pd.addEvent(this.db.getEventConnection(), this.event);
            pd.addEvent(this.db.getEventConnection(), this.diffEvent);
            events = pd.getEvents(db.getEventConnection());
            db.closeEventConnection();
        } catch (DataAccessException e) {
            db.closeEventConnection();
        }

        Assertions.assertEquals(events.size(), 2);
    }
    @Test
    public void insertEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        List<Event> eventCheck = null;
        events.add(this.event);
        events.add(this.diffEvent);
        EventAO ea =  null;

        try {
            ea = new EventAO();
            ea.addEvents(db.getEventConnection(), events);
            eventCheck = ea.getEvents(db.getEventConnection());
            db.closeEventConnection();
        } catch (DataAccessException e) {
            db.closeEventConnection();
            e.printStackTrace();
        }
        Assertions.assertEquals(events, eventCheck);
    }


    @Test
    public void insertEventFail() throws Exception {
        EventAO pd = null;
        try {
            pd = new EventAO();
            pd.addEvent(this.db.getEventConnection(), this.event);
            this.db.closeEventConnection();
        } catch (DataAccessException e) {
            db.closeEventConnection();
        }
        //successfully addded a event
        boolean failed = false;

        try {
            Connection con = this.db.getEventConnection();
            pd = new EventAO();
            pd.addEvent(con, this.event);
        } catch (DataAccessException e) {
            //error gettign the event because its a duplicate
            db.closeEventConnection();
            failed = true;
        }

        Assertions.assertTrue(failed);
    }

    @Test
    public void getEvent() throws DataAccessException {
        EventAO pd = null;
        try {
            pd = new EventAO();
            pd.addEvent(this.db.getEventConnection(), this.event);
            this.db.closeEventConnection();
        } catch (DataAccessException  e) {
            db.closeEventConnection();
        }

        String eventID = this.event.getEventID();
        Event newEvent = null;

        try {
            newEvent = pd.getEvent(db.getEventConnection(), eventID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(newEvent, this.event);
    }

    @Test
    public void getEventFail() throws DataAccessException {
        EventAO pd = null;
        try {
            pd = new EventAO();
            pd.addEvent(this.db.getEventConnection(), this.event);
            db.closeEventConnection();
        } catch (DataAccessException  e) {
            db.closeEventConnection();
        }

        Event newEvent = null;
        boolean failed = false;
        try {
            pd.getEvent(db.getEventConnection(), this.diffEvent.getEventID());
            db.closeEventConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            failed = true;
            db.closeEventConnection();
        }
        Assertions.assertTrue(failed);
    }



    /**
     *
     */
    @Test
    public void clearEvents() {
        EventAO pa = new EventAO();
        Event token = null;
        try {
            pa.addEvent(db.getEventConnection(), event);
            this.db.clearEventsTables();
            token = pa.getEvent(db.getEventConnection(), event.getEventID());
            this.db.closeEventConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                this.db.closeEventConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        Assertions.assertEquals(token, null);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.clearEventsTables();
    }
}
