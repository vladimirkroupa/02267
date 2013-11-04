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
import dk.dtu.ws.hotelservice.ValidateCreditCard;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Test
    public void testGetHotelOperation() throws DatatypeConfigurationException {
        HotelQueryType query = new HotelQueryType();
        query.setArrivalDate(toXMLGregCal(new Date()));
        query.setCity("Copenhagen");
        query.setDepartureDate(toXMLGregCal(new Date()));

        HotelArrayType result = getHotelsOperation(query);
        Assert.assertFalse(result.getHotels().isEmpty());
    }

    @Test
    public void testBookHotelOperation() {
        try {
            HotelBookingWithCreditCardType booking = new HotelBookingWithCreditCardType();
            booking.setBookingNumber("000001");
            CreditCardInfoType info = new CreditCardInfoType();
            info.setName("Anne Strandberg");
            info.setNumber("50408816");
            ExpirationDateType date = new ExpirationDateType();
            date.setMonth(5);
            date.setYear(9);
            info.setExpirationDate(date);
            ValidateCreditCard v = new ValidateCreditCard();
            v.setCreditCardInfo(info);
            v.setAmount(250);
            v.setGroup(1);
            booking.setValidateCreditCardInfo(v);
            boolean bookStatus = bookHotelOperation(booking);
            Assert.assertTrue(bookStatus);
        } catch (BookHotelOperationFault ex) {
            Logger.getLogger(HotelReservationTest.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    @Test
    public void testCancelHotelOperation() {
        boolean cancelStatus;
        try {
            cancelStatus = cancelHotelOperation("000001");
            Assert.assertTrue(cancelStatus);            
        } catch (CancelHotelOperationFault ex) {
            Logger.getLogger(HotelReservationTest.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private XMLGregorianCalendar toXMLGregCal(Date date) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        DatatypeFactory dtf = DatatypeFactory.newInstance();
        return dtf.newXMLGregorianCalendar(cal);
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
}
