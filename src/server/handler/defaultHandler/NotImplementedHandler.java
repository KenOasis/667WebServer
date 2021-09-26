package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.Logger;

import java.io.IOException;

public class NotImplementedHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        Logger logger = new Logger(request);
        logger.setStatusCode(501);
        logger.log();
        response.setResponseCodeAndStatus(501, "Not Implemented");
        String body = "<body><h2>Method: " + request.getMethod() + " is not support! </b2></body>";
        response.addBody(body);
        response.addHeader("Content-Type : ", "text/html");
        response.send();
    }
}
