package server.handler.defaultHandler;

import server.configuration.MimeTypeReader;
import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.*;

public class ResponseFileHandler implements Handler {

    @Override
    public void handle(Request request, Response response) throws IOException {
        String path = request.getHeader("Path");
        File file = new File(path);
        int fileLength = (int) file.length();
        String mimeTypes = "text/plain";
        MimeTypeReader mimeTypeReader = new MimeTypeReader("/conf/mime.types");
        int indexOfExtensionDot = path.lastIndexOf(".");
        if (indexOfExtensionDot != -1) {
            mimeTypes = mimeTypeReader.getContentType(path.substring(indexOfExtensionDot + 1));
        }
        response.writeFileData(file);
        response.addHeader("Content-Type", mimeTypes);
        response.addHeader("Content-Length", Integer.toString(fileLength));
        response.send();
    }
}
