package testCases;

import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Person;
import Model.User;
import Response.FillResponse;
import Service.FillService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class FillServiceTest {
    DataBase db = new DataBase();

    @Test
    public void fillTest() {
        Person p = new Person("Gabe", "Suttner", "cat", "m");
        User user = new User("Gabe", "asdfasd");
        try {
            UserAO ua = new UserAO();
            ua.addUser(db.getUserConnection(), user);
            PersonAO pa = new PersonAO();
            pa.addPerson(db.getPersonConnection(), p);

            FillService service = new FillService();
            FillResponse fp = service.fill("Gabe", 4);

            List<Person> people = pa.getPersonByAssociated(db.getPersonConnection(), "Gabe");
            db.closeAllConnections();
            Assertions.assertEquals(31, people.size());

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fillTestFail() {
        Person p = new Person("Gabe", "Suttner", "cat", "m");
        User user = new User("Gabe", "asdfasd");
        try {
            UserAO ua = new UserAO();
            ua.addUser(db.getUserConnection(), user);
            PersonAO pa = new PersonAO();
            pa.addPerson(db.getPersonConnection(), p);

            FillService service = new FillService();
            FillResponse fp = service.fill("Cat", 4);

            List<Person> people = pa.getPersonByAssociated(db.getPersonConnection(), "Gabe");
            db.closeAllConnections();
            Assertions.assertFalse(people.size() > 1);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.clearTables();
    }
}
