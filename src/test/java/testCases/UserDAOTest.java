package testCases;

import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Person;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//insert
//clear
//retreive
public class UserDAOTest {
    private DataBase db;
    private User User;
    private User diffUser;


    @BeforeEach
    public void setUp() {
        this.db = new DataBase();
        this.User = new User("Gabriel", "pig", "gsuttner@gmail.com", "gabe", "Suttner", "M", "afasasdfasdfwerasdf");
        this.diffUser = new User("cory", "doll", "gsuttner@gmail.com", "gabe", "Suttner", "M", "afasasdfasdfwerasdf");
    }

    @Test
    public void insertUser() throws Exception {
        UserAO pd = null;
        try {
            Connection con = this.db.getUserConnection();
            pd = new UserAO();
            pd.addUser(con, this.User);
        } catch (DataAccessException e) {
            db.closeUserConnection();
        }
        List<User> people = pd.getUsers(db.getUserConnection());
        User compare = null;
        for(User p : people) {
            compare = p;
        }
        db.closeUserConnection();
        Assertions.assertEquals(1, people.size());
    }

    @Test
    public void addUsers() throws Exception {
        List<User> users = new ArrayList<>();
        List<User> userList = null;
        users.add(this.diffUser);
        users.add(this.User);
        UserAO ua = null;
        try {
            ua = new UserAO();
            ua.addUsers(this.db.getUserConnection(), users);
            userList = ua.getUsers(this.db.getUserConnection());
            db.closeUserConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeUserConnection();
        }
        Assertions.assertEquals(users, userList);
    }

    @Test
    public void insertFail() throws DataAccessException {
        UserAO pd = null;
        boolean failed = false;
        try {
            Connection con = this.db.getUserConnection();
            pd = new UserAO();
            pd.addUser(con, this.User);
            pd.addUser(con, this.User);
            db.closeUserConnection();
        } catch (DataAccessException e) {
            db.closeUserConnection();
            e.printStackTrace();
            failed = true;
        }

        //check to make sure that no duplicate User added.
        Assertions.assertTrue(failed);
    }

    @Test
    public void getUser() throws DataAccessException {
        UserAO pd = null;
        User s = null;
        try {
            pd = new UserAO();
            pd.addUser(this.db.getUserConnection(), this.User);
            s = pd.getUser(this.db.getUserConnection(), this.User.getUserName());
            this.db.closeUserConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeUserConnection();
        }
        Assertions.assertEquals(s, User);
    }

    @Test
    public void getUserFail() throws DataAccessException {
        UserAO pd = null;
        try {
            Connection con = this.db.getUserConnection();
            pd = new UserAO();
            pd.addUser(con, this.User);
        } catch (DataAccessException  e) {
            db.closeUserConnection();
        }
        String personID = this.User.getUserName();
        User newPerson = null;

        boolean failed = false;
        try {
            newPerson = pd.getUser(db.getUserConnection(), this.diffUser.getUserName());
        } catch (DataAccessException e) {
            e.printStackTrace();
            failed = true;
        } finally {
            db.closeUserConnection();
        }
        Assertions.assertTrue(failed);
    }

    /**
     * @throws SQLException
     */
    @Test
    public void clearUser() throws SQLException {
        UserAO pa = new UserAO();
        try {
            Connection con = db.getUserConnection();
            pa.addUser(con, User);
            pa.addUser(con, diffUser);
            db.clearUserTables();
            List<User> people = pa.getUsers(db.getUserConnection());
            this.db.closeUserConnection();

            Assertions.assertTrue(people.size() == 0);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }



    @AfterEach
    public void tearDown() throws DataAccessException {
        this.db.openUserConnection();
        this.db.clearUserTables();
    }
}