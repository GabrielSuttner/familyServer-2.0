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
     *
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
     *
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
     *
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
     *
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
    public void closeAllConnections(boolean t) throws DataAccessException {
        closePersonConnection(t);
        closeAuthConnection(t);
        closeEventConnection(t);
        closeUserConnection(t);
    }

    /**
     *
     * @param commit
     * @throws DataAccessException
     */
    public void closePersonConnection(boolean commit) throws DataAccessException {
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
     * @param commit
     * @throws DataAccessException
     */
    public void closeEventConnection(boolean commit) throws DataAccessException {
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
     * @param commit
     * @throws DataAccessException
     */
    public void closeAuthConnection(boolean commit) throws DataAccessException {
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
     * @param commit
     * @throws DataAccessException
     */
    public void closeUserConnection(boolean commit) throws DataAccessException {
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

    public void clearUserTables() throws DataAccessException {

        if(this.userConnection == null) {
            openUserConnection();
        }
        try (Statement stmt = this.userConnection.createStatement()){
            String sql = "DELETE FROM users;";
            stmt.executeUpdate(sql);
            closeUserConnection(true);
        } catch (SQLException e) {
            closeUserConnection(false);
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
            closePersonConnection(true);
        } catch (SQLException e) {
            closePersonConnection(false);
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
            closeEventConnection(true);
        } catch (SQLException e) {
            closeEventConnection(false);
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
            closeAuthConnection(true);
        } catch (SQLException e) {
            closeAuthConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing authTokens tables");
        }
    }

    /**
     *
     * @throws SQLException
     */
    //When database is created, it goes through and creates its own pre filled database.
    public DataBase() {
    }

}
