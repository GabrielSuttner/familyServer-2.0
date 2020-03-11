package DAO;

import DataAccess.DataAccessException;
import Model.Event;
import Model.Person;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventAO {
    /**
     * Add event to Database.
     * @param connection
     * @param event
     * @throws SQLException
     */
    public void addEvent(Connection connection, Event event) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO events (Event_ID, Username, Person_ID, Latitude," +
                    " Longitude, Country, City, Event_Type, Year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                System.out.println("Added Event " + event.toString() );
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
     * Delete all persons in Database by associated Username.
     * @param connection
     * @param associatedUserName
     * @throws DataAccessException
     */
    public void deleteEventByAssociatedUserName(Connection connection, String associatedUserName) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM events WHERE Username = '" + associatedUserName +"';";
            stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
            System.out.printf("Deleted %s.\n", associatedUserName);
        } catch(SQLException e) {
            throw new DataAccessException("Failed to delete " + associatedUserName);

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add list of events to the database
     * @param connection
     * @param events
     * @throws DataAccessException
     */
    public void addEvents(Connection connection, List<Event> events) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            for (Event event : events) {

            String sql = "INSERT INTO events (Event_ID, Username, Person_ID, Latitude," +
                    " Longitude, Country, City, Event_Type, Year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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


        }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error adding event in addEvents Method");
        }

    }

    /**
     * update a given event, updates eveny by the eventID.
     * @param connection
     * @param event
     * @throws SQLException
     */
    public void updateEvent(Connection connection, Event event) throws SQLException {
        PreparedStatement stmt = null;
        try{
            String sql = "UPDATE events" +
                    " set Event_ID = ?, Username = ?, Person_ID = ?, Latitude = ?," +
                    " Longitude = ?, Country = ?, City = ?, EventType = ?, Year = ? WHERE " +
                    " Event_ID = '" +event.getEventID()+ "';";

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
     * Delete event by EventID
     * @param connection
     * @param eventID
     * @throws SQLException
     */
    public void deleteEvent(Connection connection, String eventID) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM events WHERE Event_ID = '" + eventID + "';";
            stmt = connection.prepareStatement(sql);
            if(stmt.executeUpdate() == 1){
                System.out.printf("Deleted %s\n", eventID);
            } else {
                System.out.printf("Failed to delete %s.\n", eventID);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("error deleting " + eventID + "from database");
        } finally {
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get list of all events.
     * @param connection
     * @return list of events
     * @throws SQLException
     */
    public List<Event> getEvents(Connection connection) throws DataAccessException {
        List<Event> events = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Event_ID, Username, Person_ID, Latitude," +
                    " Longitude, Country, City, Event_Type, Year FROM events;";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {

                events.add( new Event(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getFloat(4), rs.getFloat(5),
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getInt(9)));
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error retreiving all data from Events table");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return events;
    }

    /**
     * get all events that are associated with a username.
     * @param connection
     * @param personID
     * @return
     * @throws DataAccessException
     */
    public List<Event> getUserEvents(Connection connection, String personID) throws DataAccessException {
        List<Event> events = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Event_ID, Username, Person_ID, Latitude," +
                    " Longitude, Country, City, Event_Type, Year FROM events " +
                    "Where Person_ID = '" + personID + "';";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {

                events.add( new Event(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getFloat(4), rs.getFloat(5),
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getInt(9)));
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error retreiving all data from Events table");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return events;
    }

    /**
     * Get event by EventID
     * @param eventID
     * @return specified event
     */
    public Event getEvent(Connection connection, String eventID) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Event newEvent = null;

        try {
            String sql = "SELECT Event_ID, Username, Person_ID, Latitude," +
                    " Longitude, Country, City, Event_Type, Year FROM events " +
                    "WHERE Event_ID = '" + eventID + "';";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            newEvent = new Event(rs.getString(1), rs.getString(2),
                    rs.getString(3), rs.getFloat(4), rs.getFloat(5),
                    rs.getString(6), rs.getString(7), rs.getString(8),
                    rs.getInt(9));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error finding Event");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error closing Result Set or PreparedStatement");
            }
        }

        return newEvent;
    }

    /**
     * Get all events that are associated with a username.
     * @param connection
     * @param s
     * @return
     * @throws DataAccessException
     */
    public List<Event> getUserEventsByUserName(Connection connection, String s) throws DataAccessException {
        List<Event> events = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Event_ID, Username, Person_ID, Latitude," +
                    " Longitude, Country, City, Event_Type, Year FROM events " +
                    "Where Username = '" + s + "';";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {

                events.add( new Event(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getFloat(4), rs.getFloat(5),
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getInt(9)));
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error retreiving all data from Events table");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return events;
    }
}
