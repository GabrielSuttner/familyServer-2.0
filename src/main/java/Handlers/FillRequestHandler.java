package Handlers;

import DataAccess.DataBase;
import Response.FillResponse;
import Service.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class FillRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DataBase db = new DataBase();
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                String uri = exchange.getRequestURI().toString();
                int pos = uri.indexOf('/', 2);
                String userAndGeneration = uri.substring(pos + 1);
                pos = userAndGeneration.indexOf('/');
                int generations = 4;

                if(Character.isDigit(userAndGeneration.charAt(pos + 1))){
                   generations = Integer.parseInt(userAndGeneration.substring( pos + 1));
                }
                String userName = userAndGeneration;

                if(userName.contains("/")) {
                    userName = userAndGeneration.substring(0, pos);
                }
                FillService fs = new FillService();
                FillResponse response = null;
                response = fs.fill(userName, generations);

                OutputStream respBody = exchange.getResponseBody();

                if (response.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                }
                Gson gson = new Gson();
                OutputStreamWriter osw = new OutputStreamWriter(respBody);
                osw.write(gson.toJson(response));

                osw.close();
                exchange.close();

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
