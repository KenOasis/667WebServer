package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.IOException;

public class ForbiddenHandler implements Handler {


    @Override
    public void handle(Request request, Response response) throws IOException {
        response.setResponseCodeAndStatus(403, "Forbidden");
        String body = "<body><h2> ErrorCode: 403 Forbidden </h2></body>";
        response.addBody(body);
        response.send();
    }
}
