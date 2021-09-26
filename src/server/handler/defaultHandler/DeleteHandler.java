package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.Logger;

import java.io.File;
import java.io.IOException;

public class DeleteHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        String absolutePath = request.getHeader("Path");
        File file = new File(absolutePath);
        Logger logger = new Logger(request);
        if(!file.exists()){
            FileNotFoundHandler fileNotFoundHandler = new FileNotFoundHandler();
            fileNotFoundHandler.handle(request, response);
        } else {
            if(file.delete()){
                logger.setStatusCode(204);
                logger.log();
                response.setResponseCodeAndStatus(204, "No Content");
                response.send();
            } else {
                logger.setStatusCode(500);
                logger.log();
                response.setResponseCodeAndStatus(500, "Internal Server Error");
                response.send();
            }
        }
    }
}
