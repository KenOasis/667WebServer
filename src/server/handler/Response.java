package server.handler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

public class Response {
    private int statusCode;
    private String statusMessage;
    private HashMap<String, String> headers = new HashMap<>();
    private String body;
    private PrintWriter out;
    private OutputStream fileOut;
    private byte[] fileBytes;
    private int fileLength;

    public Response(PrintWriter out, OutputStream fileOut)  {

        this.out = out;
        this.fileOut = fileOut;
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

    public void writeFileData(byte[] fileData, int fileLength) throws IOException {
        this.fileBytes = fileData;
        this.fileLength = fileLength;
    }

    public OutputStream getFileOut() {
        return fileOut;
    }

    public void send() throws IOException {
        headers.put("Connection", "Close");
        headers.put("Server", "Java WebServer");
        headers.put("Date", new Date().toString());
        out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        for (String headerName : headers.keySet())  {
            out.println(headerName + ": " + headers.get(headerName));
        }
        out.println();
        if (body != null)  {
            out.println(body);
        }
        out.flush();
        if(fileBytes != null) {
            fileOut.write(fileBytes, 0, fileLength);
            fileOut.flush();
        }
    }
}
