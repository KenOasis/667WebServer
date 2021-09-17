package server.configuration;

import java.io.IOException;
import java.util.HashMap;

public class ServerConfReader extends ConfigurationReader {
    public ServerConfReader(String filename) throws IOException {
        super(filename);
        System.out.println("Configuration file : " + filename);
        this.load();
    }

    public String getServerRoot() {
        return this.getProperty("ServerRoot");
    }

    public String getDocumentRoot() {
        return this.getProperty("DocumentRoot");
    }

    public int getListenPort() {
        return Integer.parseInt(this.getProperty("Listen"));
    }

    public String getLogFilePath() {
        return (this.getProperty("LogFile"));
    }

    public String getScriptAlias(String symbolicPath) {
        return this.getAbsolutePathScriptAlias(symbolicPath);
    }

    public String getAlias(String symbolicPath) {
        return this.getAbsolutePathAlias(symbolicPath);
    }
}