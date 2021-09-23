package server.handler;

import server.configuration.ConfigurationReader;
import server.configuration.ServerConfReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Request {
    private BufferedReader in = null;
    private String method = null;
    private String URI = null;
    private HashMap<String, String> headerInfo = null;
    private HashMap<String, String> queryParams = null;
    private boolean isScriptAliased = false;
    public Request(BufferedReader in) {
        this.in = in;
        this.headerInfo = new HashMap<>();
        this.queryParams = new HashMap<>();
    }

    public boolean parse() throws IOException {
        String firstLine = in.readLine();
        if (firstLine == null) {
            return false;
        }
        StringTokenizer tokenizer = new StringTokenizer(firstLine);
        String[] tokens = new String[3];
        for (int i = 0; i < tokens.length; ++i) {
            if (tokenizer.hasMoreTokens()) {
                tokens[i] = tokenizer.nextToken();
            } else {
                // parse fail for the request
                return false;
            }
        }

        // if parse success, the first token is http-method, the second one is the whole uri
        this.method = tokens[0];
        this.URI = tokens[1];

        // parse header items
        while (true) {
            String headerLine = in.readLine();
            if (headerLine.length() == 0) {
                break;
            }

            int delimiterIndex = headerLine.indexOf(":");
            if (delimiterIndex == -1) {
                // no header info found
                break;
            }

            headerInfo.put(headerLine.substring(0, delimiterIndex), headerLine.substring(delimiterIndex + 1));
        }

        // parse absolute path of the url

        int paramDelimiterIndex = tokens[1].indexOf("?");
        String path = null;
        if (paramDelimiterIndex == -1) {
            // no params
            path = tokens[1];
        } else {
            path = tokens[1].substring(0, paramDelimiterIndex);
        }

        ServerConfReader confReader = new ServerConfReader("/conf/httpd.conf");
        String scriptAlias;
        String alias;
        int lastIndexOfDir = path.lastIndexOf("/");

        String dir = path.substring(0, lastIndexOfDir + 1);
        System.out.println("Path init : " + path);
        if((scriptAlias = confReader.getScriptAlias(dir)) != null) {
            path = scriptAlias + path.substring(lastIndexOfDir + 1);
            isScriptAliased = true;
        } else if ((alias = confReader.getAlias(dir)) != null) {
            path = alias + path.substring(lastIndexOfDir + 1);
        } else {
            path = confReader.getDocumentRoot() + path.substring(1);
        }
        File file = new File(path);
        if (!isScriptAliased && file.isDirectory()) {
            path = path + confReader.getDirectoryIndex();
        }

        System.out.println("PATH : " + path);
        headerInfo.put("Path", path);

        // parse query parameter

        String queryString = tokens[1].substring(paramDelimiterIndex + 1);
        parseQueryParams(queryString);
        return true;
    }

    public String getMethod() {
        return this.method;
    }

    public String getFullURI() {
        return this.URI;
    }

    public String getHeader(String headerName) {
        return this.headerInfo.get(headerName);
    }

    public String getQueryParameter(String parameterName) {
        return this.queryParams.get(parameterName);
    }

    public boolean isScriptAliased() {
        return this.isScriptAliased;
    }
    // helper method to parse query parameters
    private void parseQueryParams(String queryString) {
        String[] paramTokens = queryString.split("&");
        for (int i = 0; i < paramTokens.length; ++i) {
            int assignIndex = paramTokens[i].indexOf("=");
            if (assignIndex >= 0) {
                // has parameter
                String paramField = paramTokens[i].substring(0, assignIndex);
                String paramValue = paramTokens[i].substring(assignIndex + 1);
                queryParams.put(paramField, paramValue);
            } else {
                // no filed value but the field exist;
                queryParams.put(paramTokens[i], null);
            }
        }
    }
}
