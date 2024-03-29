package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.GetClientIPAddress;
import server.logs.Logger;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/*
    Default Handler of GET
 */
public class GetHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String ifModifiedStr = request.getHeader("If-Modified-Since");

        String filePath = request.getHeader("Path");
        File file = new File(filePath);
        Date lastModified = new Date(file.lastModified());

        Logger logger = new Logger(request);

        if (ifModifiedStr != null) {
            Date ifModifiedSinceDate = null;;
            try {
                ifModifiedSinceDate = dateFormat.parse(ifModifiedStr.trim());
            } catch (ParseException pe) {
                pe.printStackTrace();
                BadRequestHandler badRequestHandler = new BadRequestHandler();
                badRequestHandler.handle(request, response);
                return;
            }
            if (ifModifiedSinceDate.compareTo(lastModified) <= 0) {
                response.setResponseCodeAndStatus(200, "OK");
                ResponseFileHandler responseFileHandler = new ResponseFileHandler();
                responseFileHandler.handle(request, response);
            } else {
                response.setResponseCodeAndStatus(304, "NOT Modified");
                logger.setStatusCode(304);
                logger.log();
                response.send();
            }
        } else {
            response.setResponseCodeAndStatus(200, "OK");
            ResponseFileHandler responseFileHandler = new ResponseFileHandler();
            responseFileHandler.handle(request, response);
        }
    }
}
