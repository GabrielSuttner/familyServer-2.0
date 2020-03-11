package testCases;

import DAO.AuthTokenAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AuthTokenDAOTest {
    private DataBase db;
    private AuthToken authToken;
    private AuthToken diffToken;


    @BeforeEach
    public void setUp() {
        this.db = new DataBase();
        this.authToken = new AuthToken( "Gsutt");
        this.diffToken = new AuthToken("cbush");
    }

    @Test
    public void insertAuthToken() throws Exception {
        AuthTokenAO pd = null;
        AuthToken newAuthToken = null;
        try {
            pd = new AuthTokenAO();
            pd.addToken(this.db.getAuthConnection(), this.authToken);
            newAuthToken = pd.getToken(db.getAuthConnection(), this.authToken.getUserName());
            db.closeAuthConnection();
        } catch (DataAccessException e) {
            db.closeAuthConnection();
        }

        Assertions.assertEquals(newAuthToken, this.authToken);
    }

    @Test
    public void addTokens() throws Exception {
        List<AuthToken> tokens = new ArrayList<>();
        AuthToken token = null;
        AuthTokenAO ao = null;
        tokens.add(this.authToken);
        tokens.add(this.diffToken);

        try {
            ao = new AuthTokenAO();
            ao.addTokens(db.getAuthConnection(), tokens);
            token = ao.getToken(db.getAuthConnection(), this.diffToken.getUserName());
            db.closeAuthConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeAuthConnection();
        }
        Assertions.assertEquals(token, this.diffToken);
    }

    @Test
    public void insertAuthTokenFail() throws Exception {
        AuthTokenAO pd = null;
        try {
            pd = new AuthTokenAO();
            pd.addToken(this.db.getAuthConnection(), this.authToken);
            this.db.closeAuthConnection();
        } catch (DataAccessException e) {
            db.closeAuthConnection();
        }
        //successfully addded a authToken
        boolean failed = false;

        try {
            Connection con = this.db.getAuthConnection();
            pd = new AuthTokenAO();
            pd.addToken(con, this.authToken);
        } catch (DataAccessException e) {
            //error gettign the authToken because its a duplicate
            db.closeAuthConnection();
            failed = true;
        }

        Assertions.assertTrue(failed);
    }

    @Test
    public void getAuthToken() throws DataAccessException {
        AuthTokenAO pd = null;
        try {
            pd = new AuthTokenAO();
            pd.addToken(this.db.getAuthConnection(), this.authToken);
            this.db.closeAuthConnection();
        } catch (DataAccessException  e) {
            db.closeAuthConnection();
        }

        String userID = this.authToken.getUserName();
        AuthToken newAuthToken = null;

        try {
            newAuthToken = pd.getToken(db.getAuthConnection(), userID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(newAuthToken, this.authToken);
    }

    @Test
    public void getAuthTokenFail() throws DataAccessException {
        AuthTokenAO pd = null;
        try {
            pd = new AuthTokenAO();
            pd.addToken(this.db.getAuthConnection(), this.authToken);
            db.closeAuthConnection();
        } catch (DataAccessException  e) {
            db.closeAuthConnection();
        }

        AuthToken newAuthToken = null;
        boolean failed = false;
        try {
            pd.getToken(db.getAuthConnection(), this.diffToken.getTokenID());
            db.closeAuthConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            failed = true;
            db.closeAuthConnection();
        }
        Assertions.assertTrue(failed);
    }



    /**
     *
     */
    @Test
    public void clearAuthTokens() {
        AuthTokenAO pa = new AuthTokenAO();
        AuthToken token = null;
        try {
            pa.addToken(db.getAuthConnection(), authToken);
            this.db.clearAuthTables();
            token = pa.getToken(db.getAuthConnection(), authToken.getTokenID());
            this.db.closeAuthConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                this.db.closeAuthConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        Assertions.assertEquals(token, null);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.clearAuthTables();
    }
}
