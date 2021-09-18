package server.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Request {
    private BufferedReader in = null;
    private String method = null;
    private String URI = null;
    private HashMap<String, String> headerInfo = null;
    private HashMap<String, String> queryParams = null;
    public Request(BufferedReader in) {
        this.in = in;
        this.headerInfo = new HashMap<>();
        this.queryParams = new HashMap<>();
    }

    public boolean parse() throws IOException {
        String firstLine = in.readLine();

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

        // parse query parameters

        int paramDelimiterIndex = tokens[1].indexOf("?");
        String path = null;
        if (paramDelimiterIndex == -1) {
            // no params
            path = tokens[1];
        } else {
            path = tokens[1].substring(0, paramDelimiterIndex);
        }
        if ("/".equals(path)) {
            path = "/index.html";
        }

        headerInfo.put("Path", path);

        return true;
    }

    public String getMethod() {
        return this.method;
    }

    public String getFullURI() {
        return this.URI;
    }

    public HashMap<String, String> getHeaderInfo() {
        return (HashMap<String, String>) this.headerInfo.clone();
    }

    public HashMap<String, String> getQueryParams() {
        return (HashMap<String, String>) this.queryParams.clone();
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
