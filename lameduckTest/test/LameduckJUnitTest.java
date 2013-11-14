/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import flightdata.FlightInfoListType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;

/**
 *
 * @author s114671
 */
public class LameduckJUnitTest {

    public LameduckJUnitTest() {
    }
    
    


    @Test
    public void testGetFlightsCPH_LONDON() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        flightdata.GetFlightQuery getFlightQuery = new flightdata.GetFlightQuery();
        
        getFlightQuery.setDate(date("2013-09-18T17:00:00.000+00:00"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("London, Heathrow, England");
        
        flightdata.FlightInfoListType testResult = port.getFlights(getFlightQuery);

        assertEquals("1234567", testResult.getFlightInfo().get(0).getBookingNumber());
    }
    
    @Test
    public void testGetFlightsCPH_BEIJING() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        
        flightdata.GetFlightQuery getFlightQuery = new flightdata.GetFlightQuery();
        
        getFlightQuery.setDate(date("2013-12-02T12:00:00.000+00:00"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("Beijing, China");
        
        flightdata.FlightInfoListType testResult = port.getFlights(getFlightQuery);
       // System.out.println("SIZE: "+testResult.getFlightInfo().size());
        assertEquals("1234568", testResult.getFlightInfo().get(0).getBookingNumber());
    }
    
    @Test
    public void testGetFlightsCPH_OSLO() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        flightdata.GetFlightQuery getFlightQuery = new flightdata.GetFlightQuery();
        getFlightQuery.setDate(date("2013-12-06T12:00:00.000+00:00"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("Oslo, Norway");
        
        flightdata.FlightInfoListType testResult = port.getFlights(getFlightQuery);
        
        //flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "Oslo, Norway", date("2013-12-06T12:00:00.000+00:00"));

        assertEquals(testResult.getFlightInfo().get(0).getBookingNumber(), "1234571");
    }
    
    @Test
    public void testGetFlightsCPH_OSLO_MULTIPLERESULTS() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        
                flightdata.GetFlightQuery getFlightQuery = new flightdata.GetFlightQuery();
        getFlightQuery.setDate( date("2013-12-06T12:00:00.000+00:00"));
        getFlightQuery.setStartDest("Copenhagen, Denmark");
        getFlightQuery.setFinalDest("Oslo, Norway");
        
        flightdata.FlightInfoListType testResult = port.getFlights(getFlightQuery);
        
        //flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "Oslo, Norway", date("2013-12-06T12:00:00.000+00:00"));
        for(int i=0;i<testResult.getFlightInfo().size();i++){
            System.out.println(testResult.getFlightInfo().get(i).getBookingNumber());       
        }
        assertEquals(testResult.getFlightInfo().size(), 3);
    }

    
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

    }

   @Test
   public void testBookFlight() throws BookFlightFault {   
        System.out.println("------------start testBookFlight():");
        String testBookingNumber1="1234567";
        System.out.println(" try to book flight : "+testBookingNumber1);
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        //not enough money creditcard
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo2 = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate2 = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate2.setMonth(7);
        expDate2.setYear(9);
        creditcardInfo2.setName("Bech Camilla");
        creditcardInfo2.setNumber("50408822");
        creditcardInfo2.setExpirationDate(expDate2);
        //enough money creditcard
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        ///book the flight
        
        
        
        flightdata.BookFlightQuery bookFlightQuery = new flightdata.BookFlightQuery();
        bookFlightQuery.setBookingNumber(testBookingNumber1);
        bookFlightQuery.setCreditcardInfo(creditcardInfo);
        boolean result= port.bookFlight(bookFlightQuery);
        if(result){
        System.out.println(" flight  "+testBookingNumber1+" is booked ;");}
        System.out.println("------------end testBookFlight():");
    }
   
    public void BookFlight(String bookingNumber) throws BookFlightFault {
        System.out.println("in Bookflight() : try to book flight : "+bookingNumber);
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        //not enough money creditcard
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo2 = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate2 = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate2.setMonth(7);
        expDate2.setYear(9);
        creditcardInfo2.setName("Bech Camilla");
        creditcardInfo2.setNumber("50408822");
        creditcardInfo2.setExpirationDate(expDate2);
        //enough money creditcard
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        ///book the flight
        
        flightdata.BookFlightQuery bookFlightQuery = new flightdata.BookFlightQuery();
        bookFlightQuery.setBookingNumber(bookingNumber);
        bookFlightQuery.setCreditcardInfo(creditcardInfo);
        boolean result= port.bookFlight(bookFlightQuery);
        if(result){
        System.out.println("in Bookflight(): flight "+bookingNumber+" is booked ");
        }
    }
 @Test
    public void testCancelFlight() throws CancelFlightFault, BookFlightFault {
        System.out.println("------------start testCancelFlight():");
        String testBookingNumber="1234573";
        BookFlight(testBookingNumber);
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        flightdata.CancelFlightQuery cf = new flightdata.CancelFlightQuery();
        cf.setBookingNumber(testBookingNumber);
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        boolean result= port.cancelFlight(cf);
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
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        flightdata.CancelFlightQuery cf = new flightdata.CancelFlightQuery();
        cf.setBookingNumber("1222782266");
        cf.setCreditcardInfo(creditcardInfo);
        cf.setFlightPrice(1500);

        boolean result= port.cancelFlight(cf);
        //boolean result= port.cancelFlight("1222782266", 1500, creditcardInfo);
       
    }
   @Test(expected=BookFlightFault.class)
    public void testTwice_booking_withsameBookingNumber() throws BookFlightFault{
         System.out.println("------------start testTwice_booking_withsameBookingNumber():");
    BookFlight("1234571");
    BookFlight("1234571");
     System.out.println("------------end testTwice_booking_withsameBookingNumber():");
    }

    


    
}
