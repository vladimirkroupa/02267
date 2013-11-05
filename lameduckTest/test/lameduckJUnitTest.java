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
public class lameduckJUnitTest {

    public lameduckJUnitTest() {
    }

    @Test
    public void testGetFlightsCPH_LONDON() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "London, Heathrow, England", date("2013-09-18T17:00:00.000+00:00"));

        assertEquals("1234567", testResult.getFlightInfo().get(0).getBookingNumber());
    }
    
    @Test
    public void testGetFlightsCPH_BEIJING() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "Beijing, China", date("2013-12-02T12:00:00.000+00:00"));
        System.out.println("SIZE: "+testResult.getFlightInfo().size());
        assertEquals("1234568", testResult.getFlightInfo().get(0).getBookingNumber());
    }
    
    @Test
    public void testGetFlightsCPH_OSLO() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "Oslo, Norway", date("2013-12-06T12:00:00.000+00:00"));

        assertEquals(testResult.getFlightInfo().get(0).getBookingNumber(), "1234571");
    }
    
    @Test
    public void testGetFlightsCPH_OSLO_MULTIPLERESULTS() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        flightdata.FlightInfoListType testResult = port.getFlights("Copenhagen, Denmark", "Oslo, Norway", date("2013-12-06T12:00:00.000+00:00"));
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
    public void testValidateCreditCard() throws CreditCardFaultMessage {
        dk.dtu.imm.fastmoney.BankService service = new dk.dtu.imm.fastmoney.BankService();
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);

        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        boolean result = port.validateCreditCard(02, creditcardInfo, 10000000);
        System.out.println(result);
        
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo2 = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate2 = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate2.setMonth(7);
        expDate2.setYear(9);

        creditcardInfo2.setName("Bech Camilla");
        creditcardInfo2.setNumber("50408822");
        creditcardInfo2.setExpirationDate(expDate2);
        
        boolean result2 = port.validateCreditCard(02, creditcardInfo2,999);
        System.out.println(result2);
    }
    
    public void testBookFlight() throws BookFlightFault {
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
        boolean result= port.bookFlight("1234567", creditcardInfo);
        System.out.println("test book flight : "+result);
    }
    @Test
    public void testCancelFlight() throws CancelFlightFault, BookFlightFault {
        testBookFlight();
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        
        dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
        dk.dtu.imm.fastmoney.types.ExpirationDateType expDate = new dk.dtu.imm.fastmoney.types.ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        
        boolean result= port.cancelFlight("1234567", 1500, creditcardInfo);
        System.out.println("test cancel flight result is : "+ result);
    }
    
}
