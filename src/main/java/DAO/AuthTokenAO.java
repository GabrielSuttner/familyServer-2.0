package DAO;

import DataAccess.DataAccessException;
import Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenAO {
    /**
     *
     * @param connection
     * @param userID
     * @return AuthToken
     * @throws DataAccessException
     */
    public AuthToken getToken(Connection connection, String userID) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AuthToken at = null;
    try {
        try {
            String sql = "SELECT Token_ID, Username FROM authTokens WHERE Username = '"+ userID +"';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            at = new AuthToken(rs.getString(1), rs.getString(2));

        } finally {
            if(rs != null) {
                rs.close();
            }
            if(stmt != null) {
                stmt.close();
            }
        } }
    catch (SQLException e) {
        e.printStackTrace();
        throw new DataAccessException("Error retrieving authToken");
    }

        return at;
    }

    /**
     *
     * @param connection
     * @param token
     * @throws DataAccessException
     */
    public void addToken(Connection connection, AuthToken token) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO authTokens (Token_ID, Username)  VALUES(?, ?);";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, token.getTokenID());
            stmt.setString(2, token.getUserName());

            if(stmt.executeUpdate() == 1) {
                System.out.println("Added token for: " +token.getUserName());
            } else {
                System.out.println("Failed to add token for: " + token.getUserName());
            }

        } catch (SQLException e){
            throw new DataAccessException("Error inserting New Token\n");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new DataAccessException("Error closing STMT in addToken");

                }
            }
        }
    }

    /**
     *
     * @param connection
     * @param userName
     * @throws DataAccessException
     */
    public void deleteToken(Connection connection, String userName) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM authTokens WHERE Username = '" + userName +"';";
            stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
            System.out.printf("User: %s deleted from database", userName);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting " + userName+" from authTokens dataBase.");
        }
    }
}
