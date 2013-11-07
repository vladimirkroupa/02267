/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dtu.ws;

import dk.dtu.ws.hotelservice.BookHotelOperationFault;
import dk.dtu.ws.hotelservice.CancelHotelOperationFault;
import dk.dtu.ws.hotelservice.CreditCardInfoType;
import dk.dtu.ws.hotelservice.ExpirationDateType;
import dk.dtu.ws.hotelservice.HotelArrayType;
import dk.dtu.ws.hotelservice.HotelBookingWithCreditCardType;
import dk.dtu.ws.hotelservice.HotelQueryType;
import dk.dtu.ws.hotelservice.HotelType;
import dk.dtu.ws.hotelservice.ValidateCreditCard;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author prasopes
 */
public class HotelReservationTest {

    @Before
    public void cleanBookings() {
        resetOperation("");
    }
    
    @Test
    public void testGetHotelOperation() {
        HotelArrayType result = getHotelsOperation(createHotelQuery());
        Assert.assertEquals("00001", result.getHotels().get(0).getBookingNo());
        Assert.assertEquals("00002", result.getHotels().get(1).getBookingNo());
        Assert.assertFalse(result.getHotels().isEmpty());
    }

    @Test
    public void testBookHotelOperation() throws BookHotelOperationFault {
        getHotelsOperation(createHotelQuery());
        HotelBookingWithCreditCardType booking = createBooking();
        boolean bookStatus = bookHotelOperation(booking);
        Assert.assertTrue(bookStatus);
    }
        
    @Test
    public void testCancelHotelOperation() throws CancelHotelOperationFault, BookHotelOperationFault {
        getHotelsOperation(createHotelQuery());
        HotelBookingWithCreditCardType booking = createBooking();
        bookHotelOperation(booking);
        boolean cancelStatus;
        cancelStatus = cancelHotelOperation("00001");
        Assert.assertTrue(cancelStatus);            
    }

    @Test(expected = CancelHotelOperationFault.class)
    public void testCancelHotelOperationNonExistingBooking() throws CancelHotelOperationFault {
        cancelHotelOperation("00002");
    }

    private HotelQueryType createHotelQuery() {
        HotelQueryType query = new HotelQueryType();
        query.setArrivalDate(toXMLGregCal(DateUtil.today()));
        query.setCity("Copenhagen");
        query.setDepartureDate(toXMLGregCal(DateUtil.tommorow()));
        return query;
    }
    
    private HotelBookingWithCreditCardType createBooking() {
        HotelBookingWithCreditCardType booking = new HotelBookingWithCreditCardType();
        booking.setBookingNumber("00001");
        
        CreditCardInfoType info = new CreditCardInfoType();
        info.setName("Anne Strandberg");
        info.setNumber("50408816");
        ExpirationDateType date = new ExpirationDateType();
        date.setMonth(5);
        date.setYear(9);
        info.setExpirationDate(date);        
        
        //booking.setCreditCardInfo(info);
        return booking;
    }
    
    private XMLGregorianCalendar toXMLGregCal(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        try {
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            return dtf.newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(e);
        }
        
    }

    private static boolean bookHotelOperation(HotelBookingWithCreditCardType bookingWithCreditCard) throws BookHotelOperationFault {
        dk.dtu.ws.hotelservice.HotelService service = new dk.dtu.ws.hotelservice.HotelService();
        dk.dtu.ws.hotelservice.HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.bookHotelOperation(bookingWithCreditCard);
    }

    private static boolean cancelHotelOperation(String bookingCancellation) throws CancelHotelOperationFault {
        dk.dtu.ws.hotelservice.HotelService service = new dk.dtu.ws.hotelservice.HotelService();
        dk.dtu.ws.hotelservice.HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.cancelHotelOperation(bookingCancellation);
    }

    private static HotelArrayType getHotelsOperation(HotelQueryType hotelQuery) {
        dk.dtu.ws.hotelservice.HotelService service = new dk.dtu.ws.hotelservice.HotelService();
        dk.dtu.ws.hotelservice.HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.getHotelsOperation(hotelQuery);
    }

    private static void resetOperation(java.lang.String reset) {
        dk.dtu.ws.hotelservice.HotelService service = new dk.dtu.ws.hotelservice.HotelService();
        dk.dtu.ws.hotelservice.HotelServicePortType port = service.getHotelServiceSOAPPort();
        port.resetOperation(reset);
    }
}
