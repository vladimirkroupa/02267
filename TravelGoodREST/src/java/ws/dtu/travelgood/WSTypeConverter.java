package ws.dtu.travelgood;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class WSTypeConverter {
    
    /**
     * 
     * @param date in yyyy-MM-dd format
     * @return 
     */
    public static XMLGregorianCalendar toGregorianCalendar(String aDate) {
        try {
            return _toGregorianCalendar(aDate);
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        } catch (DatatypeConfigurationException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private static XMLGregorianCalendar _toGregorianCalendar(String aDate) throws ParseException, DatatypeConfigurationException {
        Date date = toDate(aDate);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }
    
    /**
     * 
     * @param date in yyyy-MM-dd format
     * @return 
     */
    private static Date toDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(date);
    }

}
