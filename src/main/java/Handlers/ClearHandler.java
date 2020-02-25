package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
    /**
     *
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    //make it so that this authorization is correctly implimented.
                    if(authToken.equals("asdf")) {
                        InputStream reqBody = exchange.getRequestBody();
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(HttpsURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                exchange.sendResponseHeaders(HttpsURLConnection.HTTP_BAD_REQUEST, 0);
            }
            exchange.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpsURLConnection.HTTP_INTERNAL_ERROR, 0);

            exchange.getResponseBody().close();

            e.printStackTrace();

        }
    }
}
