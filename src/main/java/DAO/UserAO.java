package DAO;

import DataAccess.DataAccessException;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAO {
    public void addUser(Connection connection, User user) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO users (Username, Password, Email, First_Name, " +
                    "Last_Name, Gender, Person_ID) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?);";
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
            System.out.println("Added user: " + user.getUserName());
        } catch(SQLException e ){
            throw new DataAccessException("Failed to add user: " + user.getUserName());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error closing Result Set or PreparedStatement");
            }
        }
    }

    public void addUsers(Connection connection, List<User> users) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            for(User user : users) {
                String sql = "INSERT INTO users (Username, Password, Email, First_Name, " +
                        "Last_Name, Gender, Person_ID) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?);";
                stmt = connection.prepareStatement(sql);

                stmt.setString(1, user.getUserName());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFirstName());
                stmt.setString(5, user.getLastName());
                stmt.setString(6, user.getGender());
                stmt.setString(7, user.getPersonID());

                stmt.executeUpdate();
                System.out.println("Added user: " + user.getUserName());
            }
        } catch(SQLException e ){
            e.printStackTrace();
            throw new DataAccessException("Failed to add user in addUsers method");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error closing Result Set or PreparedStatement");
            }
        }
    }


    public User getUser(Connection connection, String userID) throws DataAccessException {
        User newU = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            String sql = "SELECT Username, Password, Email, First_Name, Last_Name, Gender, Person_ID  FROM users " +
                    "WHERE username = '" + userID + "';";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
        } catch(SQLException e ){
            throw new DataAccessException("Error finding User");
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


        return user;
    }

    public List<User> getUsers(Connection connection) throws DataAccessException {
        List<User> users = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Username, Password, Email, First_Name, Last_Name, Gender, " +
                    "Person_ID FROM users";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while(rs.next()) {
                users.add(new User(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6),
                        rs.getString(7)));
            }
        } catch(SQLException e ){
            throw new DataAccessException("Error retrieving Users");
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
        return users;
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearUsersTables(Connection connection) throws DataAccessException {
        try (Statement stmt = connection.createStatement()){
            String sql = "DELETE FROM users;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing user tables");
        }
    }

}
