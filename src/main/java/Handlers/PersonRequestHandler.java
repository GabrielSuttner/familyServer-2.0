package Handlers;

import DAO.AuthTokenAO;
import DAO.PersonAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import Model.Person;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class PersonRequestHandler implements HttpHandler {
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

                        db.openPersonConnection();
                        PersonAO pa = new PersonAO();
                        Person person = pa.getPerson(db.getPersonConnection(), userID);
                        String respData =
                                "{ \"person\": {" +
                                        "{ \"person_id\": "+ person.getPersonID() +"\"," +
                                        "{ \"username\": "+ person.getUsername() +"\"," +
                                        "{ \"first_name\": "+ person.getFirstName() +"\"," +
                                        "{ \"last_name\": "+ person.getLastName() +"\"," +
                                        "{ \"gender\": "+ person.getGender() +"\"," +
                                        "{ \"father_id\": "+ person.getFatherID() +"\"," +
                                        "{ \"mother_id\": "+ person.getMotherID() +"\"," +
                                        "{ \"spouse_id\": "+ person.getSpouseID() +"\"" +
                                        "}";
                        db.closeAllConnections(true);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        OutputStream respBody = exchange.getResponseBody();

                        writeString(respData, respBody);

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
