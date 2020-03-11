package DAO;

import DataAccess.DataAccessException;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAO {
    /**
     * Add user to database.
     * @param connection
     * @param user
     * @throws DataAccessException
     */
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

    /**
     * Add list of users to database.
     * @param connection
     * @param users
     * @throws DataAccessException
     */
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

    /**
     * Returns the user from the username
     * @param connection
     * @param userName
     * @return
     * @throws DataAccessException
     */
    public User getUser(Connection connection, String userName) throws DataAccessException {
        User newU = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            String sql = "SELECT Username, Password, Email, First_Name, Last_Name, Gender, Person_ID  FROM users " +
                    "WHERE username = '" + userName + "';";
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

    /**
     * get all users from database.
     * @param connection
     * @return
     * @throws DataAccessException
     */
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

}
