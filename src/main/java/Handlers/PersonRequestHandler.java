package Handlers;

import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
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

                if (authorized(authToken)) {
                    Gson gson = new Gson();
                    String uri = exchange.getRequestURI().toString();
                    int pos = uri.indexOf('/', 2);

                    //run the code for /person/ api
                    if(uri.length() < 9) {
                        String userID = getUserIDViaToken(authToken);
                        UserAO ua = new UserAO();
                        String personID = null;

                        try {
                            User user = ua.getUser(db.getUserConnection(), userID);
                            db.closeUserConnection(true);
                            personID = user.getPersonID();
                        } catch (DataAccessException e) {
                            e.printStackTrace();
                            try {
                                db.closeUserConnection(true);
                            } catch (DataAccessException ex) {
                                ex.printStackTrace();
                            }
                        }

                        DataService ds = new DataService();
                        OutputStream respBody = exchange.getResponseBody();

                        PersonsResponse pr = ds.getPeople(personID);
                        if (pr.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                        }

                        OutputStreamWriter osw = new OutputStreamWriter(respBody);
                        osw.write(gson.toJson(pr));

                        osw.close();
                        exchange.close();

                        //run code for /person/*
                    } else {
                        String userID = uri.substring(pos + 1);

                        DataService ds = new DataService();
                        PersonResponse pr = new PersonResponse();
                        OutputStream respBody = exchange.getResponseBody();

                        if (isRelatedHelper(userID, authToken)) {
                            pr = ds.getPerson(userID);
                            if (pr.isSuccess()) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                            }
                        } else {
                            pr.setSuccess(false);
                            pr.setMessage("Requested person is not associated to this user");
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                        }

                        OutputStreamWriter osw = new OutputStreamWriter(respBody);
                        osw.write(gson.toJson(pr));

                        osw.close();
                        exchange.close();
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            try {
                db.closeAllConnections(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
