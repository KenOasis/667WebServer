package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.Logger;

import java.io.IOException;

public class FileNotFoundHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        Logger logger = new Logger(request);
        logger.setStatusCode(404);
        logger.log();
        response.setResponseCodeAndStatus(404, "Not Found");
        response.addHeader("Content-Type", "text/html");
        String path = request.getHeader("Path");
        String body = "<body> <h2> ErrorCode: 404 Not Found </h2>" +
                "<h3>" + path + "</h3> </body>";
        response.addBody(body);
        response.send();
    }
}
