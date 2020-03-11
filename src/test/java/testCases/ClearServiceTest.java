package testCases;

import DAO.PersonAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Person;
import Response.ClearResponse;
import Service.ClearService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClearServiceTest {
    @Test
    public void clear() {
        DataBase db = new DataBase();
        PersonAO ps = new PersonAO();
        List<Person> persons = null;
        try {
            ps.addPerson(db.getPersonConnection(), new Person("asd", "ads", "ads", "m"));
            db.closePersonConnection();

            ClearService cs = new ClearService();
            ClearResponse cr = cs.clear();

            persons = ps.getPersons(db.getPersonConnection());
            db.closePersonConnection();
            Assertions.assertEquals(persons.size(), 0);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
