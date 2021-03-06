package DAO;

import DataAccess.DataAccessException;
import Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthTokenAO {
    /**
     *Returns AuthToken that cooresponds to a given userID.
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
     * Returns AuthToken from the authorizationID.
     * @param connection
     * @param authToken
     * @return AuthToken
     * @throws DataAccessException
     */
    public AuthToken getTokenByAuthToken(Connection connection, String authToken) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        AuthToken at = null;
        try {
            String sql = "SELECT Token_ID, Username FROM authTokens WHERE Token_ID = '"+ authToken +"';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            String id = rs.getString(1);
            String user = rs.getString(2);
            at = new AuthToken(id, user);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error retrieving authToken");
        }

        return at;
    }

        /**
     * Add Token to Token table
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
     * add List<AuthToken> to database.
     * @param authConnection
     * @param tokens
     * @throws DataAccessException
     */
    public void addTokens(Connection authConnection, List<AuthToken> tokens) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            for(AuthToken token: tokens) {
                String sql = "INSERT INTO authTokens (Token_ID, Username)  VALUES(?, ?);";

                stmt = authConnection.prepareStatement(sql);
                stmt.setString(1, token.getTokenID());
                stmt.setString(2, token.getUserName());

                if (stmt.executeUpdate() == 1) {
                    System.out.println("Added token for: " + token.getUserName());
                } else {
                    System.out.println("Failed to add token for: " + token.getUserName());
                }
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
     * Delete all tokens for a given username.
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
