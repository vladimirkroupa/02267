package dk.dtu.ws;

import java.util.Date;
import org.joda.time.LocalDate;

public class DateUtil {
    
    public static Date today() {
        return LocalDate.now().toDateTimeAtStartOfDay().toDate();
    }
    
    public static Date tommorow() {
        return LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().toDate();
    }
    
}
