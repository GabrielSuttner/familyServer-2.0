package HelperClass;

import DAO.AuthTokenAO;
import DAO.EventAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.*;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Utility {
    Random rand = new Random();
    DataHolder dh = new DataHolder();
    String[] maleNames = dh.getMaleNames();
    String[] femaleNames = dh.getFemaleNames();
    String[] surNames = dh.getSurNames();
    List<Location> locations = null;
    int year = 2000;

    /**
     * Helper to get gerateData started.
     * @param person
     * @param generations
     */
    public void generateDataHelper(Person person, int generations){
        person.setFatherID(null);
        person.setMotherID(null);
        person.setSpouseID(null);
        dh.buildLocation();
        this.locations = dh.getLocations();
        addEvent(generateBirth(person));
        generateData(person, generations);

    }

    /**
     * generate data for a given person down x generations
     * @param person
     * @param generations
     */
    private void generateData(Person person, int generations) {
        this.year = this.year - 35;
        if(generations == 0) {
            this.year = this.year + 35;
            return;
        }
        generateParents(person);
        Person dad = getPerson(person.getFatherID());
        generateData(dad, (generations - 1));
        Person mom = getPerson(person.getMotherID());
        generateData(mom, (generations - 1));
        this.year = this.year + 35;
        return;
    }

    /**
     * Generate two parents for a given person.
     * @param person
     */
    private void generateParents(Person person) {
        String father = person.getFatherID();
        String mother = person.getMotherID();
        Person dad = null;
        Person mom = null;
        if(father == null) {
            String fName = getMaleName();
            String lName = getLastName();
            dad = new Person(person.getUsername(), fName, lName, "m");
        }
        if(mother == null) {
            String fName = getFemaleName();
            String lName = getLastName();
            mom = new Person(person.getUsername(), fName, lName, "f ");
        }
        dad.setSpouseID(mom.getPersonID());
        mom.setSpouseID(dad.getPersonID());
        person.setFatherID(dad.getPersonID());
        person.setMotherID(mom.getPersonID());
        updatePerson(person);
        addPerson(dad);
        addPerson(mom);
        generateEvents(dad, mom);

    }

    /**
     * functions to get random name data for individuals.
     * @return
     */
    private String getLastName(){
        int random = rand.nextInt(surNames.length);
        return surNames[random];
    }
    private String getMaleName() {
        int random = rand.nextInt(maleNames.length);
        return maleNames[random];
    }
    private String getFemaleName() {
        int random = rand.nextInt(femaleNames.length);
        return femaleNames[random];
    }

    /**
     * generate birth, marriage and death for two individuals
     * @param dad
     * @param mom
     */
    private void generateEvents(Person dad, Person mom) {
        Event birth = generateBirth(dad);
        addEvent(birth);
        birth = generateBirth(mom);
        addEvent(birth);
        generateMarriage(dad, mom);
        addEvent(generateDeath(dad));
        addEvent(generateDeath(mom));
    }

    /**
     * generates a birth for one person
     * @param person
     * @return
     */
    private Event generateBirth(Person person) {
        int random = rand.nextInt(12);
        int year = random + this.year % 2 == 0 ? this.year + random : this.year - random;
        int size = this.locations.size();
        Location location = (this.locations.size() > 0) ?
                this.locations.get(rand.nextInt(size)) :
                null;
        Event event = new Event(person.getUsername(), person.getPersonID(),
                (float)location.getLatitude(), (float) location.getLongitude(), location.getCountry(),
                location.getCity(), "Birth", year);

        return event;
    }

    /**
     * generates a marriage between two people
     * @param dad
     * @param mom
     */
    private void generateMarriage(Person dad, Person mom) {
        int random = rand.nextInt(15);
        int year = this.year + random + 15;
        int size = this.locations.size();
        Location location = (this.locations.size() > 0) ?
                this.locations.get(rand.nextInt(size)) :
                null;
        addEvent(new Event(dad.getUsername(), dad.getPersonID(),
                (float)location.getLatitude(), (float)location.getLongitude(), location.getCountry(),
                location.getCity(), "Marriage", year));
        addEvent(new Event(mom.getUsername(), mom.getPersonID(),
                (float) location.getLatitude(),(float) location.getLongitude(), location.getCountry(),
                location.getCity(), "Marriage", year));
    }

    /**
     * generates a death for a given person.
     * @param person
     * @return
     */
    private Event generateDeath(Person person) {
        int random = rand.nextInt(20);
        int year = random + this.year + 50;
        int size = this.locations.size();

        Location location = (this.locations.size() > 0) ?
                this.locations.get(rand.nextInt(size)) :
                null;

        Event event = new Event(person.getUsername(), person.getPersonID(),
                (float)location.getLatitude(), (float)location.getLongitude(), location.getCountry(),
                location.getCity(), "Death", year);

        return event;
    }
    /**
     * Static function to be used throughout the program
     * @param event
     */
    private void addEvent(Event event) {
        DataBase db = new DataBase();
        EventAO ea = new EventAO();
        try {
            ea.addEvent(db.getEventConnection(), event);
            db.closeEventConnection();
        } catch (DataAccessException e) {
            try {
                db.closeEventConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    /**
     * Static function to be used throughout the program
     * @param person
     */
    private void addPerson(Person person) {
        DataBase db = new DataBase();
        PersonAO ea = new PersonAO();
        try {
            ea.addPerson(db.getPersonConnection(), person);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * Static function to be used throughout the program
     * @param person
     */
    private void updatePerson(Person person) {
        DataBase db = new DataBase();
        PersonAO ea = new PersonAO();
        try {
            ea.updatePerson(db.getPersonConnection(), person);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     *
     * @param authToken
     * @return userID associated with authToken
     */
    public static String getUserIDViaToken(String authToken) {
        String userID = null;
        DataBase db = new DataBase();
        AuthTokenAO ao = new AuthTokenAO();
        try {
            AuthToken at = ao.getTokenByAuthToken(db.getAuthConnection(), authToken);
            db.closeAuthConnection();
            if (!at.equals(null)) {
                userID = at.getUserName();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeAuthConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return userID;
    }

    /**
     *
     * @param userID
     * @return True if the userID is associated with the authToken
     */
    static boolean found = false;
    static Set<String> visited = new TreeSet<>();

    /**
     * used as a wrapper for the isRelated function
     * @param personID
     * @param authToken
     * @return
     */
    public static boolean isRelatedHelper(String personID, String authToken) {
        visited.clear();
        found = false;
        //user string is the person that requested the person
        String user = getUserIDViaToken(authToken);
        if (user == null) {
            return false;
        }
        User user1 = getUser(user);

        if (user.equals(personID)) {
            return true;
        }
        Person person = getPerson(user1.getPersonID());
        visited.add(person.getPersonID());
        if(person == null) {
            return  false;
        }

        isRelated(person, personID);
        return found;
    }

    /**
     * checks to see if Person is related to the requested person.
     * @param person
     * @param requestedPersonID
     */
    public static void isRelated(Person person, String requestedPersonID) {
        if(requestedPersonID == null) {
            requestedPersonID = "temp";
            found = false;
            visited.clear();
        }
        if(found) return;
        if(person.getPersonID() == requestedPersonID) {
            found = true;
            return;
        }

        if(person.getFatherID()!= null) {
            if(requestedPersonID.equals(person.getFatherID())) {
                found = true;
                return;
            }
            if(visited.add(person.getFatherID())){
                Person p = getPerson(person.getFatherID());
                isRelated(p, requestedPersonID);
            }
        }

        if(found) return;
        if(person.getMotherID() != null) {
            if(requestedPersonID.equals(person.getMotherID())) {
                found = true;
                return;
            }
            if(visited.add(person.getMotherID())) {
                Person p = getPerson(person.getMotherID());
                isRelated(p, requestedPersonID);
            }
        }

        if(found) return;
        if(person.getSpouseID() != null) {
            if(requestedPersonID.equals(person.getSpouseID())) {
                found = true;
                return;
            }
            if(visited.add(person.getSpouseID())) {
                Person p = getPerson(person.getSpouseID());
                isRelated(p, requestedPersonID);
            }
        }
        return;
    }

    /**
     * returns a person from the person ID
     * @param id
     * @return Person
     */
    public static Person getPerson(String id) {
        PersonAO pa = new PersonAO();
        DataBase db = new DataBase();
        Person person = null;
        try {
            person = pa.getPerson(db.getPersonConnection(), id);
            db.closePersonConnection();
        } catch (DataAccessException e) {
            try {
                db.closePersonConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        return person;
    }

    /**
     * get user from username
     * @param username
     * @return
     */
    public static User getUser(String username) {
        UserAO pa = new UserAO();
        DataBase db = new DataBase();
        User user = null;
        try {
            user = pa.getUser(db.getUserConnection(), username);
            db.closeUserConnection();
        } catch (DataAccessException e) {
            try {
                db.closeUserConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        return user;
    }

    /**
     * checks first parameter to see if associated with second parameter.
     * @param relativePersonID
     * @param associatedUsername
     * @return
     */
    public static boolean isAssociated(String relativePersonID, String associatedUsername) {
        Person p = getPerson(relativePersonID);
        if(p.getUsername().equals(associatedUsername)) return true;
        return false;
    }

    /**
     * checks the token against the database.
     * @param token
     * @return
     */
    public static boolean authorized(String token) {
        DataBase db = new DataBase();
        AuthTokenAO ao = new AuthTokenAO();
        try {
            AuthToken at = ao.getTokenByAuthToken(db.getAuthConnection(), token);
            db.closeAuthConnection();
            if (at == null) {
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            try {
                db.closeAuthConnection();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
}
