package server.logs;

import server.configuration.ServerConfReader;
import server.handler.Request;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private File logFile;
    private String IPAddress = "127.0.0.1";
    private String clientID = "-";
    private String userId = "-";
    private Date timeStamp;
    private String method;
    private String requestedResource;
    private String protocol = "HTTP/1.1";
    // statusCode = 508 means missing statusCode
    private Integer statusCode = 508;
    private Integer fileSize = 0;
    private String dataPattern ="dd/MMM/yyyy:HH:mm:ss Z";
    private SimpleDateFormat format;
    public Logger(Request request) throws IOException {
        ServerConfReader serverConfReader = new ServerConfReader();
        String logFilePath = serverConfReader.getLogFilePath();
        logFile = new File(logFilePath);
        if(!logFile.exists()){
            logFile.createNewFile();
        };
        timeStamp = new Date();
        format = new SimpleDateFormat(this.dataPattern);

        String clientIpAddress = GetClientIPAddress.getClientIpAddress(request);
        if (clientIpAddress != null) {
            this.setIPAddress(clientIpAddress);
        }
        if (request.getHeader("userid") != null) {
            this.setUserId(request.getHeader("userid"));
        }
        this.setMethod(request.getMethod());
        this.setRequestedResource(request.getFullURI());
    }

    public void setIPAddress(String ipAddress) {
        this.IPAddress = ipAddress;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRequestedResource(String resource) {
        this.requestedResource = resource;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setDataPattern(String dataPattern){
        this.dataPattern = dataPattern;
    }

    public void log() throws IOException {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(this.IPAddress);
        logBuilder.append(" ");
        logBuilder.append(this.clientID);
        logBuilder.append(" ");
        logBuilder.append(this.userId);
        logBuilder.append(" [");
        logBuilder.append(format.format(this.timeStamp));
        logBuilder.append("] \"");
        logBuilder.append(this.method);
        logBuilder.append(" ");
        logBuilder.append(this.requestedResource);
        logBuilder.append(" ");
        logBuilder.append(this.protocol);
        logBuilder.append("\" ");
        logBuilder.append(this.statusCode.toString());
        logBuilder.append(" ");
        logBuilder.append(this.fileSize.toString());
        logBuilder.append("\n");
        FileWriter fileWriter = new FileWriter(this.logFile, true);
        fileWriter.append(logBuilder.toString());
        fileWriter.flush();
        fileWriter.close();
    }
}
