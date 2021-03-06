package Handlers;

import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Person;
import Model.User;
import Response.PersonResponse;
import Response.PersonsResponse;
import Service.DataService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import static Server.Server.*;

public class PersonRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DataBase db = new DataBase();
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();

                String authToken = reqHeaders.get("Authorization").toString();
                authToken = authToken.substring(1, authToken.length() - 1);

                Gson gson = new Gson();
                OutputStream respBody = exchange.getResponseBody();

                if (util.authorized(authToken)) {
                    String uri = exchange.getRequestURI().toString();
                    int pos = uri.indexOf('/', 2);

                    String userID;
                    if(uri.length() < 9) {
                        userID = util.getUserIDViaToken(authToken);
                        UserAO ua = new UserAO();
                        String personID = null;

                        try {
                            User user = ua.getUser(db.getUserConnection(), userID);
                            db.closeUserConnection();
                            personID = user.getPersonID();

                        } catch (DataAccessException e) {
                            e.printStackTrace();
                            try {
                                db.closeUserConnection();
                            } catch (DataAccessException ex) {
                                ex.printStackTrace();
                            }
                        }

                        DataService ds = new DataService();

                        PersonsResponse pr = ds.getPeople(personID);
                        if (pr.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                        }

                        OutputStreamWriter osw = new OutputStreamWriter(respBody);
                        osw.write(gson.toJson(pr));

                        osw.close();

                    } else {
                        String personID = uri.substring(pos + 1);

                        DataService ds = new DataService();
                        PersonResponse pr = new PersonResponse();
                        String id = util.getUserIDViaToken(authToken);
                        if (util.isAssociated(personID, id)) {

                            pr = ds.getPerson(personID);
                            if (pr.isSuccess()) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                            }
                        } else {
                            pr.setSuccess(false);
                            pr.setMessage("error: Requested person does not belong to this user");
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        OutputStreamWriter osw = new OutputStreamWriter(respBody);
                        osw.write(gson.toJson(pr));

                        osw.close();
                    } exchange.close();
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    OutputStreamWriter osw = new OutputStreamWriter(respBody);
                    PersonResponse pr = new PersonResponse();
                    pr.setSuccess(false);
                    pr.setMessage("error: Requested person does not belong to this user");
                    osw.write(gson.toJson(pr));

                    osw.close();
                    exchange.close();
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            try {
                db.closeAllConnections();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
