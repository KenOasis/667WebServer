package server.handler;

import java.io.IOException;
/*
    Interface for handling the request
 */
public interface Handler {
    public void handle(Request request, Response response) throws IOException;
}
