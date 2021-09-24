package server.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;

public class AuthHandle {

    private String AuthHeader=null;
    private String AuthType =null;
    private String AuthInfo = null;
    private HashMap<String,String> AccessFileInfo=null;
    private HashMap<String,String> pwdFileInfo=null;

    public AuthHandle(String AuthHeader, HashMap<String,String> AccessFileInfo) {
        this.AuthHeader = AuthHeader;
        this.AccessFileInfo=AccessFileInfo;
        this.pwdFileInfo =new HashMap<>();
    }

    // check and read .htpassword

    private boolean checkAndParsePasswordFile() throws IOException {
        String passwordPath = AccessFileInfo.get("AuthUserFile").replace("\"","");

        File passwordFile = new File(passwordPath);
        boolean hadPasswordFile=passwordFile.exists();

        if (hadPasswordFile){
            BufferedReader reader=null;
            reader = new BufferedReader(new FileReader(passwordPath));
            String line = null;
            while ((line =reader.readLine()) != null){
                String[] tokens = line.split(":");
                if(tokens.length ==2) {
                    pwdFileInfo.put(tokens[0], tokens[1].replace("{SHA}","").trim());
                }
            }
        }
        return hadPasswordFile;
    }

    private void parseAuthInfo(){
        String[] tokens =AuthHeader.trim().split(" ");
        if(tokens.length == 2){
            AuthType=tokens[0];
            AuthInfo =tokens[1];
        }
    }

    public boolean isAuthorized( ) throws IOException {
        parseAuthInfo();
        boolean result = true;

        if(AuthType.equals(AccessFileInfo.get("AuthType"))) {


            // decode client header //dgadfggad
            String credentials = new String(
                    Base64.getDecoder().decode(AuthInfo),
                    Charset.forName("UTF-8")
            );
            //user:password 1
            String[] tokens = credentials.split(":");
            if (tokens.length == 2) {
                result = verifyPassword(tokens[0], tokens[1]);
            }
            else {
                result = false;
            }
        }
        else{
            result= false;
        }

        return result;
    }


    private boolean verifyPassword(String name, String password ) throws IOException {
        // encrypt the password, and compare it to the password stored
        // in the password file (keyed by username)
        // method, below

        if(checkAndParsePasswordFile()) {
            //encode
            String decode = encryptClearPassword(password);
            if (decode.equals(pwdFileInfo.get(name))) {
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    private String encryptClearPassword( String password ) {
        // Encrypt the cleartext password (that was decoded from the Base64 String
        // provided by the client) using the SHA-1 encryption algorithm
        try {
            MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
            byte[] result = mDigest.digest( password.getBytes() );

            return Base64.getEncoder().encodeToString( result );
        } catch( Exception e ) {
            return "";
        }
    }





}
