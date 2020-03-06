package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("GET")){
                String path = exchange.getRequestURI().toString();
                if(path == null) {
                    path = "web";
                } else {
                    path = "web" + path;
                }

                //check to see if it wants index.
                String finalPath = finalizePath(path);
                System.out.println(finalPath);
                File file = null;
                Path filePath = FileSystems.getDefault().getPath(finalPath);

                file = new File(filePath.toString());
                OutputStream respBody = exchange.getResponseBody();
                if(finalPath.contains("404")){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                try {
                    Files.copy(file.toPath(), respBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exchange.close();
            } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String finalizePath(String path) {

        System.out.println("in checkpath uri: " +path);
        String newPath = null;
        if("web/".equals(path)) {
            newPath = "web/index.html";
        } else if(path.contains("web/css") || path.contains("web/img")) {
            newPath = path;
        } else {
            newPath = "web/HTML/404.html";
        }
        return newPath;
    }
}
