package Service;

import DAO.EventAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import HelperClass.Utility;
import Model.Event;
import Model.Person;
import Model.User;
import Response.FillResponse;

import java.util.List;

import static Server.Server.*;

/**
 * fill the database with fake data that is associated with a given ID.
 */
public class FillService {
    DataBase db = new DataBase();

    /**
     * fill the database, starting at given username and going back x generations
     * @param userName
     * @param generations
     * @return
     */
    public FillResponse fill(String userName, int generations) {
        FillResponse response = new FillResponse();
        UserAO ua = new UserAO();
        EventAO ea = new EventAO();
        PersonAO pa = new PersonAO();
        User user = null;
        int beforeP = 0, beforeE = 0, afterE = 0, afterP = 0;
        List<Person> people = null;
        List<Event> events = null;
        try {
            user = ua.getUser(db.getUserConnection(), userName);
            db.closeUserConnection();

            if(deleteRelativesHelper(user)) {
                people = pa.getPersons(db.getPersonConnection());
                db.closePersonConnection();
                beforeP = people.size();

                events = ea.getEvents(db.getEventConnection());
                db.closeEventConnection();
                beforeE = events.size();

                util.generateDataHelper(util.getPerson(user.getPersonID()), generations);

                events = ea.getEvents(db.getEventConnection());
                db.closeEventConnection();
                afterE = events.size();

                people = pa.getPersons(db.getPersonConnection());
                db.closePersonConnection();
                afterP = people.size();

                response.setMessage("Successfully added " + (afterP - beforeP + 1)+ " persons and " + (afterE - beforeE) + " events to the database.");
                response.setSuccess(true);
            } else throw new DataAccessException("Couldn't delete persons");

        } catch (DataAccessException e) {
            try {
                db.closeAllConnections();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return response;
    }

    /**
     * deletes all current relatives for a given user.
     * @param user
     * @return
     * @throws DataAccessException
     */
    private boolean deleteRelativesHelper(User user) throws DataAccessException {
        String personID = user.getPersonID();
        Utility util = new Utility();

        Person p = util.getPerson(personID);
        PersonAO pa = new PersonAO();

        pa.deletePersonByAssociated(db.getPersonConnection(), p.getUsername());
        pa.addPerson(db.getPersonConnection(), p);
        db.closePersonConnection();
        return true;
    }


}
