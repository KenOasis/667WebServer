package server.handler.defaultHandler;

import server.configuration.ServerConfReader;
import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.IOException;

public class FileNotFoundHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        response.setResponseCodeAndStatus(404, "Not Found");
        response.addHeader("Content-Type", "text/html");
        String path = request.getHeader("Path");
        String body = "<body> <h2> ErrorCode: 404 Not Found </h2>" +
                "<h3>" + path + "</h3> </body>";
        response.addBody(body);
        response.send();
    }
}
