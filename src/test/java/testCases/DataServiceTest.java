package testCases;

import DAO.PersonAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Person;
import Response.PersonResponse;
import Response.PersonsResponse;
import Service.DataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DataServiceTest {
    Person p = new Person("GaBE", "suttner", "thing", "m");
    Person a = new Person("seth", "ds", "thing", "m");
    Person t = new Person("beau", "suttner", "thing", "m");
    DataBase db = new DataBase();
    @Test
    public void getPerson() {
        PersonAO pa = new PersonAO();
        DataService service = new DataService();
        List<Person> personList = new ArrayList<>();

        personList.add(p);
        personList.add(a);
        personList.add(t);

        try {
            pa.addPersons(db.getPersonConnection(), personList);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        PersonResponse pr = service.getPerson(p.getPersonID());

        Assertions.assertEquals(pr.getPersonID(), p.getPersonID());
    }

    @Test
    public void getPersonFail() {
        PersonAO pa = new PersonAO();
        DataService service = new DataService();
        List<Person> personList = new ArrayList<>();

        personList.add(p);
        personList.add(a);
        personList.add(t);

        try {
            pa.addPersons(db.getPersonConnection(), personList);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        PersonResponse pr = service.getPerson("p.getPersonID()");

        Assertions.assertFalse(pr.getPersonID() == p.getPersonID());
    }

    @Test
    public void getPersons() {
        PersonAO pa = new PersonAO();
        DataService service = new DataService();
        List<Person> personList = new ArrayList<>();

        p.setUsername("Gabe");
        a.setUsername("Gabe");
        t.setUsername("Gabe");

        personList.add(p);
        personList.add(a);
        personList.add(t);

        try {
            pa.addPersons(db.getPersonConnection(), personList);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        PersonsResponse pr = service.getPeople(p.getPersonID());
        List<Person> temp = pr.getData();
        Assertions.assertEquals(temp.size(), personList.size());
    }

    @Test
    public void getPersonsFail() {
        PersonAO pa = new PersonAO();
        DataService service = new DataService();
        List<Person> personList = new ArrayList<>();

        personList.add(p);
        personList.add(a);
        personList.add(t);

        try {
            pa.addPersons(db.getPersonConnection(), personList);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        PersonsResponse pr = service.getPeople(p.getPersonID());
        List<Person> temp = pr.getData();
        //no associated username between them.
        Assertions.assertFalse(temp.size() == personList.size());
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.clearTables();
    }
}
