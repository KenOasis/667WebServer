package server.configuration;

import java.io.IOException;
import java.util.HashMap;

public class MimeTypeReader extends ConfigurationReader {

    public MimeTypeReader(String filename) throws IOException {
        super(filename);
        this.setIsMimeTypes();
        this.load();
    }

    public String getContentType(String fileExtension) {
        return this.getProperty(fileExtension);
    }
}
