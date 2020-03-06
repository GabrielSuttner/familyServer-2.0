package Handlers;

import Model.Event;
import Model.ListContainer;
import Model.Person;
import Model.User;

import Request.LoadRequest;
import Response.LoadResponse;
import Service.LoadService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;

public class LoadRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                List<User> users = null;
                List<Person> persons = null;
                List<Event> events = null;
                Reader reader = new InputStreamReader(exchange.getRequestBody());
                Gson gson = new Gson();
                ListContainer container = null;

                container = gson.fromJson(reader, ListContainer.class);
                users = container.getUsers();
                persons = container.getPersons();
                events = container.getEvents();

                LoadRequest request = new LoadRequest();
                request.setEvents(events);
                request.setPersons(persons);
                request.setUsers(users);
                LoadService loadService = new LoadService();
                LoadResponse response = loadService.loadData(request);


                OutputStream respBody = exchange.getResponseBody();
                OutputStreamWriter osw = new OutputStreamWriter(respBody);

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
