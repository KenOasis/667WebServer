package server.handler.defaultHandler;

import server.configuration.MimeTypeReader;
import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.File;
import java.io.IOException;

public class HeadHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        String filePath = request.getHeader("Path");
        File file = new File(filePath);
        int fileLength = 0;
        String mimeTypes = "text/plain";
        MimeTypeReader mimeTypeReader = new MimeTypeReader("/conf/mime.types");
        int indexOfExtensionDot = filePath.lastIndexOf(".");
        if (indexOfExtensionDot != -1) {
            mimeTypes = mimeTypeReader.getContentType(filePath.substring(indexOfExtensionDot + 1));
        }
        fileLength = (int) file.length();
        response.setResponseCodeAndStatus(200, "OK");
        response.addHeader("Content-Type", mimeTypes);
        response.addHeader("Content-Length", Integer.toString(fileLength));
        response.send();
    }
}
