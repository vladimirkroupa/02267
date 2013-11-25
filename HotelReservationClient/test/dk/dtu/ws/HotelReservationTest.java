/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dtu.ws;

import common.DateUtils;
import dk.dtu.ws.hotelservice.BookHotelOperationFault;
import dk.dtu.ws.hotelservice.CancelHotelOperationFault;
import dk.dtu.ws.hotelservice.CreditCardInfoType;
import dk.dtu.ws.hotelservice.ExpirationDateType;
import dk.dtu.ws.hotelservice.HotelList;
import dk.dtu.ws.hotelservice.HotelBookingWithCreditCard;
import dk.dtu.ws.hotelservice.HotelQuery;
import java.util.Date;
import java.util.GregorianCalendar;
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
        HotelList result = getHotelsOperation(createHotelQuery());
        Assert.assertEquals("00001", result.getHotels().get(0).getBookingNo());
        Assert.assertEquals("00002", result.getHotels().get(1).getBookingNo());
        Assert.assertFalse(result.getHotels().isEmpty());
    }

    @Test
    public void testBookHotelOperation() throws BookHotelOperationFault {
        getHotelsOperation(createHotelQuery());
        HotelBookingWithCreditCard booking = createBooking();
        boolean bookStatus = bookHotelOperation(booking);
        Assert.assertTrue(bookStatus);
    }
    
    @Test(expected = BookHotelOperationFault.class)
    public void testBookHotelOperationDuplicate() throws BookHotelOperationFault {
        getHotelsOperation(createHotelQuery());
        HotelBookingWithCreditCard booking = createBooking();
        bookHotelOperation(booking);
        bookHotelOperation(booking);
    }
        
    @Test
    public void testCancelHotelOperation() throws CancelHotelOperationFault, BookHotelOperationFault {
        getHotelsOperation(createHotelQuery());
        HotelBookingWithCreditCard booking = createBooking();
        bookHotelOperation(booking);
        boolean cancelStatus;
        cancelStatus = cancelHotelOperation("00001");
        Assert.assertTrue(cancelStatus);            
    }

    @Test(expected = CancelHotelOperationFault.class)
    public void testCancelHotelOperationNonExistingBooking() throws CancelHotelOperationFault {
        cancelHotelOperation("00002");
    }

    private HotelQuery createHotelQuery() {
        HotelQuery query = new HotelQuery();
        query.setArrivalDate(toXMLGregCal(DateUtil.today()));
        query.setCity("Copenhagen");
        query.setDepartureDate(toXMLGregCal(DateUtil.tommorow()));
        return query;
    }
    
    private HotelBookingWithCreditCard createBooking() {
        HotelBookingWithCreditCard booking = new HotelBookingWithCreditCard();
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
        return DateUtils.toXMLGregCal(date);
    }

    private static boolean bookHotelOperation(HotelBookingWithCreditCard bookingWithCreditCard) throws BookHotelOperationFault {
        dk.dtu.ws.hotelservice.HotelService service = new dk.dtu.ws.hotelservice.HotelService();
        dk.dtu.ws.hotelservice.HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.bookHotelOperation(bookingWithCreditCard);
    }

    private static boolean cancelHotelOperation(String bookingCancellation) throws CancelHotelOperationFault {
        dk.dtu.ws.hotelservice.HotelService service = new dk.dtu.ws.hotelservice.HotelService();
        dk.dtu.ws.hotelservice.HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.cancelHotelOperation(bookingCancellation);
    }

    private static HotelList getHotelsOperation(HotelQuery hotelQuery) {
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
