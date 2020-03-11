package Server;

import DAO.AuthTokenAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataGenerator.Utility;
import Handlers.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import com.sun.net.httpserver.HttpServer;
import jdk.jshell.execution.Util;

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
    public static Utility util = new Utility();
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
        System.out.println("FamilyMapServer listening on "+ inetAddress.toString() +":" +port);
    }

    private static void registerHandlers(HttpServer server) {
        server.createContext("/user/login", new LoginRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/clear", new ClearRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/fill/", new FillRequestHandler());
        server.createContext("/load", new LoadRequestHandler());
        server.createContext("/person", new PersonRequestHandler());
        server.createContext("/event", new EventRequestHandler());
        server.createContext("/", new FileHandler());

    }


}
