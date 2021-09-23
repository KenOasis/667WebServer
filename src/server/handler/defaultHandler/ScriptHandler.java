package server.handler.defaultHandler;

import server.handler.Handler;
import server.handler.Request;
import server.handler.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class ScriptHandler implements Handler {
    @Override
    public void handle(Request request, Response response) throws IOException {
        // TODO - implemented in POST or PUT method;
        String path = request.getHeader("Path");
        System.out.println("RUN");
        ProcessBuilder pb = new ProcessBuilder(path);
        try {
            Map<String, String> env = pb.environment();
            Map<String, String> headers = request.getHeaders();
            for (Map.Entry<String, String> header: headers.entrySet()){
                env.put(header.getKey(), header.getValue());
            }
            pb.redirectOutput(new File("ABC"));
            pb.start();
            response.setResponseCodeAndStatus(200, "OK");
            response.send();
         } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileInputStream = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileData);
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
        return fileData;
    }
}
