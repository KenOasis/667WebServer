package server.handler;

import server.configuration.ServerConfReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AccessHandle {
    private String absolutePath = null;
    private String accessFilePath = null;
    private HashMap<String,String> AccessFileInfo= null;



    public AccessHandle(String absolutePath){
        this.absolutePath=absolutePath;
        this.AccessFileInfo = new HashMap<>();

    }

    // check and read .htaccess
    public boolean checkAccessFile() throws IOException {
        boolean fileExist = false;
        int LastIndexSlash=absolutePath.lastIndexOf("/");

        String FolderPath=absolutePath.substring(0,LastIndexSlash+1);

        ServerConfReader confReader = new ServerConfReader("/conf/httpd.conf");

        String accessFileName = confReader.getAccessFile();

        accessFilePath = FolderPath+accessFileName;

        File accessFile = new File(accessFilePath);

        fileExist=accessFile.exists();
        //Parse access File
        if(fileExist){
            BufferedReader reader=null;
            reader = new BufferedReader(new FileReader(accessFilePath));
            String ReadLine = null;
            while ((ReadLine =reader.readLine()) != null){
                String[] tokens = ReadLine.split(" ",2);
                if(tokens.length == 2) {
                    AccessFileInfo.put(tokens[0], tokens[1]);
                }
            }

        }
        else {
            System.out.println("No need to verify");
        }


        return fileExist;
    }

    public HashMap<String, String> getAccessFileInfo() {
        return this.AccessFileInfo;
    }

}
