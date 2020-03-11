package Service;

import DAO.EventAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Response.LoadResponse;

import java.util.List;

public class LoadService {
    DataBase db = new DataBase();

    /**
     * Load given data into database.
     * @param request
     * @return
     */
    public LoadResponse loadData(LoadRequest request) {
        List<User> users = request.getUsers();
        List<Event> events = request.getEvents();
        List<Person> persons = request.getPersons();

        LoadResponse response = new LoadResponse();

        UserAO ua = new UserAO();
        EventAO ea = new EventAO();
        PersonAO pa = new PersonAO();
        try {
            db.clearTables();

            ua.addUsers(db.getUserConnection(), users);
            db.closeUserConnection();

            ea.addEvents(db.getEventConnection(), events);
            db.closeEventConnection();

            pa.addPersons(db.getPersonConnection(), persons);
            db.closePersonConnection();


            response.setMessage("Successfully added " + users.size() +" users, "+ persons.size()+" persons, " +
                    "and "+ events.size() +" events to the database");
            response.setSuccess(true);
        } catch (DataAccessException e) {
            response.setSuccess(false);
            e.printStackTrace();
            response.setMessage(e.getMessage());
            try {
                db.closeAllConnections();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

}
