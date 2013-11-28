/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dtu.ws;

import common.DateUtils;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.ExpirationDateType;
import flightdata.BookFlightQuery;
import flightdata.CancelFlightQuery;
import flightdata.FlightInfoList;
import flightdata.FlightInfoType;
import flightdata.GetFlightQuery;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;

/**
 *
 * @author s114671
 */
public class LameDuckTest {

    @Before
    public void resetBookings() {
        reset("");
    }

    @Test
    public void testGetFlightsSingleFlight() {
        GetFlightQuery getFlightQuery = new GetFlightQuery();
        getFlightQuery.setDate(DateUtils.toXmlGregCal("2013-09-18"));
        getFlightQuery.setStartDest("CPH");
        getFlightQuery.setFinalDest("LHR");

        FlightInfoList flightList = getFlights(getFlightQuery);

        assertEquals("1234567", flightList.getFlightInfo().get(0).getBookingNumber());
    }

    @Test
    public void testGetFlightsMultipleFlights() {
        GetFlightQuery getFlightQuery = new GetFlightQuery();
        getFlightQuery.setDate(DateUtils.toXmlGregCal("2013-12-06"));
        getFlightQuery.setStartDest("CPH");
        getFlightQuery.setFinalDest("OSL");

        FlightInfoList flightList = getFlights(getFlightQuery);

        assertEquals(3, flightList.getFlightInfo().size());
        // check that price is not zero
        for (FlightInfoType flight : flightList.getFlightInfo()) {
            assertThat(flight.getFlightPrice(), is(not(0)));
        }
    }

    @Test
    public void testBookFlightCardOK() throws BookFlightFault {
        String bookingNo = "1234567";

        //credit card with enough money
        CreditCardInfoType ccInfo = noLimitCreditCard();

        //book the flight        
        boolean booked = bookFlight(bookingNo, ccInfo);

        Assert.assertTrue(booked);
    }

    @Test(expected = BookFlightFault.class)
    public void testBookFlightCardFail() throws BookFlightFault {
        String bookingNo = "1234567";

        //credit card with enough money
        CreditCardInfoType ccInfo = thousandKrCreditCard();

        //book the flight     
        bookFlight(bookingNo, ccInfo);
    }

    @Test
    public void testCancelFlight() throws CancelFlightFault, BookFlightFault {
        String bookingNumber = "1234573";
        CreditCardInfoType creditcardInfo = noLimitCreditCard();
        
        boolean booked = bookFlight(bookingNumber, creditcardInfo);
        Assert.assertTrue(booked);
        
        CancelFlightQuery cf = new CancelFlightQuery();
        cf.setBookingNumber(bookingNumber);
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        boolean cancelled = cancelFlight(cf);
        Assert.assertTrue(cancelled);
    }

    @Test(expected = CancelFlightFault.class)
    public void testCancelNonExistingFlight() throws CancelFlightFault {
        CreditCardInfoType creditcardInfo = noLimitCreditCard();

        CancelFlightQuery cf = new CancelFlightQuery();
        cf.setBookingNumber("0000000");
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        cancelFlight(cf);
    }
    
    @Test(expected = CancelFlightFault.class)
    public void testCancelNonCancellableFlight() throws CancelFlightFault, BookFlightFault {
        String bookingNumber = "7777777";
        CreditCardInfoType creditcardInfo = noLimitCreditCard();
        
        boolean booked = bookFlight(bookingNumber, creditcardInfo);
        Assert.assertTrue(booked);
        
        CancelFlightQuery cf = new CancelFlightQuery();
        cf.setBookingNumber(bookingNumber);
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        boolean cancelled = cancelFlight(cf);
        Assert.assertTrue(cancelled);
    }    

    @Test(expected = CancelFlightFault.class)
    public void testCancelNonBookedFlight() throws CancelFlightFault {
        CreditCardInfoType creditcardInfo = noLimitCreditCard();

        CancelFlightQuery cf = new CancelFlightQuery();
        cf.setBookingNumber("1234567");
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        cancelFlight(cf);
    }
    
    @Test(expected = BookFlightFault.class)
    public void testRepeatedBooking() throws BookFlightFault {
        String bookingNo = "1234567";

        //credit card with enough money
        CreditCardInfoType ccInfo = noLimitCreditCard();

        //book the flight        
        bookFlight(bookingNo, ccInfo);

        //book the flight again
        bookFlight(bookingNo, ccInfo);
    }
    
    private boolean bookFlight(String bookingNumber, CreditCardInfoType ccInfo) throws BookFlightFault {
        BookFlightQuery bookFlightQuery = new BookFlightQuery();
        bookFlightQuery.setBookingNumber(bookingNumber);
        bookFlightQuery.setCreditcardInfo(ccInfo);
        return bookFlight(bookFlightQuery);
    }
    
    private CreditCardInfoType noLimitCreditCard() {
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setExpirationDate(expDate);
        return creditcardInfo;
    }

    private CreditCardInfoType thousandKrCreditCard() {
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
        creditcardInfo.setName("Bech Camilla");
        creditcardInfo.setNumber("50408822");
        ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(7);
        expDate.setYear(9);
        creditcardInfo.setExpirationDate(expDate);
        return creditcardInfo;
    }

    private static boolean bookFlight(flightdata.BookFlightQuery bookFlightQuery) throws BookFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.bookFlight(bookFlightQuery);
    }

    private static boolean cancelFlight(flightdata.CancelFlightQuery cancelFlightQuery) throws CancelFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.cancelFlight(cancelFlightQuery);
    }

    private static FlightInfoList getFlights(flightdata.GetFlightQuery getFlightQuery) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.getFlights(getFlightQuery);
    }

    private static void reset(java.lang.String resetQuery) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        port.reset(resetQuery);
    }
}
