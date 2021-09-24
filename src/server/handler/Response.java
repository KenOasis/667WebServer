package server.handler;

import java.io.*;
import java.util.Date;
import java.util.HashMap;

public class Response {
    private int statusCode;
    private String statusMessage;
    private HashMap<String, String> headers = new HashMap<>();
    private String body;
    private OutputStream out;
    private File file;
    public Response(OutputStream out)  {
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

    public void writeFileData(File file) throws IOException {
        this.file = file;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public void send() throws IOException {
        headers.put("Connection", "Close");
        headers.put("Server", "Tan Huang");
        headers.put("Date", new Date().toString());
        out.write(("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n").getBytes());
        for (String headerName : headers.keySet())  {
            out.write((headerName + " : " + headers.get(headerName) + "\r\n").getBytes());
        }
        out.write("\r\n".getBytes());
        if (body != null)  {
            out.write(body.getBytes());
        }
        out.flush();
        if (file != null) {
            FileInputStream fs = new FileInputStream(file);
            final byte[] buffer = new byte[0x100000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                out.write(buffer, 0, count);
            }
            fs.close();
        }
        out.flush();
    }
}
