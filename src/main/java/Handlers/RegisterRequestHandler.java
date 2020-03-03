package Handlers;
import Model.User;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResponse;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                User user = null;
                Reader reader = null;
                reader = new InputStreamReader(exchange.getRequestBody());

                Gson gson = new Gson();
                user = gson.fromJson(reader, User.class);

                RegisterRequest request = new RegisterRequest(
                        user.getUserName(), user.getPassword(),
                        user.getEmail(), user.getFirstName(),
                        user.getLastName(), user.getGender());

                RegisterService ls = new RegisterService();
                RegisterResponse lp = ls.register(request);

                OutputStream respBody = exchange.getResponseBody();
                OutputStreamWriter osw = new OutputStreamWriter(respBody);

                osw.write(gson.toJson(lp));

                if(lp.isSuccess()) {
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
