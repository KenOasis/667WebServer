package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.Logger;

import java.io.IOException;

public class PostHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        // ** Currently work as GET
        Logger logger = new Logger(request);
        logger.setStatusCode(200);
        logger.log();
        response.setResponseCodeAndStatus(200, "OK");
        ResponseFileHandler responseFileHandler = new ResponseFileHandler();
        responseFileHandler.handle(request, response);
    }
}
