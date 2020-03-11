package Handlers;

import DataAccess.DataBase;
import Model.User;
import Request.LoginRequest;
import Response.LoginResponse;
import Service.LoginService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                User user;
                Reader reader;
                reader = new InputStreamReader(exchange.getRequestBody());

                Gson gson = new Gson();
                user = gson.fromJson(reader, User.class);
                LoginRequest request = new LoginRequest(user.getUserName(), user.getPassword());
                LoginService ls = new LoginService();
                LoginResponse lp = ls.login(request);

                OutputStream respBody = exchange.getResponseBody();
                OutputStreamWriter osw = new OutputStreamWriter(respBody);
                osw.write(gson.toJson(lp));
                if(lp.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                osw.close();
                exchange.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
