/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dtu.ws;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.ExpirationDateType;
import flightdata.BookFlightQuery;
import flightdata.CancelFlightQuery;
import flightdata.FlightInfoList;
import flightdata.GetFlightQuery;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
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
    public void testGetFlightsCPH_LONDON() {

        GetFlightQuery getFlightQuery = new GetFlightQuery();        
        getFlightQuery.setDate(toGregorianCalendar("2013-09-18"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("London, Heathrow, England");
        FlightInfoList flightList = getFlights(getFlightQuery);

        assertEquals("1234567", flightList.getFlightInfo().get(0).getBookingNumber());
    }
    
    @Test
    public void testGetFlightsCPH_BEIJING() {
        
        GetFlightQuery getFlightQuery = new GetFlightQuery();
        getFlightQuery.setDate(toGregorianCalendar("2013-12-02"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("Beijing, China");
        
        FlightInfoList flightList = getFlights(getFlightQuery);
       // System.out.println("SIZE: "+testResult.getFlightInfo().size());
        assertEquals("1234568", flightList.getFlightInfo().get(0).getBookingNumber());
    }
    
    @Test
    public void testGetFlightsCPH_OSLO() {
        
        GetFlightQuery getFlightQuery = new GetFlightQuery();
        getFlightQuery.setDate(toGregorianCalendar("2013-12-06"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("Oslo, Norway");
        
        FlightInfoList flightList = getFlights(getFlightQuery);
        
        //flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "Oslo, Norway", date("2013-12-06T12:00:00.000+00:00"));

        assertEquals(flightList.getFlightInfo().get(0).getBookingNumber(), "1234571");
    }
    
    @Test
    public void testGetFlightsCPH_OSLO_MULTIPLERESULTS() {
   
        GetFlightQuery getFlightQuery = new GetFlightQuery();
        getFlightQuery.setDate(toGregorianCalendar("2013-12-06"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("Oslo, Norway");
        
        FlightInfoList flightList = getFlights(getFlightQuery);
        
        //flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "Oslo, Norway", date("2013-12-06T12:00:00.000+00:00"));
        for(int i=0;i<flightList.getFlightInfo().size();i++){
            System.out.println(flightList.getFlightInfo().get(i).getBookingNumber());       
        }
        assertEquals(flightList.getFlightInfo().size(), 3);
    }

    /*
    //Helper method to convert a string like "2013-12-06T14:30:00.000+00:00" in to an XMLGregorianCalendar
    public XMLGregorianCalendar date(String dateinput) {

        //Demos and Usage of javax.xml.datatype.DatatypeFactory.newXMLGregorianCalendar
        //(int year,int month,int day,int hour,int minute,int second,int millisecond,int timezone)
        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            //  Logger.getLogger(lameduckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        XMLGregorianCalendar date = df.newXMLGregorianCalendar(dateinput);
        return date;
    }*/

   @Test
   public void testBookFlight() throws BookFlightFault {   
        System.out.println("------------start testBookFlight():");
        String testBookingNumber1="1234567";
        System.out.println(" try to book flight : "+testBookingNumber1);
        //not enough money creditcard
        CreditCardInfoType creditcardInfo2 = new CreditCardInfoType();
        ExpirationDateType expDate2 = new ExpirationDateType();
        expDate2.setMonth(7);
        expDate2.setYear(9);
        creditcardInfo2.setName("Bech Camilla");
        creditcardInfo2.setNumber("50408822");
        creditcardInfo2.setExpirationDate(expDate2);
        //enough money creditcard
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
        ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        ///book the flight
        

        BookFlightQuery bookFlightQuery = new BookFlightQuery();
        bookFlightQuery.setBookingNumber(testBookingNumber1);
        bookFlightQuery.setCreditcardInfo(creditcardInfo);
        boolean result= bookFlight(bookFlightQuery);
        if(result){
        System.out.println(" flight  "+testBookingNumber1+" is booked ;");}
        System.out.println("------------end testBookFlight():");
    }
   
    public void BookFlight(String bookingNumber) throws BookFlightFault {
        System.out.println("in Bookflight() : try to book flight : "+bookingNumber);
        //not enough money creditcard
        CreditCardInfoType creditcardInfo2 = new CreditCardInfoType();
        ExpirationDateType expDate2 = new ExpirationDateType();
        expDate2.setMonth(7);
        expDate2.setYear(9);
        creditcardInfo2.setName("Bech Camilla");
        creditcardInfo2.setNumber("50408822");
        creditcardInfo2.setExpirationDate(expDate2);
        //enough money creditcard
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
        ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        ///book the flight
        
        BookFlightQuery bookFlightQuery = new BookFlightQuery();
        bookFlightQuery.setBookingNumber(bookingNumber);
        bookFlightQuery.setCreditcardInfo(creditcardInfo);
        boolean result= bookFlight(bookFlightQuery);
        if(result){
        System.out.println("in Bookflight(): flight "+bookingNumber+" is booked ");
        }
    }
 @Test
    public void testCancelFlight() throws CancelFlightFault, BookFlightFault {
        System.out.println("------------start testCancelFlight():");
        String testBookingNumber="1234573";
        BookFlight(testBookingNumber);
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
        ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        CancelFlightQuery cf = new CancelFlightQuery();
        cf.setBookingNumber(testBookingNumber);
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        boolean result= cancelFlight(cf);
        if(result){
            System.out.println(" flight: "+testBookingNumber+" is canceled");
        }  
        System.out.println("------------end testCancelFlight():");
    }
//try to cancel a flight that is not booked or exist 
    // data stored in the server side can be refresh by redploy the web service 
    @Test(expected=CancelFlightFault.class)
    public void testCancelFlight_fault() throws CancelFlightFault{
        System.out.println("------------start testCancelFlight_fault():");
        
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
       ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        CancelFlightQuery cf = new CancelFlightQuery();
        cf.setBookingNumber("1222782266");
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        boolean result= cancelFlight(cf);
        //boolean result= port.cancelFlight("1222782266", 1500, creditcardInfo);
       
    }
    
    @Test(expected=BookFlightFault.class)
    public void testTwice_booking_withsameBookingNumber() throws BookFlightFault{
         System.out.println("------------start testTwice_booking_withsameBookingNumber():");
    BookFlight("1234571");
    BookFlight("1234571");
     System.out.println("------------end testTwice_booking_withsameBookingNumber():");
    }

   
    
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
    
    private static Date toDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(date);
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
