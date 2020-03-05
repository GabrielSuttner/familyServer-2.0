package Server;

import DAO.AuthTokenAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Handlers.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import com.sun.net.httpserver.HttpServer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

//https://docs.oracle.com/en/java/javase/11/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html
public class Server  {
    public static void main(String argv[]){
        try {
            start(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void start(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("FamilyMapServer listening on "+inetAddress.toString() +":" +port);
    }

    private static void registerHandlers(HttpServer server) {
        server.createContext("/user/login", new LoginRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/clear", new ClearRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/fill/", new FillRequestHandler());
        server.createContext("/load", new LoadRequestHandler());
        server.createContext("/person/", new PersonRequestHandler());
        server.createContext("/event/", new EventRequestHandler());
        server.createContext("/", new FileHandler());

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
            db.closeAuthConnection(true);
            if (!at.equals(null)) {
                userID = at.getUserName();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeAuthConnection(false);
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

        if(person == null) {
            return  false;
        }

        isRelated(person, personID);
        return found;
    }

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

    private static Person getPerson(String id) {
        PersonAO pa = new PersonAO();
        DataBase db = new DataBase();
        Person person = null;
        try {
            person = pa.getPerson(db.getPersonConnection(), id);
            db.closePersonConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closePersonConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        return person;
    }

    private static User getUser(String username) {
        UserAO pa = new UserAO();
        DataBase db = new DataBase();
        User user = null;
        try {
            user = pa.getUser(db.getUserConnection(), username);
            db.closeUserConnection(true);
        } catch (DataAccessException e) {
            try {
                db.closeUserConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public static boolean authorized(String token) {
        DataBase db = new DataBase();
        AuthTokenAO ao = new AuthTokenAO();
        try {
            AuthToken at = ao.getTokenByAuthToken(db.getAuthConnection(), token);
            db.closeAuthConnection(true);
            if (at == null) {
                return false;
            }
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeAuthConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public static Set<String> getUserIDSet() {
        return visited;
    }
}
