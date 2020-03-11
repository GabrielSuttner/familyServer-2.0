package testCases;

import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.User;
import Request.LoginRequest;
import Response.LoginResponse;
import Service.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginServiceTest {
    DataBase db = new DataBase();
    @Test
    public void login() {
        UserAO ua = new UserAO();
        LoginResponse response = null;
        try {
            User user = new User("Gabe","Password", "gabe@byu.edu", "m", "gabriel","suttner", "aspdfasdflaksjdfasd");
            ua.addUser(db.getUserConnection(), user);
            db.closeUserConnection();
            LoginService login = new LoginService();
            LoginRequest request = new LoginRequest(user.getUserName(), user.getPassword());
            response = login.login(request);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void loginFail() {
        UserAO ua = new UserAO();
        LoginResponse response = null;
        try {
            User user = new User("Kitten","Password", "gabe@byu.edu", "m", "gabriel","suttner", "aspdfasdflaksjdfasd");
            ua.addUser(db.getUserConnection(), user);
            db.closeUserConnection();
            LoginService login = new LoginService();
            LoginRequest request = new LoginRequest(user.getUserName(), "Junk");
            response = login.login(request);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(response.isSuccess());
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.clearTables();
    }
}

