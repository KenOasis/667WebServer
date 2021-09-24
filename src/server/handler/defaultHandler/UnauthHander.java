package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.IOException;

public class UnauthHander implements Handler {

    @Override
    public void handle(Request request, Response response) throws IOException {
        response.setResponseCodeAndStatus(401, "Unauthorized");
        response.addHeader("WWW-Authenticate", "Basic");
        response.addHeader("Content-Type" ,"text/html");
        String body = "<body><h2> Method ErrorCode 401 : Unauthorized <h2></body>";
        response.addBody(body);
        response.send();
    }
}
