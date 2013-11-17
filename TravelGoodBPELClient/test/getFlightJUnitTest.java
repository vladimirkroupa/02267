/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import flightdata.FlightInfoList;
import hotelreservationtypes.HotelList;
import java.util.Date;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joannes
 */
public class getFlightJUnitTest {
    
    public getFlightJUnitTest() {
    }
    
   
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    

    
    @Test
    public void testslistFlightsOperation() {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        String startDest="Copenhagen, Denmark";
        String finalDest="Oslo, Norway";
      
        flightdata.GetFlightQuery flightQuery = new flightdata.GetFlightQuery();
        flightQuery.setDate(date("2013-12-06T12:00:00.000+00:00"));
        flightQuery.setStartDest("Copenhagen, Denmark");
        flightQuery.setFinalDest("Oslo, Norway");
      // port.listFlightsOperation(flightQuery);
      String a = port.listFlightsOperation(flightQuery).getFlightInfo().get(0).getBookingNumber();
        
    }
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
       
    //@Test
    public void  listHotelsOperation() {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        
        hotelreservationtypes.HotelQuery hq = new hotelreservationtypes.HotelQuery();
        
        hq.setArrivalDate(date("2013-11-11T12:00:00.000+00:00"));
        hq.setCity("Copenhagen");
        hq.setDepartureDate(date("2013-11-12T12:00:00.000+00:00"));
        System.out.println(port.listHotelsOperation(hq).getHotels().get(0).getHotelName());
    }


   

 

}
