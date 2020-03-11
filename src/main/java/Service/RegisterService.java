package Service;

import DAO.AuthTokenAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.RegisterRequest;
import Response.RegisterResponse;

import static Server.Server.util;

public class RegisterService {
    /**
     * registering a new account will log you in, and generate 4 generations of ancestors for the new user.
     * @param r
     * @return
     */
    public RegisterResponse register(RegisterRequest r) {
        DataBase db = new DataBase();

        UserAO ua = new UserAO();
        PersonAO pa = new PersonAO();
        AuthTokenAO aa = new AuthTokenAO();

        RegisterResponse response = new RegisterResponse();

        Person person = null;
        User user = null;
        AuthToken token = null;
        try {
            person = new Person(r.getUserName(),
                    r.getFirstName(),
                    r.getLastName(),
                    r.getGender());

            user = new User(r.getUserName(),
                    r.getPassword(),
                    r.getFirstName(),
                    r.getEmail(),
                    r.getLastName(),
                    r.getGender(),
                    person.getPersonID());

            token = new AuthToken(r.getUserName());

            pa.addPerson(db.getPersonConnection(), person);
            db.closePersonConnection();

            ua.addUser(db.getUserConnection(), user);
            db.closeUserConnection();

            aa.addToken(db.getAuthConnection(), token);
            db.closeAuthConnection();

            util.generateDataHelper(person, 4);

            response.setSuccess(true);
            response.setAuthToken(token.getTokenID());
            response.setUserName(user.getUserName());
            response.setPersonID(person.getPersonID());

        } catch (DataAccessException e) {
            response.setSuccess(false);
            response.setMessage("error: "+ e.getMessage());
        }

        return response;
    }
}
