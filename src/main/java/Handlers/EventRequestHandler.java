package Handlers;

import DAO.EventAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.Event;
import Model.User;
import Response.EventResponse;
import Response.EventsResponse;
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

public class EventRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DataBase db = new DataBase();
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                String authToken = reqHeaders.get("Authorization").toString();
                authToken = authToken.substring(1, authToken.length() - 1);

                EventResponse er = new EventResponse();
                OutputStream respBody = exchange.getResponseBody();
                Gson gson = new Gson();
                //validate token
                if (util.authorized(authToken)) {

                    String uri = exchange.getRequestURI().toString();
                    int pos = uri.indexOf('/', 2);

                    if(uri.length() < 8) {
                            String userID = util.getUserIDViaToken(authToken);
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
                            EventsResponse pr = ds.getEvents(personID);
                            if (pr.isSuccess()) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            }

                            OutputStreamWriter osw = new OutputStreamWriter(respBody);
                            osw.write(gson.toJson(pr));

                            osw.close();

                    } else {

                        String eventID = uri.substring(pos + 1);

                        DataService ds = new DataService();
                        EventAO ea = new EventAO();
                        Event event = null;
                        try {
                            event = ea.getEvent(db.getEventConnection(), eventID);
                            db.closeEventConnection();
                        } catch (DataAccessException e) {
                            e.printStackTrace();
                            try {
                                db.closeEventConnection();
                            } catch (DataAccessException ex) {
                                ex.printStackTrace();
                            }
                            er.setMessage("error: User not associated with event");
                            er.setSuccess(false);
                        }
                        if (util.isRelatedHelper(event.getPersonID(), authToken)) {
                            er = ds.getEvent(eventID);
                            if (er.isSuccess()) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                            }
                        } else {
                            er.setSuccess(false);
                            er.setMessage("error: Requested event does not belong to this user");
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                        OutputStreamWriter osw = new OutputStreamWriter(respBody);
                        osw.write(gson.toJson(er));

                        osw.close();

                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                    EventsResponse pr = new EventsResponse();
                    pr.setSuccess(false);
                    pr.setMessage("error: Requested event does not belong to this user");
                    OutputStreamWriter osw = new OutputStreamWriter(respBody);
                    osw.write(gson.toJson(pr));
                    osw.close();
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
        exchange.close();
    }

}
