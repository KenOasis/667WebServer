package server.configuration;

import java.io.*;


public class ConfigurationReader {
    private Properties properties;
    private String filename = System.getProperty("user.dir");
    ;

    public ConfigurationReader(String filename) {
        this.filename += filename;
        this.properties = new Properties();
    }

    public ConfigurationReader(String filename, boolean isMimeTypes) {
        this.filename += filename;
    }

    public void load() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.filename));
            properties.load(reader);
            reader.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public void setIsMimeTypes() {
        properties.setIsMimeTypes();
    }

    public void setDelimiter(String delimiter) {
        properties.setDelimiter(delimiter);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getAbsolutePathScriptAlias(String symbolicPath) {
        return properties.getAbsolutePathScriptAlias(symbolicPath);
    }

    public String getAbsolutePathAlias(String symbolicPath) {
        return properties.getAbsolutePathAlias(symbolicPath);
    }
}
