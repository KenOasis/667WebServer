package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.File;
import java.io.IOException;

public class DeleteHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        String absolutePath = request.getHeader("Path");
        File file = new File(absolutePath);
        if(!file.exists()){
            FileNotFoundHandler fileNotFoundHandler = new FileNotFoundHandler();
            fileNotFoundHandler.handle(request, response);
        } else {
            if(file.delete()){
                response.setResponseCodeAndStatus(204, "No Content");
                response.send();
            } else {
                response.setResponseCodeAndStatus(500, "Internal Server Error");
                response.send();
            }
        }
    }
}
