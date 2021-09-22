package server;

import server.configuration.ServerConfReader;
import server.handler.GETHandler;
import server.handler.Request;
import server.handler.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class WebServer extends Thread{
    private static String SERVER_ROOT = null;
    private static String DOCUMENT_ROOT = null;
    private static String LOG_FILE = null;
    private static int LISTEN_PORT = 8080;
    private static boolean verbose = true; // use for test

    private Socket connect;

    public WebServer(Socket c){
        this.connect = c;
    }

    private static void LoadConf() throws IOException {
        ServerConfReader serverConfReader = new ServerConfReader("/conf/httpd.conf");
        SERVER_ROOT = serverConfReader.getServerRoot();
        DOCUMENT_ROOT = serverConfReader.getDocumentRoot();
        LOG_FILE = serverConfReader.getLogFilePath();
        LISTEN_PORT = serverConfReader.getListenPort();
    }

    public static void main(String[] args) {
        try {
            WebServer.LoadConf();
            ServerSocket serverSocket = new ServerSocket(LISTEN_PORT);
            // TODO LOG
            System.out.println("Server Started.\nListening for connections on port: " + LISTEN_PORT + "...\n");

            Socket client;
            // keep listening util halt server execution
            while ((client = serverSocket.accept()) != null) {
                WebServer server = new WebServer(client);

                if (verbose) {
                    System.out.println("Connection established. Time : " + new Date());
                }

                Thread thread = new Thread(server);
                thread.start();
            }
        } catch (IOException ioe) {
            // TODO LOG
            System.err.println(("Server Connection error: " + ioe.getMessage()));
        }
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            out = new PrintWriter(connect.getOutputStream());
            dataOut = new BufferedOutputStream((connect.getOutputStream()));
            Request request = new Request(in);
            Response response = new Response(out, dataOut);
            String method;
            if (request.parse()) {
                method = request.getMethod().toUpperCase();
                if(method.equals("GET")){
                    GETHandler getHandler = new GETHandler();
                    getHandler.handle(request, response);
                } else {
                    response.setResponseCodeAndStatus(501, "Not Implemented");
                    response.addHeader("Server", "WebServer");
                    response.addHeader("Date", new Date().toString());
                    // TODO -> should be get from mime.types
                    response.addHeader("Content-Type", "text/plain");
                    // TODO -> should be determine by the body;
                    response.addHeader("Content-length", "0");
                    response.send();
                }
            } else {
                response.setResponseCodeAndStatus(500, "Internal Server");
                response.addHeader("Server", "WebServer");
                response.addHeader("Date", new Date().toString());
                // TODO -> should be get from mime.types
                response.addHeader("Content-Type", "text/plain");
                // TODO -> should be determine by the body;
                response.addHeader("Content-length", "0");
                response.send();
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("Error with file not found exception : " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println("Server Error : " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close();
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }
}
