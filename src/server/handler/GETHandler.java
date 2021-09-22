package server.handler;

import server.handler.defaultHandler.FileNotFoundHandler;
import server.handler.defaultHandler.ResponseFileHandler;

import java.io.File;
import java.io.IOException;

/*
    Default Handler of GET
 */
public class GETHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        String filePath = request.getHeader("Path");
        File file = new File(filePath);
        System.out.print("In GET HANDLER, " + file.exists());
        if (file.exists()) {
            response.setResponseCodeAndStatus(200, "OK");
            ResponseFileHandler responseFileHandler = new ResponseFileHandler();
            responseFileHandler.handle(request, response);
        } else {
            FileNotFoundHandler fileNotFoundHandler = new FileNotFoundHandler();
            fileNotFoundHandler.handle(request, response);
        }
    }


    public boolean isAuthenticated() {
        // TODO:
        // 1). Read the conf file to check the AccessFileName
        // 2). Check the target directory whether contain the Access File
        //      Yes => step 3) ;  No => return true;
        // 3). Located the AuthUser file and do the authentication
        return true;
    }
}
