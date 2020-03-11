package DataAccess;

import Model.User;

import java.sql.*;
import java.util.List;

public class DataBase {
    private Connection personConnection = null;
    private Connection eventConnection = null;
    private Connection userConnection = null;
    private Connection authConnection = null;

    /**
     * return PersonConnection, if null, new connection is opened.
     * @return
     * @throws DataAccessException
     */
    public Connection getPersonConnection() throws DataAccessException {
        if(this.personConnection == null) {
            openPersonConnection();
        }
        return personConnection;
    }

    /**
     * return eventConnection, if null, new connection is opened.
     * @return
     * @throws DataAccessException
     */
    public Connection getEventConnection() throws DataAccessException {
        if(this.eventConnection == null) {
            openEventConnection();
        }
        return eventConnection;
    }

    /**
     * return userConnection, if null, new connection is opened.
     * @return
     * @throws DataAccessException
     */
    public Connection getUserConnection() throws DataAccessException {
        if(this.userConnection == null) {
            openUserConnection();
        }
        return userConnection;
    }

    /**
     * return authConnection, if null, new connection is opened.
     * @return
     * @throws DataAccessException
     */
    public Connection getAuthConnection() throws DataAccessException {
        if(this.authConnection == null) {
            openAuthConnection();
        }
        return authConnection;
    }

    /**
     *
     * @throws DataAccessException
     */
    public void openPersonConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";
        if(this.personConnection != null) {
            return;
        }
        try {
            this.personConnection = DriverManager.getConnection(url);
            this.personConnection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to persons database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public  void openEventConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";

        try {
            this.eventConnection = DriverManager.getConnection(url);
            this.eventConnection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to Events database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void openUserConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";

        try {
            this.userConnection = DriverManager.getConnection(url);
            this.userConnection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to user database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void openAuthConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";

        try {
            this.authConnection = DriverManager.getConnection(url);
            this.authConnection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to auth database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void closeAllConnections() throws DataAccessException {
        closePersonConnection();
        closeAuthConnection();
        closeEventConnection();
        closeUserConnection();
    }

    /**
     *
     *
     * @throws DataAccessException
     */
    public void closePersonConnection() throws DataAccessException {
        if(this.personConnection == null) return;
        try {
            this.personConnection.close();
            this.personConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     *
     *
     * @throws DataAccessException
     */
    public void closeEventConnection( ) throws DataAccessException {
        if(eventConnection == null) return;
        try {
            this.eventConnection.close();
            this.eventConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     *
     *
     * @throws DataAccessException
     */
    public void closeAuthConnection( ) throws DataAccessException {
        if(this.authConnection == null) return;
        try {
            this.authConnection.close();
            this.authConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     *
     * @param
     * @throws DataAccessException
     */
    public void closeUserConnection() throws DataAccessException {
        if(this.userConnection == null) return;
        try {
            this.userConnection.close();
            this.userConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     * Attempts to close all database connections.
     * @throws DataAccessException
     */
    public void clearTables() throws DataAccessException {
       clearPersonsTable();
       clearAuthTables();
       clearEventsTables();
       clearUserTables();
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearUserTables() throws DataAccessException {
        if(this.userConnection == null) {
            openUserConnection();
        }
        try (Statement stmt = this.userConnection.createStatement()){
            String sql = "DELETE FROM users;";
            stmt.executeUpdate(sql);
            closeUserConnection();
        } catch (SQLException e) {
            closeUserConnection();
            throw new DataAccessException("SQL Error encountered while clearing Users tables");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearPersonsTable() throws DataAccessException {
        if(this.personConnection == null) {
            openPersonConnection();
        }
        try (Statement stmt = this.personConnection.createStatement()){
            String sql = "DELETE FROM persons";
            stmt.executeUpdate(sql);
            closePersonConnection();
        } catch (SQLException e) {
            closePersonConnection();
            throw new DataAccessException("SQL Error encountered while clearing persons tables");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearEventsTables() throws DataAccessException {
        if(this.eventConnection == null) {
            openEventConnection();
        }
        try (Statement stmt = eventConnection.createStatement()){
            String sql = "DELETE FROM events;";
            stmt.executeUpdate(sql);
            closeEventConnection();
        } catch (SQLException e) {
            closeEventConnection();
            throw new DataAccessException("SQL Error encountered while clearing events tables");
        }
    }



    /**
     *
     * @throws DataAccessException
     */
    public void clearAuthTables() throws DataAccessException {
        if(authConnection == null) {
            openAuthConnection();
        }
        try (Statement stmt = authConnection.createStatement()){
            String sql = "DELETE FROM authTokens;";
            stmt.executeUpdate(sql);
            closeAuthConnection();
        } catch (SQLException e) {
            closeAuthConnection();
            throw new DataAccessException("SQL Error encountered while clearing authTokens tables");
        }
    }

    /**
     *
     * @throws SQLException
     */
    //When database is created, it goes through and creates its own pre filled database.

}
