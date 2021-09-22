package server.handler;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Response {
    private int statusCode;
    private String statusMessage;
    private HashMap<String, String> headers = new HashMap<>();
    private String body;
    private PrintWriter out;

    public Response(PrintWriter out)  {
        this.out = out;
    }

    public void setResponseCodeAndStatus(int statusCode, String statusMessage)  {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public void addHeader(String headerName, String headerValue)  {
        this.headers.put(headerName, headerValue);
    }

    public void addBody(String body)  {
        headers.put("Content-Length", Integer.toString(body.length()));
        this.body = body;
    }

    public void send() throws IOException {
        headers.put("Connection", "Close");
        out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        for (String headerName : headers.keySet())  {
            out.println(headerName + ": " + headers.get(headerName));
        }
        out.println();
        if (body != null)  {
            out.println(body);
        }
        out.flush();
    }
}
