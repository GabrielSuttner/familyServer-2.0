package Handlers;

import Model.Event;
import Model.Person;
import Model.User;
import javax.json.Json;
import javax.json.stream.JsonParser;

import RequestResult.LoadRequest;
import RequestResult.LoadResponse;
import Service.LoadService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class LoadRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("POST")) {
                List<User> users = null;
                List<Person> persons = null;
                List<Event> events = null;
                Reader reader1 = new InputStreamReader(exchange.getRequestBody());
                Reader reader2 = new InputStreamReader(exchange.getRequestBody());
                Reader reader = new InputStreamReader(exchange.getRequestBody());

                try {
                    persons = parsePersons(reader);
                    users = parseUsers(reader1);
                    events = parseEvents(reader2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LoadRequest request = new LoadRequest(users, persons, events);
                LoadService loadService = new LoadService();
                LoadResponse response = loadService.loadData(request);


                OutputStream respBody = exchange.getResponseBody();
                OutputStreamWriter osw = new OutputStreamWriter(respBody);

                Gson gson = new Gson();
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

    private List<User> parseUsers(Reader reader) throws IOException {
        List<User> users = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(reader);

        JsonParser parser = Json.createParser(bufferedReader);

        String username = null, password = null, email = null,
                firstname = null, lastname = null, gender = null, personID = null;
        boolean fullfilled = false;
        while(parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if(event.equals(JsonParser.Event.KEY_NAME)) {
                String key = parser.getString();
                switch (key.toUpperCase()) {
                    case "USERS":
                        fullfilled = true;
                        break;
                    case "USERNAME":
                        username = processAttribute(parser);
                        break;
                    case "PASSWORD":
                        password = processAttribute(parser);
                        break;
                    case "EMAIL":
                        email = processAttribute(parser);
                        break;
                    case "FIRSTNAME":
                        firstname = processAttribute(parser);
                        break;
                    case "LASTNAME":
                        lastname = processAttribute(parser);
                        break;
                    case "GENDER":
                        gender = processAttribute(parser);
                        break;
                    case "PERSONID":
                        personID = processAttribute(parser);
                        break;
                    default:
                        if(fullfilled) {
                            bufferedReader.close();
                            return users;
                        }
                }

            } else if(event.equals(JsonParser.Event.END_OBJECT) && fullfilled) {
                users.add(new User(username,password,email,firstname,lastname,gender,personID));
                username = null;
                password = null;
                email = null;
                firstname = null;
                lastname = null;
                gender = null;
                personID = null;
                event = parser.next();
            }
        }
        bufferedReader.close();
        return users;
    }

    private List<Person> parsePersons(Reader reader) throws IOException {
        List<Person> persons = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(reader);
        JsonParser parser = Json.createParser(bufferedReader);

        String firstname = null, lastname = null, gender = null,
                personID = null, spouseID = null, fatherID = null,
                motherID = null, associated = null;

        boolean fullfilled = false;
        while(parser.hasNext()) {
            JsonParser.Event event = parser.next();

            if(event.equals(JsonParser.Event.KEY_NAME)) {
                String key = parser.getString();
                switch (key.toUpperCase()) {
                    case "PERSONS":
                        fullfilled = true;
                        break;
                    case "FIRSTNAME":
                        firstname = processAttribute(parser);
                        break;
                    case "LASTNAME":
                        lastname = processAttribute(parser);
                        break;
                    case "GENDER":
                        gender = processAttribute(parser);
                        break;
                    case "PERSONID":
                        personID = processAttribute(parser);
                        break;
                    case "SPOUSEID":
                        spouseID = processAttribute(parser);
                        break;
                    case "FATHERID":
                        fatherID = processAttribute(parser);
                        break;
                    case "MOTHERID":
                        motherID = processAttribute(parser);
                        break;
                    case "ASSOCIATEDUSERNAME":
                        associated = processAttribute(parser);
                        break;
                    default:
                        if(fullfilled) {
                            bufferedReader.close();
                            bufferedReader = null;
                            return persons;
                        }
                }

            } else if(event.equals(JsonParser.Event.END_OBJECT) && fullfilled) {
                persons.add(new Person(firstname, lastname, gender, personID, spouseID, fatherID, motherID, associated));
                firstname = null;
                lastname = null;
                gender = null;
                personID = null;
                spouseID = null;
                fatherID = null;
                motherID = null;
                associated = null;
                event = parser.next();
            }
        }
        bufferedReader.close();
        return persons;
    }

    private List<Event> parseEvents(Reader reader) {
        List<Event> events = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(reader);

        JsonParser parser = Json.createParser(bufferedReader);

        String personID = null,  associated = null, eventType = null,
        city = null, country = null, eventID = null;

        float longitude = 0.0f, latitude = 0.0f;

        int year = 0;

        boolean fullfilled = false;
        while(parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if(event.equals(JsonParser.Event.KEY_NAME)) {
                String key = parser.getString();
                switch (key.toUpperCase()) {
                    case "EVENTS":
                        fullfilled = true;
                        break;
                    case "EVENTTYPE":
                        eventType = processAttribute(parser);
                        break;
                    case "PERSONID":
                        personID = processAttribute(parser);
                        break;
                    case "CITY":
                        city = processAttribute(parser);
                        break;
                    case "COUNTRY":
                        country = processAttribute(parser);
                        break;
                    case "LATITUDE":
                        latitude = Float.parseFloat(processAttribute(parser));
                        break;
                    case "LONGITUDE":
                        longitude = Float.parseFloat(processAttribute(parser));
                        break;
                    case "YEAR":
                        year = Integer.parseInt(processAttribute(parser));
                        break;
                    case "ASSOCIATEDUSERNAME":
                        associated = processAttribute(parser);
                        break;
                    case "EVENTID":
                        eventID = processAttribute(parser);
                        break;
                    default:
                        if(fullfilled)
                            return events;
                }

            } else if(event.equals(JsonParser.Event.END_OBJECT) && fullfilled) {
                events.add(new Event(associated, personID, latitude, longitude, country, city, eventType, year));
                associated = null;
                personID = null;
                latitude = 0.0f;
                longitude = 0.0f;
                country = null;
                city = null;
                eventType = null;
                year = 0;
                event = parser.next();
            }
        }

        return events;
    }

    private String processAttribute(JsonParser parser) {
        parser.next();
        return parser.getString();
    }
}
