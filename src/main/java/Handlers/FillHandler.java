package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

public class FillHandler implements HttpHandler {
    /**
     *
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

        } else {
            exchange.sendResponseHeaders(HttpsURLConnection.HTTP_BAD_REQUEST, 0);
        }
    }
}
