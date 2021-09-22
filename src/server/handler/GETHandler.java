package server.handler;

import java.io.IOException;

/*
    Default Handler of GET
 */
public class GETHandler implements RequestHandler {
    @Override
    public void handle(Request request, Response response) throws IOException {

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
