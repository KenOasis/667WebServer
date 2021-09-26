package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.Logger;

import java.io.IOException;

public class BadRequestHandler implements Handler {

    @Override
    public void handle(Request request, Response response) throws IOException {
        Logger logger = new Logger(request);
        logger.setStatusCode(400);
        logger.log();
        response.setResponseCodeAndStatus(400, "Bad Request");
        String body = "<body><h2>ErrorCode 400, Bad Request, </h2></body>";
        response.addHeader("Content-Type", "text/html");
        response.addBody(body);
        response.send();
    }
}
