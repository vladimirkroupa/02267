package common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author prasopes
 */
public class DateUtils {
 
    /**
     * @param date in format yyyy-MM-dd
     * @return {@link Date} instance
     */
    public static Date toDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Date toDate(XMLGregorianCalendar xgc) {
        return xgc.toGregorianCalendar().getTime();
    }

    public static XMLGregorianCalendar toXMLGregCal(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        try {
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            return dtf.newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(e);
        }
        
    }
    
    /**
     * @param date date in format yyyy-MM-dd
     * @return {@XMLGregorianCalendar} instance with everything set to zero except year, month and day
     */
    public static XMLGregorianCalendar toXmlGregCal(String date) {
        return toXmlGregCal(date, "00:00");
    }
    
    /**
     * @param date date in format yyyy-MM-dd
     * @param time in format hh:mm
     * @return {@XMLGregorianCalendar} instance with everything set to zero except year, month, day, hours and minutes
     */
    public static XMLGregorianCalendar toXmlGregCal(String date, String time) {

        final String FORMAT = "%sT%s:00.000+00:00";
        String dateTime = String.format(FORMAT, date, time);

        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        XMLGregorianCalendar xmlGregCal = df.newXMLGregorianCalendar(dateTime);
        return xmlGregCal;
    }

    public static XMLGregorianCalendar toXmlGregCal(int day, int month, int year) {
        final String DATE_FORMAT = "%s-%02d-%02d";
        String date = String.format(DATE_FORMAT, year, month, day);
        String time = "00:00";
        return toXmlGregCal(date, time);
    }

}
