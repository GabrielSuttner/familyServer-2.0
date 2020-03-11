package testCases;

import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Request.RegisterRequest;
import Response.RegisterResponse;
import Service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

public class RegisterServiceTest {
    DataBase db = new DataBase();

    @Test
    public void  register(){
        RegisterRequest request = new RegisterRequest("Gabe","Password", "gabe@byu.edu", "m", "gabriel","suttner");
        RegisterService service = new RegisterService();
        RegisterResponse response = service.register(request);

        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void  registerFail(){
        RegisterRequest request = new RegisterRequest("Gabe",null, "gabe@byu.edu", "m", "gabriel","suttner");
        RegisterService service = new RegisterService();
        service.register(request);
        RegisterResponse response = service.register(request);

        Assertions.assertFalse(response.isSuccess());
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.clearTables();
    }
}
