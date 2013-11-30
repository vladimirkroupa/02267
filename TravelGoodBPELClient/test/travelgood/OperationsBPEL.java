/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import flightdata.FlightInfoList;
import flightdata.FlightInfoType;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.junit.Assert.*;
import travelgoodtypes.OperationSuccessType;
import travelgoodtypes.XItinerary;
/**
 *
 * @author ming
 */
public class OperationsBPEL {
    
    public static  XItinerary getItinerary(String customerID, String itineraryNo){
        travelgood.XGetItineraryQuery getQuery=new travelgood.XGetItineraryQuery();
        getQuery.setCustomerID(customerID);
        getQuery.setItineraryNo(itineraryNo);
        XItinerary  itineraryResult=getItineraryOperation(getQuery);
        return itineraryResult;    
    }
    
      public static  XItinerary readItinerary(String customerID, String itineraryNo){
        travelgood.XReadItineraryQuery readItineraryQuery=new travelgood.XReadItineraryQuery();
         readItineraryQuery.setCustomerID(customerID);
         readItineraryQuery.setItineraryNo(itineraryNo);
        XItinerary  itineraryResult=readItineraryOperation(readItineraryQuery);
        return itineraryResult;    
    }
    
    public static boolean bookItineray(String customerID,String itineraryNo,CreditCardInfoType CreditCard){
        travelgood.XBookQuery bookItineraryQuery = new travelgood.XBookQuery();
        bookItineraryQuery.setCustomerID(customerID);
        bookItineraryQuery.setItineraryNo(itineraryNo);
        bookItineraryQuery.setCreditCardInfo(CreditCard);
        try {             
             bookItineraryOperation(bookItineraryQuery);
             return true;
        } catch (BookItineraryFault ex) {
            System.out.println(""+ex.getFaultInfo());
            //Logger.getLogger(OperationsBPEL.class.getName()).log(Level.SEVERE, null, ex);
            return false;
         
        }
    }
    
    public static List<FlightInfoType> listFlight(String startDest,String finalDest,XMLGregorianCalendar date,String customerID, String itineraryNo){
        travelgood.XFlightQuery xflightQuery= new travelgood.XFlightQuery();
        flightdata.GetFlightQuery flightQuery = new flightdata.GetFlightQuery();
        flightQuery.setDate(date);
        flightQuery.setStartDest(startDest);
        flightQuery.setFinalDest(finalDest);
        xflightQuery.setGetFlightQuery(flightQuery);
        xflightQuery.setItineraryNo(itineraryNo);
        xflightQuery.setCustomerID(customerID);
        return listFlightsOperation(xflightQuery).getFlightInfo();
    }
    
     public static List<HotelType> listHotel(String city,XMLGregorianCalendar startDate,XMLGregorianCalendar endDate,String customerID, String itineraryNo){
        travelgood.XHotelQuery xhotelQuery= new travelgood.XHotelQuery();
        hotelreservationtypes.HotelQuery hq = new hotelreservationtypes.HotelQuery();
        hq.setArrivalDate(startDate);
        hq.setCity(city);
        hq.setDepartureDate(endDate);
        xhotelQuery.setHotelQuery(hq);
        xhotelQuery.setItineraryNo(itineraryNo);
        xhotelQuery.setCustomerID(customerID);
        return listHotelsOperation(xhotelQuery).getHotels();
    }
    
    public static void addFlight(FlightInfoType flight,String customerID, String itineraryNo){
        travelgood.XAddFlightQuery addFlightRequest= new travelgood.XAddFlightQuery();
        addFlightRequest.setItineraryNo(itineraryNo);
        addFlightRequest.setCustomerID(customerID);
        addFlightRequest.setFlight(flight);
        ResultType addFlightResult=addFlightToItineraryOperation(addFlightRequest);
        System.out.println("add flight: "+flight.getBookingNumber()+"  "+addFlightResult.status);
       // assertEquals("success",addFlightResult.status);
    }
       public static void addHotel(HotelType hotel,String customerID, String itineraryNo){
        travelgood.XAddHotelQuery addHotelRequest= new travelgood.XAddHotelQuery();
        addHotelRequest.setItineraryNo(itineraryNo);
        addHotelRequest.setCustomerID(customerID);
        addHotelRequest.setHotel(hotel);
        ResultType addHoteltResult=addHotelToItineraryOperation(addHotelRequest);
        System.out.println("add hotel: "+hotel.getBookingNo()+"  "+addHoteltResult.status);
        assertEquals("success",addHoteltResult.status);
    }
          
    public static ResultType CancelItinerary(String customerID, String itineraryNo) throws CancelItineraryFault{
           travelgood.XCancelItineraryQuery cancelQuery= new travelgood.XCancelItineraryQuery();
           cancelQuery.setCustomerID(customerID);
           cancelQuery.setItineraryNo(itineraryNo);
           ResultType result= cancelItineraryOperation(cancelQuery);
           System.out.println("cancel itinerary : "+result.status);
           return result; 
    }
       public static ResultType CancelBooking(String customerID, String itineraryNo) throws CancelBookingFault{
         travelgood.XCancelBookingQuery cancelBookingQuery= new travelgood.XCancelBookingQuery();
           cancelBookingQuery.setCustomerID(customerID);
           cancelBookingQuery.setItineraryNo(itineraryNo);
           ResultType result= cancelBookingOperation(cancelBookingQuery);       
           System.out.println("result : "+result.status);
           return result; 
    }
    
    
    
   public static FlightInfoList listFlightsOperation(travelgood.XFlightQuery flightQuery) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.listFlightsOperation(flightQuery);
    }

   public static HotelList listHotelsOperation(travelgood.XHotelQuery hotelQueryRequest) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.listHotelsOperation(hotelQueryRequest);
    }

 
   public static ResultType addFlightToItineraryOperation(travelgood.XAddFlightQuery addFlightRequest) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.addFlightToItineraryOperation(addFlightRequest);
    }

   public static ResultType cancelItineraryOperation(travelgood.XCancelItineraryQuery cancelItineraryQuery) throws CancelItineraryFault {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.cancelItineraryOperation(cancelItineraryQuery);
    }

    public static ResultType createItineraryOperation(travelgood.XCreateQuery creatQuery) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.createItineraryOperation(creatQuery);
    }

   public static XItinerary getItineraryOperation(travelgood.XGetItineraryQuery getITQuery) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.getItineraryOperation(getITQuery);
    }

   public static ResultType addHotelToItineraryOperation(travelgood.XAddHotelQuery addHotelRequest) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.addHotelToItineraryOperation(addHotelRequest);
    }

    private static ResultType bookItineraryOperation(travelgood.XBookQuery bookItineraryQuery) throws BookItineraryFault {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.bookItineraryOperation(bookItineraryQuery);
    }

    private static XItinerary readItineraryOperation(travelgood.XReadItineraryQuery readItineraryQuery) {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.readItineraryOperation(readItineraryQuery);
    }


    private static ResultType cancelBookingOperation(travelgood.XCancelBookingQuery cancelBookingQuery) throws CancelBookingFault {
        travelgood.TravelGoodService service = new travelgood.TravelGoodService();
        travelgood.TravelGoodPortType port = service.getTravelGoodPortTypeBindingPort();
        return port.cancelBookingOperation(cancelBookingQuery);
    }
    
}
