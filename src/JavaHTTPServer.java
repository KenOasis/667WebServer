import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class JavaHTTPServer extends Thread{
    static final File WEB_ROOT = new File(".");
    static final String PUBLIC_ROOT = "/public_html/";
    static final String DEFAULT_FILE = PUBLIC_ROOT + "index.html";
    static final String FILE_NOT_FOUND = PUBLIC_ROOT + "404.html";
    static final String METHOD_NOT_SUPPORTED = PUBLIC_ROOT + "not_supported.html";
    // port to listen
    static final int PORT = 8080;

    // verbose mode
    static final boolean verbose = true;

    // Client Connection Via Socket Class;
    private Socket connect;

    public JavaHTTPServer(Socket c) {
        connect = c;
    }
    public static void main(String[] args) {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server Started.\nListening for connections on port: " + PORT + "...\n");

            // we listen until users halt server execution
            while (true) {
                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                if(verbose) {
                    System.out.println("Connection Opened. (" + new Date() + ")");
                }

                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println(("Server Connection error: " + e.getMessage()));
        }
    }

    @Override
    public void run() {
        // we manage our particular client connection
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // we read characters  from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toLowerCase(); // we get the Http method of the client
            // we get file requested
            fileRequested = parse.nextToken().toLowerCase();
            // we support only GET and HEAD Methods, we check
            if (!method.equals("get") && !method.equals("head")) {

                if (verbose) {
                    System.out.println("501 Not Implemented : " + method + " method.");
                }

                // we return the not supported file to the client
                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                // read content to return to client
                byte[] fileData = readFileData(file, fileLength);

                // we send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server : Java HTTP Server : 1.0");
                out.println("Date : " + new Date());
                out.println("Content-type : " + contentMimeType);
                out.println("Content-length : " + file.length());
                out.println(); // blank line between headers and content, VERY IMPORTANT
                out.flush(); // flush character output stream buffer
                // file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();
            } else {
                // GET or HEAD method
                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }

                File file = new File(WEB_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = getContentType(fileRequested);
                System.out.println("content type: " + content);
                if (method.equals("get")) {
                    // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);

                    // send HTTP headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server : Java HTTP Server : 1.0");
                    out.println("Date : " + new Date());
                    out.println("Content-type : " + content);
                    out.println("Content-length : " + file.length());
                    out.println(); // blank line between headers and content, VERY IMPORTANT
                    out.flush(); // flush character output stream buffer

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();

                    if (verbose) {
                        System.out.println("File " + fileRequested + " of type " + content + " returned");
                    }
                } else {
                    // send HTTP headers for HEAD method
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server : Java HTTP Server : 1.0");
                    out.println("Date : " + new Date());
                    out.println("Content-type : " + content);
                    if(file.length() != 0)
                        out.println("Content-length : " + file.length());
                    out.println(); // blank line between headers and content, VERY IMPORTANT
                    out.flush(); // flush character output stream buffer
                    if (verbose) {
                        System.out.println("HEAD Response success!");
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Server Error : " + e);
        } finally {
            try {
                in.close(); // close character input stream;
                out.close();
                dataOut.close();
                connect.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    //return support MIME Types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {
            return "text/html";
        }
        else {
            return "text/plain";
        }
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 200 OK");
        out.println("Server : Java HTTP Server : 1.0");
        out.println("Date : " + new Date());
        out.println("Content-type : " + content);
        out.println("Content-length : " + file.length());
        out.println(); // blank line between headers and content, VERY IMPORTANT
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            System.out.println("File " + fileRequested + " not Found");
        }
    }
}
