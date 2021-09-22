package server.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

// properties file to read multiple configuration file
public class Properties {
    private HashMap<String, String> regularProperties = null;
    private HashMap<String, String> scriptAlias = null;
    private HashMap<String, String> alias = null;
    private String delimiter = "\\s+";
    private String commentStartCharacter = "#";
    private boolean isMimeTypes = false;
    public Properties() {
        regularProperties = new HashMap<>();
        scriptAlias = new HashMap<>();
        alias = new HashMap<>();

    }

    public void setIsMimeTypes() {
        this.isMimeTypes = true;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void load(BufferedReader bufferedReader) throws IOException {
        String lineRead = null;
        while ((lineRead = bufferedReader.readLine()) != null) {
            String[] tokens = lineRead.split(delimiter);
            if (lineRead.length() == 0 || tokens[0].startsWith(commentStartCharacter)) {
                continue;
            } else if (isMimeTypes) {
                // if the tokens are mimeTypes, create a new hashmap as <Extensions, MimeTypes> key-value pair
                if(tokens.length > 1) {
                    for( int i = 1; i < tokens.length; ++i) {
                        regularProperties.put(tokens[i], tokens[0].replaceAll("\"", ""));
                    }
                }
            } else {
                if(tokens[0].equals("Alias")) {
                    // if the tokens is Alias
                    alias.put(tokens[1], tokens[2].replaceAll("\"", ""));
                } else if (tokens[0].equals("ScriptAlias")) {
                    // if the tokens is ScriptAlias
                    scriptAlias.put(tokens[1], tokens[2].replaceAll("\"", ""));
                } else {
                    // tokens is regular type key-value pair
                    if(tokens.length == 1)
                        regularProperties.put(tokens[0], "");
                    else {
                        regularProperties.put(tokens[0], tokens[1].replaceAll("\"", ""));
                    }
                }
            }
        }
    }

    public String getProperty(String key) {
        return regularProperties.get(key);
    }

    public String getAbsolutePathScriptAlias(String symbolicPath) {
        return scriptAlias.get(symbolicPath);
    }

    public String getAbsolutePathAlias(String symbolicPath) {
        return alias.get(symbolicPath);
    }
}
