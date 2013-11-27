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
import dk.dtu.ws.hotelservice.HotelType;
import java.util.Date;
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
        HotelBookingWithCreditCard booking = createBooking("00001");
        boolean bookStatus = bookHotelOperation(booking);
        Assert.assertTrue(bookStatus);
    }
    
    @Test(expected = BookHotelOperationFault.class)
    public void testBookHotelOperationDuplicate() throws BookHotelOperationFault {
        getHotelsOperation(createHotelQuery());
        HotelBookingWithCreditCard booking = createBooking("00001");
        bookHotelOperation(booking);
        bookHotelOperation(booking);
    }
        
    @Test
    public void testCancelHotelOperation() throws CancelHotelOperationFault, BookHotelOperationFault {
        HotelList hotelList = getHotelsOperation(createHotelQuery());
        
        String bookingNoThatCanBeCancelled = "00001";
        for(HotelType h: hotelList.getHotels()){
            if(h.isCancellable()){
                bookingNoThatCanBeCancelled = h.getBookingNo();
                break;
            }
        }
        
        HotelBookingWithCreditCard booking = createBooking(bookingNoThatCanBeCancelled); 
        bookHotelOperation(booking);
        
        boolean cancelStatus;
        cancelStatus = cancelHotelOperation(bookingNoThatCanBeCancelled);
        Assert.assertTrue(cancelStatus);            
    }

    @Test(expected = CancelHotelOperationFault.class)
    public void testCancelHotelOperationNonExistingBooking() throws CancelHotelOperationFault {
        cancelHotelOperation("99999");
    }
    
    @Test(expected = CancelHotelOperationFault.class)
    public void testCancelHotelOperationNonCancellableHotel() throws CancelHotelOperationFault, BookHotelOperationFault {
        HotelList hotelList = getHotelsOperation(createHotelQueryNonCancellable());
        
        String bookingNoThatCannotBeCancelled = "00001";
        for(HotelType h: hotelList.getHotels()){
            if(!h.isCancellable()){
                System.out.println(h.getBookingNo()+","+h.getHotelName());
                bookingNoThatCannotBeCancelled = h.getBookingNo();
                break;
            }
        }
        
        HotelBookingWithCreditCard booking = createBooking(bookingNoThatCannotBeCancelled); 
        bookHotelOperation(booking);
        
        boolean cancelStatus;
        cancelStatus = cancelHotelOperation(bookingNoThatCannotBeCancelled);
        Assert.assertTrue(cancelStatus);  
    }    

    private HotelQuery createHotelQuery() {
        HotelQuery query = new HotelQuery();
        query.setArrivalDate(toXMLGregCal(DateUtil.today()));
        query.setCity("Copenhagen");
        query.setDepartureDate(toXMLGregCal(DateUtil.tommorow()));
        return query;
    }
    
    private HotelQuery createHotelQueryNonCancellable() {
        HotelQuery query = new HotelQuery();
        query.setArrivalDate(toXMLGregCal(DateUtil.today()));
        query.setCity("Lyngby");
        query.setDepartureDate(toXMLGregCal(DateUtil.tommorow()));
        return query;
    }    
    
    private HotelBookingWithCreditCard createBooking(String bookingNo) {
        HotelBookingWithCreditCard booking = new HotelBookingWithCreditCard();
        booking.setBookingNumber(bookingNo);
        
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
