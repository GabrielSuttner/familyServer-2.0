package Service;

import DAO.AuthTokenAO;
import DAO.EventAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import RequestResult.LoadRequest;
import RequestResult.LoadResponse;

import java.util.List;

public class LoadService {
    DataBase db = new DataBase();
    public LoadResponse loadData(LoadRequest request) {
        List<User> users = request.getUsers();
        List<Event> events = request.getEvents();
        List<Person> persons = request.getPersons();

        LoadResponse response = new LoadResponse();

        UserAO ua = new UserAO();
        EventAO ea = new EventAO();
        PersonAO pa = new PersonAO();
//        AuthTokenAO aa = new AuthTokenAO();
        try {
//            for(User user : users) {
//                AuthToken token = new AuthToken(user.getUserName());
//                aa.addToken(db.getAuthConnection(), token);
//                db.closeAuthConnection(true);
//            }
            ua.addUsers(db.getUserConnection(), users);
            db.closeUserConnection(true);

            ea.addEvents(db.getEventConnection(), events);
            db.closeEventConnection(true);

            pa.addPersons(db.getPersonConnection(), persons);
            db.closePersonConnection(true);

            response.setMessage("Successfully added " + users.size() +" users, "+ persons.size()+" persons, " +
                    "and "+ events.size() +" events to the database");
            response.setSuccess(true);
        } catch (DataAccessException e) {
            response.setSuccess(false);
            e.printStackTrace();
            response.setMessage(e.getMessage());
            try {
                db.closeAllConnections(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

}
