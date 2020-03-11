package testCases;//insert
//clear
//retreive
        import DAO.PersonAO;
        import DataAccess.DataAccessException;
        import DataAccess.DataBase;
        import Model.Person;
        import org.junit.jupiter.api.AfterEach;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.Assertions;

        import java.sql.Connection;
        import java.util.ArrayList;
        import java.util.List;


public class PersonDAOTest {
    private DataBase db;
    private Person person;
    private Person diffPers;


    @BeforeEach
    public void setUp() {
        this.db = new DataBase();
        this.person = new Person( "Gsutt", "Gabe", "Suttner", "m");
        this.diffPers = new Person("cbush","cory", "bushman", "f" );
    }

    @Test
    public void insertPerson() throws Exception {
        PersonAO pd = null;
        try {
            Connection con = this.db.getPersonConnection();
            pd = new PersonAO();
            pd.addPerson(con, this.person);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            db.closePersonConnection();
        }
        Person newPerson = pd.getPerson(db.getPersonConnection(), this.person.getPersonID());
        Assertions.assertEquals(newPerson, this.person);
    }

    @Test
    public void insertPersons() throws Exception {
        PersonAO pa = null;
        List<Person> persons = new ArrayList<>();
        List<Person> personList = null;
        persons.add(this.person);
        persons.add(this.diffPers);

        try {
            pa = new PersonAO();
            pa.addPersons(db.getPersonConnection(), persons);
            personList = pa.getPersons(db.getPersonConnection());
            db.closePersonConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closePersonConnection();
        }
        Assertions.assertEquals(personList, persons);
    }

    @Test
    public void insertPersonFail() throws Exception {
        PersonAO pd = null;
        try {
            pd = new PersonAO();
            pd.addPerson(this.db.getPersonConnection(), this.person);
            this.db.closePersonConnection();
        } catch (DataAccessException e) {
            db.closePersonConnection();
        }
        //successfully added a person
        boolean failed = false;

        try {
            Connection con = this.db.getPersonConnection();
            pd = new PersonAO();
            pd.addPerson(con, this.person);
        } catch (DataAccessException e) {
            //error gettign the person because its a duplicate
            db.closePersonConnection();
            failed = true;
        }

        Assertions.assertTrue(failed);
    }

    @Test
    public void getPerson() throws DataAccessException {
        PersonAO pd = null;
        try {
            pd = new PersonAO();
            pd.addPerson(this.db.getPersonConnection(), this.person);
            this.db.closePersonConnection();
        } catch (DataAccessException  e) {
            db.closePersonConnection();
        }

        String personID = this.person.getPersonID();
        Person newPerson = null;

        try {
            newPerson = pd.getPerson(db.getPersonConnection(), personID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(newPerson, person);
    }

    @Test
    public void getPersonFail() throws DataAccessException {
        PersonAO pd = null;
        try {
            Connection con = this.db.getPersonConnection();
            pd = new PersonAO();
            pd.addPerson(con, this.person);
        } catch (DataAccessException  e) {
            db.closePersonConnection();
        }
        String personID = this.person.getPersonID();
        Person newPerson = null;

        boolean failed = false;
        try {
            newPerson = pd.getPerson(db.getPersonConnection(), this.diffPers.getPersonID());
        } catch (DataAccessException e) {
            e.printStackTrace();
            failed = true;
        }
        Assertions.assertTrue(failed);
    }


    @Test
    public void updatePerson() throws DataAccessException {
        PersonAO pd = null;
        String newName = "Clonewars";

        try {
            Connection con = this.db.getPersonConnection();
            pd = new PersonAO();
            pd.addPerson(con, this.person);
            db.closePersonConnection();
            this.person.setFirstName(newName);
            pd.updatePerson(db.getPersonConnection(), this.person);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            db.closePersonConnection();
        }
        Person newPerson = pd.getPerson(db.getPersonConnection(), this.person.getPersonID());
        Assertions.assertEquals(newName, newPerson.getFirstName());
    }


    /**
     *
     */
    @Test
    public void clearPersons() {
        PersonAO pa = new PersonAO();
        List<Person> people = null;
        try {
            pa.addPerson(db.getPersonConnection(), person);
            pa.addPerson(db.getPersonConnection(), diffPers);
            this.db.clearPersonsTable();
            people = pa.getPersons(db.getPersonConnection());
            this.db.closePersonConnection();


        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(people.size(), 0);

    }

    @Test
    public void DeletePerson() {
        PersonAO pa = new PersonAO();
        List<Person> people = null;
        try {
            pa.addPerson(db.getPersonConnection(), person);
            pa.addPerson(db.getPersonConnection(), diffPers);
            people = pa.getPersons(db.getPersonConnection());

            this.db.closePersonConnection();
            Assertions.assertEquals(people.size(), 2);

            pa.deletePerson(db.getPersonConnection(), person.getPersonID());
            pa.deletePerson(db.getPersonConnection(), diffPers.getPersonID());
            people = pa.getPersons(db.getPersonConnection());

            this.db.closePersonConnection();
            Assertions.assertEquals(people.size(), 0);


        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.openPersonConnection();
        this.db.clearPersonsTable();
    }
}