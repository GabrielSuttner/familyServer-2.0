package Handlers;

import Response.ClearResponse;
import Service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class ClearRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Gson gson = new Gson();
                OutputStream respBody = exchange.getResponseBody();
                OutputStreamWriter osw = new OutputStreamWriter(respBody);

                ClearService cl = new ClearService();
                ClearResponse response = cl.clear();
                osw.write(gson.toJson(response));

                if(response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                }

                osw.close();
                exchange.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }

        } catch (IOException e) {
            System.out.println("Error: Couldn't establish connection to userDatabase.");
        }
    }
}
