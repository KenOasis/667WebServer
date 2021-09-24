package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.IOException;

public class PostHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        // ** Currently work as GET
        response.setResponseCodeAndStatus(200, "OK");
        ResponseFileHandler responseFileHandler = new ResponseFileHandler();
        responseFileHandler.handle(request, response);
    }
}
