package Handlers;

import DataAccess.DataAccessException;
import DataAccess.DataBase;
import RequestResult.PersonResponse;
import Service.DataService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import static Server.Server.getAuthToken;

public class PersonRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DataBase db = new DataBase();
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                String authToken = reqHeaders.get("Authorization").toString();
                authToken = authToken.substring(1, authToken.length() - 1);
                if (!authToken.equals(null)) {
                    Gson gson = new Gson();
                    String uri = exchange.getRequestURI().toString();
                    int pos = uri.indexOf('/', 2);
                    String userID = uri.substring(pos + 1);

                    DataService ds = new DataService();

                    PersonResponse pr = ds.getPerson(userID);

                    String token = getAuthToken(userID);
                    OutputStream respBody = exchange.getResponseBody();

                     if (pr.isSuccess()) {
                         if (!token.equals(null) && token.equals(authToken)) {
                             exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                         } else {
                             pr.setSuccess(false);
                             pr.setMessage("Requested person does not belong to this user");
                             exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                         }
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    }

                    OutputStreamWriter osw = new OutputStreamWriter(respBody);
                    osw.write(gson.toJson(pr));

                    osw.close();
                    exchange.close();
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
