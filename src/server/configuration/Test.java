package server.configuration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date date = new Date();
        System.out.println(dateFormat.format(date));
    }
}
