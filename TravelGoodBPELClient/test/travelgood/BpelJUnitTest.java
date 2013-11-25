package travelgood;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import flightdata.FlightInfoList;
import hotelreservationtypes.HotelList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author joannes
 */
public class BpelJUnitTest {
static String itNo="no.1234";
    public BpelJUnitTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @BeforeClass
    public static void setUpClass() {
    createItineraryOperation(itNo);
    }
    @Test
    public void p1_testPlanning(){
        //flight
        testslistFlightsOperation();
        //hotel
        testslistHotelsOperation();
        //  cancel itinerary
        testCancelItinerary();
    }
    
    
    //@Test
    public void testslistFlightsOperation() {
        String startDest = "Copenhagen, Denmark";
        String finalDest = "Oslo, Norway";
        XMLGregorianCalendar date = date("2013-12-06T12:00:00.000+00:00");
        String EXPECTED = "1234571";
        travelgood.XFlightQuery xflightQuery= new travelgood.XFlightQuery();
        flightdata.GetFlightQuery flightQuery = new flightdata.GetFlightQuery();
        flightQuery.setDate(date);
        flightQuery.setStartDest(startDest);
        flightQuery.setFinalDest(finalDest);
        xflightQuery.setGetFlightQuery(flightQuery);
        xflightQuery.setItineraryNo(itNo);
        String result = listFlightsOperation(xflightQuery).getFlightInfo().get(0).getBookingNumber();
        System.out.println("Flight: " + result);
        assertEquals(EXPECTED, result);
    }
     
   // @Test
    public void testslistFlightsOperationGetMultiple() {
        
        String startDest = "Copenhagen, Denmark";
        String finalDest = "Oslo, Norway";
        XMLGregorianCalendar date = date("2013-12-06T12:00:00.000+00:00");
        int EXPECTED = 3;
        travelgood.XFlightQuery xflightQuery= new travelgood.XFlightQuery();
        flightdata.GetFlightQuery flightQuery = new flightdata.GetFlightQuery();
        flightQuery.setDate(date);
        flightQuery.setStartDest(startDest);
        flightQuery.setFinalDest(finalDest);
        xflightQuery.setGetFlightQuery(flightQuery);
        xflightQuery.setItineraryNo(itNo);
        int result = listFlightsOperation(xflightQuery).getFlightInfo().size();
        assertEquals(EXPECTED, result);
    }

    
   // @Test
    public void testslistFlightsOperationZERO() {
        String startDest = "Copenhagen, Denmark";
        String finalDest = "San Francisco, United States";
        XMLGregorianCalendar date = date("2013-12-06T12:00:00.000+00:00");
        int EXPECTED = 0;
        travelgood.XFlightQuery xflightQuery= new travelgood.XFlightQuery();
        flightdata.GetFlightQuery flightQuery = new flightdata.GetFlightQuery();
        flightQuery.setDate(date);
        flightQuery.setStartDest(startDest);
        flightQuery.setFinalDest(finalDest);
        xflightQuery.setGetFlightQuery(flightQuery);
        xflightQuery.setItineraryNo(itNo);
        int result = listFlightsOperation(xflightQuery).getFlightInfo().size();
        assertEquals(EXPECTED, result);
    }
    
    //@Test
    public void testslistHotelsOperation() {
        String startDest = "Copenhagen";
        XMLGregorianCalendar startDate = date("2013-12-06T12:00:00.000+00:00");
        XMLGregorianCalendar endDate = date("2013-12-06T12:00:00.000+00:00");
        String EXPECTED = "DTU Hostel";
        travelgood.XHotelQuery xhotelQuery= new travelgood.XHotelQuery();
        hotelreservationtypes.HotelQuery hq = new hotelreservationtypes.HotelQuery();
        hq.setArrivalDate(startDate);
        hq.setCity(startDest);
        hq.setDepartureDate(endDate);
        xhotelQuery.setHotelQuery(hq);
        xhotelQuery.setItineraryNo(itNo);
        String result = listHotelsOperation(xhotelQuery).getHotels().get(0).getHotelName();
        assertEquals(EXPECTED, result);
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
    public void testListHotelsOperation() {
        hotelreservationtypes.HotelQuery hq = new hotelreservationtypes.HotelQuery();

        hq.setArrivalDate(date("2013-11-11T12:00:00.000+00:00"));
        hq.setCity("Copenhagen");
        hq.setDepartureDate(date("2013-11-12T12:00:00.000+00:00"));
      //  System.out.println(listHotelsOperation(hq).getHotels().get(0).getHotelName());
    }  

    public void testCancelItinerary(){
        try {
           String result= cancelItineraryOperation(itNo);
            System.out.println(result);
        } catch (CancelItineraryFault ex) {
            Logger.getLogger(BpelJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static FlightInfoList listFlightsOperation(travelgood.XFlightQuery flightQuery) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.listFlightsOperation(flightQuery);
    }

    private static HotelList listHotelsOperation(travelgood.XHotelQuery hotelQueryRequest) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.listHotelsOperation(hotelQueryRequest);
    }

    private static String createItineraryOperation(java.lang.String createItineraryReq) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.createItineraryOperation(createItineraryReq);
    }

    private static String cancelItineraryOperation(java.lang.String itineraryNo) throws CancelItineraryFault {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.cancelItineraryOperation(itineraryNo);
    }
}
