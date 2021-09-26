package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;
import server.logs.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class ScriptHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        // TODO - implemented in POST or PUT method;
        Logger logger = new Logger(request);
        logger.setStatusCode(200);
        int bodySize = 0;
        String path = request.getHeader("Path");
        ProcessBuilder pb = new ProcessBuilder(path);
        OutputStream out = response.getOutputStream();
        out.write(("HTTP/1.1 200\r\n").getBytes());
        out.write(("Server : JavaWebServer\r\n").getBytes());
        out.write(("Date : " + new Date().toString() + "\r\n").getBytes());
        try {
            Map<String, String> env = pb.environment();
            Map<String, String> headers = request.getHeaders();
            for (Map.Entry<String, String> header: headers.entrySet()){
                env.put(header.getKey(), header.getValue());
            }
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[0x100000];
                    int len = 0;
                    try {
                    while ((len = inputStream.read(buffer)) >0){
                            out.write(buffer, 0, len);
                    } } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            process.waitFor();
            out.flush();
         } catch (Exception e) {
            e.printStackTrace();
        }
        logger.log();
    }

}
