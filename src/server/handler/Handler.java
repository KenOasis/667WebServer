package server.handler;

import server.handler.Request;
import server.handler.Response;

import java.io.IOException;
/*
    Interface for handling the request
 */
public interface Handler {
    public void handle(Request request, Response response) throws IOException;
}
