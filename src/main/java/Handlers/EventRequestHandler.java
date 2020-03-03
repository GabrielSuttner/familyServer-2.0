package Handlers;

import DAO.AuthTokenAO;
import DAO.EventAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import Model.Event;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class EventRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DataBase db = new DataBase();
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String userID = reqHeaders.getFirst("username");
                    AuthTokenAO at = new AuthTokenAO();
                    AuthToken token = null;
                    db.openAuthConnection();
                    token = at.getToken(db.getAuthConnection(), userID);
                    if (token.equals(authToken)) {

                        db.openEventConnection();
                        EventAO ea = new EventAO();
                        Event event = ea.getEvent(db.getEventConnection(), userID);
                       /*String respData =
                                "{ \"person\": {" +
                                        "{ \"person_id\": "+ event.getPersonID() +"\"," +
                                        "{ \"username\": "+ event.get() +"\"," +
                                        "{ \"first_name\": "+ event.getFirstName() +"\"," +
                                        "{ \"last_name\": "+ event.getLastName() +"\"," +
                                        "{ \"gender\": "+ event.getGender() +"\"," +
                                        "{ \"father_id\": "+ event.getFatherID() +"\"," +
                                        "{ \"mother_id\": "+ event.getMotherID() +"\"," +
                                        "{ \"spouse_id\": "+ event.getSpouseID() +"\"" +
                                        "}";*/
                        db.closeAllConnections(true);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        OutputStream respBody = exchange.getResponseBody();

                        //writeString(respData, respBody);

                        respBody.close();

                    } else {
                        db.closeAuthConnection(true);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException  | DataAccessException e) {
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
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
