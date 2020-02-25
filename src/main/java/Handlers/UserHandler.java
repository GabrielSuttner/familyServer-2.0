package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static java.nio.file.Files.readString;

public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

            InputStream reqBody = exchange.getRequestBody();

            String reqData = readString((Path) reqBody);

            System.out.println(reqBody);

            exchange.sendResponseHeaders(HttpsURLConnection.HTTP_OK, 0);

        } else {
            exchange.sendResponseHeaders(HttpsURLConnection.HTTP_BAD_REQUEST, 0);
        }
    }

}