package server.handler.defaultHandler;

import server.configuration.MimeTypeReader;
import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HeadHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        // Parsing the if-Modified-Since header
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String ifModifiedStr = request.getHeader("If-Modified-Since");

        // get file metadata
        String filePath = request.getHeader("Path");
        File file = new File(filePath);
        Date lastModified = new Date(file.lastModified());
        int fileLength = (int) file.length();;
        String mimeTypes = "text/plain";
        MimeTypeReader mimeTypeReader = new MimeTypeReader("/conf/mime.types");
        int indexOfExtensionDot = filePath.lastIndexOf(".");
        if (indexOfExtensionDot != -1) {
            mimeTypes = mimeTypeReader.getContentType(filePath.substring(indexOfExtensionDot + 1));
        }

        if (ifModifiedStr != null) {
            Date ifModifiedSinceDate = null;;
            try {
                ifModifiedSinceDate = dateFormat.parse(ifModifiedStr.trim());
            } catch (ParseException pe) {
                // parsing failed 400 Bad Request
                pe.printStackTrace();
                BadRequestHandler badRequestHandler = new BadRequestHandler();
                badRequestHandler.handle(request, response);
                return;
            }

            if (ifModifiedSinceDate.compareTo(lastModified) <= 0) {
                response.setResponseCodeAndStatus(200, "OK");
                response.addHeader("Content-Type", mimeTypes);
                response.addHeader("Content-Length", Integer.toString(fileLength));
                response.send();
            } else {
                response.setResponseCodeAndStatus(304, "NOT Modified");
                response.addHeader("Content-Type", mimeTypes);
                response.addHeader("Content-Length", Integer.toString(fileLength));
                response.send();
            }
        } else {
            response.setResponseCodeAndStatus(200, "OK");
            response.addHeader("Content-Type", mimeTypes);
            response.addHeader("Content-Length", Integer.toString(fileLength));
            response.send();
        }
    }
}
