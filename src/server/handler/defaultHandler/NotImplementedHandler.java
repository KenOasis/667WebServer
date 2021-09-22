package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.IOException;

public class NotImplementedHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        response.setResponseCodeAndStatus(501, "Not Implemented");
        String body = "<body><h2>Method: " + request.getMethod() + " is not support! </b2></body>";
        response.addBody(body);
        response.addHeader("Content-Type : ", "text/html");
        response.send();
    }
}
