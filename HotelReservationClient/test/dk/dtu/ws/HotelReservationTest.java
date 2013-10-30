/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dtu.ws;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author prasopes
 */
public class HotelReservationTest {
    
    private static HotelBookingArrayType getHotelsOperation(HotelQueryType hotelQuery) {
        HotelService service = new HotelService();
        HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.getHotelsOperation(hotelQuery);
    }
    
    @Test
    public void testGetHotelOperation() throws DatatypeConfigurationException {
        HotelQueryType query = new HotelQueryType();
        query.setArrivalDate(toXMLGregCal(new Date()));
        query.setCity("Copenhagen");
        query.setDepartureDate(toXMLGregCal(new Date()));
        
        HotelBookingArrayType result = getHotelsOperation(query);
        Assert.assertFalse(result.getBookings().isEmpty());
    }
    
    private XMLGregorianCalendar toXMLGregCal(Date date) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        DatatypeFactory dtf = DatatypeFactory.newInstance();
        return dtf.newXMLGregorianCalendar(cal);
    }
    
}
