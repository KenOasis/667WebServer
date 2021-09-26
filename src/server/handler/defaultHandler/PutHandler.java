package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class PutHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        String fileName = request.getQueryParameter("filename");
        String path = request.getHeader("Path");
        String payload = request.getPayload();
        Logger logger = new Logger(request);
        if (fileName == null) {
            fileName = "new_file.txt";
        }
        String absolutePath = path + fileName;
        File file = new File(absolutePath);
        if (file.exists()) {
            // 409 Conflict
            logger.setStatusCode(409);
            logger.log();
            response.setResponseCodeAndStatus(409, "Conflict");
            response.addHeader("Content-Type", "text/html");
            String body = "<body><h2>Error Code: 409 Conflict, Resource already exist. </h2></body>";
            response.addBody(body);
            response.send();
        } else {
            file.createNewFile();
            if (payload != null) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(payload);
                fileWriter.close();
            }
            logger.setStatusCode(201);
            logger.log();
            response.setResponseCodeAndStatus(201, "Created");
            response.send();
        }
    }
}
