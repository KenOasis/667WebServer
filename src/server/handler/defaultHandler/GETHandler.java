package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.File;
import java.io.IOException;

/*
    Default Handler of GET
 */
public class GETHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        String filePath = request.getHeader("Path");
        File file = new File(filePath);
        response.setResponseCodeAndStatus(200, "OK");
        ResponseFileHandler responseFileHandler = new ResponseFileHandler();
        responseFileHandler.handle(request, response);
    }
}
