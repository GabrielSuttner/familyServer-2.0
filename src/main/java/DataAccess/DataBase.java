package DataAccess;

import java.sql.*;

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
            this.personConnection.setAutoCommit(false);

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
            this.eventConnection.setAutoCommit(false);

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
            this.userConnection.setAutoCommit(false);

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
            this.authConnection.setAutoCommit(false);

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
        try {
            if (commit) {
                this.personConnection.commit();
            } else {
                this.personConnection.rollback();
            }
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
        try {
            if (commit) {
                this.eventConnection.commit();
            } else {
                this.eventConnection.rollback();
            }
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
        try {
            if (commit) {
                this.authConnection.commit();
            } else {
                this.authConnection.rollback();
            }
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
        try {
            if (commit) {
                this.userConnection.commit();
            } else {
                this.userConnection.rollback();
            }
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
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearPersonsTable() throws DataAccessException {
        if(this.userConnection == null) {
            openUserConnection();
        }
        try (Statement stmt = this.personConnection.createStatement()){
            String sql = "DELETE FROM persons;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing persons tables");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearEventsTables() throws DataAccessException {
        try (Statement stmt = eventConnection.createStatement()){
            String sql = "DELETE FROM events;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing events tables");
        }
    }



    /**
     *
     * @throws DataAccessException
     */
    public void clearAuthTables() throws DataAccessException {
        try (Statement stmt = authConnection.createStatement()){
            String sql = "DELETE FROM authToken;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing authToken tables");
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
