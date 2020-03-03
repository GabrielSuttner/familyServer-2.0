package DAO;

import DataAccess.DataAccessException;
import Model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventAO {
    /**
     *
     * @param connection
     * @param event
     * @throws SQLException
     */
    public void addEvent(Connection connection, Event event) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO events (Event_ID, Username, Person_ID, Latitude," +
                    " Longitude, Country, City, EventType, Year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            if(stmt.executeUpdate() == 1) {
                System.out.println("Added Event: " + event.getEventID());
            } else {
                System.out.println("Failed to add Event: " + event.getEventID());
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error adding event " + event.getEventID());
        }
    }

    /**
     *
     * @param connection
     * @param event
     * @throws SQLException
     */
    public void updateEvent(Connection connection, Event event) throws SQLException {
        PreparedStatement stmt = null;
        try{
            String sql = "UPDATE events" +
                    "set Event_ID = ?, Username = ?, Person_ID = ?, Latitude = ?," +
                    "Longitude = ?, Country = ?, City = ?, EventType = ?, Year = ?";

            stmt = connection.prepareStatement(sql);
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            if(stmt.executeUpdate() == 1) {
                System.out.println("Updated Event: " + event.getEventID());
            } else {
                System.out.println("Failed to update Event: " + event.getEventID());
            }

        } finally {
            if(stmt!= null) {
                stmt.close();
            }
        }
    }

    /**
     *
     * @param connection
     * @param eventID
     * @throws SQLException
     */
    public void deleteEvent(Connection connection, String eventID) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM events";
            stmt = connection.prepareStatement(sql);
            if(stmt.executeUpdate() == 1){
                System.out.printf("Deleted %s\n", eventID);
            } else {
                System.out.printf("Failed to delete %s.\n", eventID);
            }


        } finally {
            if(stmt != null){
                stmt.close();
            }
        }
    }

    /**
     *
     * @param connection
     * @return list of events
     * @throws SQLException
     */
    public List<Event> getEvents(Connection connection) throws DataAccessException {
        List<Event> events = new ArrayList<>();

        return events;
    }

    /**
     *
     * @param connection
     * @throws SQLException
     */
    public void clearEvents(Connection connection) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from events";
            stmt = connection.prepareStatement(sql);

            int count = stmt.executeUpdate();

            sql = "delete from sqlite_sequence where name = 'events'";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();

            System.out.printf("Deleted %d events\n", count);

        } finally {
            if(stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * @param eventID
     * @return specified event
     */
    public Event getEvent(Connection connection, String eventID) {
        Event newEv = null;
        return newEv;
    }

}
