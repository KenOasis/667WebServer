package server;

import server.configuration.ServerConfReader;
import server.handler.defaultHandler.GETHandler;
import server.handler.defaultHandler.HEADHandler;
import server.handler.Request;
import server.handler.Response;
import server.handler.defaultHandler.BadRequestHandler;
import server.handler.defaultHandler.FileNotFoundHandler;
import server.handler.defaultHandler.NotImplementedHandler;

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
                // TODO Authentication
                method = request.getMethod().toUpperCase();
                String absolutePath = request.getHeader("Path");
                File file = new File(absolutePath);
                if (file.exists()) {
                    // TODO Check isScriptAlias and run script
                    if(method.equals("GET")) {
                        // TODO check if-modified-since
                        GETHandler getHandler = new GETHandler();
                        getHandler.handle(request, response);
                    } else if (method.equals("HEAD")) {
                        HEADHandler headHandler = new HEADHandler();
                        headHandler.handle(request, response);
                    } else {
                        // Not Implemented
                        NotImplementedHandler notImplementedHandler = new NotImplementedHandler();
                        notImplementedHandler.handle(request, response);
                    }
                } else {
                    // 404 Not Found
                    FileNotFoundHandler fileNotFoundHandler = new FileNotFoundHandler();
                    fileNotFoundHandler.handle(request, response);
                }
            } else {
                // 400 Bad Request
                BadRequestHandler badRequestHandler = new BadRequestHandler();
                badRequestHandler.handle(request, response);
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
