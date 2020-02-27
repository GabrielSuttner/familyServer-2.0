package Handlers;

import DAO.AuthTokenAO;
import DAO.PersonAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import Model.Person;
import Model.User;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class LoginRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DataBase db = new DataBase();
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                String username = (String) exchange.getAttribute("username");
                String password = (String) exchange.getAttribute("password");
                UserAO ua = new UserAO();
                User user = ua.getUser(db.getUserConnection(), username);
                db.closeUserConnection(true);
                AuthTokenAO ao = null;
                if(user.checkPassword(password)) {
                     ao = new AuthTokenAO();
                    AuthToken token = new AuthToken(username);
                    ao.addToken(db.getAuthConnection(), token);
                    db.closeAuthConnection(true);


                } else {
                    //return a reject
                }

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }

        } catch (IOException e){
            e.printStackTrace();
        } catch (DataAccessException e) {
            System.out.println("Error: Couldn't establish connection to user Database.");
            try {
                db.closeAllConnections(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}
