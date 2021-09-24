package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class PutHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        String fileName = request.getQueryParameter("filename");
        String path = request.getHeader("Path");
        if (fileName == null) {
            fileName = "new_file.txt";
        }
        String absolutePath = path + fileName;
        File file = new File(absolutePath);
        if (file.exists()) {
            // 409 Conflict
            response.setResponseCodeAndStatus(409, "Conflict");
            response.addHeader("Content-Type", "text/html");
            String body = "<body><h2>Error Code: 409 Conflict, Resource already exist. </h2></body>";
            response.addBody(body);
            response.send();
        } else {
            file.createNewFile();
            response.setResponseCodeAndStatus(200, "OK");
            response.send();
        }
    }
}
