package common;

import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prasopes
 */
public class DateUtilsTest {
    
    @Test
    public void testSingleDigitMonthDay() {
        XMLGregorianCalendar xgc1 = DateUtils.toXmlGregCal("2013-08-05", "00:00");
        XMLGregorianCalendar xgc2 = DateUtils.toXmlGregCal(5, 8, 2013);
        assertEquals(xgc1.toGregorianCalendar(), xgc2.toGregorianCalendar());
    }    
    
    @Test
    public void testSameValues() {
        XMLGregorianCalendar xgc1 = DateUtils.toXmlGregCal("2013-11-25", "00:00");
        XMLGregorianCalendar xgc2 = DateUtils.toXmlGregCal(25, 11, 2013);
        assertEquals(xgc1.toGregorianCalendar(), xgc2.toGregorianCalendar());
    }
    
}
