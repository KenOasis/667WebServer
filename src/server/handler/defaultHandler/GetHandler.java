package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

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
        String ifModifiedStr = request.getHeader("If-Modified-Since").trim();
        if (ifModifiedStr != null) {
            Date ifModifiedSinceDate = null;;
            try {
                System.out.println(ifModifiedStr);
                ifModifiedSinceDate = dateFormat.parse(ifModifiedStr);
            } catch (ParseException pe) {
                pe.printStackTrace();
                BadRequestHandler badRequestHandler = new BadRequestHandler();
                badRequestHandler.handle(request, response);
                return;
            }
            String filePath = request.getHeader("Path");
            File file = new File(filePath);
            Date lastModified = new Date(file.lastModified());
            System.out.println("Last modified : " + lastModified.toString());
            if (ifModifiedSinceDate.compareTo(lastModified) <= 0) {
                response.setResponseCodeAndStatus(200, "OK");
                ResponseFileHandler responseFileHandler = new ResponseFileHandler();
                responseFileHandler.handle(request, response);
            } else {
                response.setResponseCodeAndStatus(304, "NOT Modified");
                response.send();
            }
        } else {
            response.setResponseCodeAndStatus(200, "OK");
            ResponseFileHandler responseFileHandler = new ResponseFileHandler();
            responseFileHandler.handle(request, response);
        }

    }
}
