
import com.sun.jersey.api.client.ClientResponse;
import flightdata.FlightInfoList;
import flightdata.FlightInfoType;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import java.util.List;
import javax.ws.rs.core.Response;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import testutil.ItineraryResourceClient;
import testutil.ResponseWrapper;
import travelgoodtypes.FlightBooking;
import travelgoodtypes.HotelBooking;
import travelgoodtypes.Itinerary;
import travelgoodtypes.StatusType;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henry Lie
 */
public class C2TestCase {
    private final ItineraryResourceClient client;

    public C2TestCase() {
       this.client = new ItineraryResourceClient();
    }
        
    @Before
    public void clean() {
        client.reset();
    }
   
    @Test
    public void testC2() {
        ClientResponse resp;
        
        // create itinerary
        String itineraryNo = client.createItinerary().entity();
    
        // list first flights
        ResponseWrapper<FlightInfoList> flightsResp = client.listFlights("2013-12-27", "CPH", "ARL");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());    
        
        List<FlightInfoType> flights = flightsResp.entity().getFlightInfo();
        assertFalse(flights.isEmpty());
        
        // add 1st flight
        String flightBookingNo = flights.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        //list second flight
        flightsResp = client.listFlights("2014-01-05", "CPH", "FNJ");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());      
        
        flights = flightsResp.entity().getFlightInfo();
        assertFalse(flights.isEmpty());
        
        // add 2nd flight which is non cancellable flight
        flightBookingNo = flights.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());  
        
        // list hotels
        ResponseWrapper<HotelList> hotelsResp = client.listHotels("Copenhagen", "2013-12-28", "2013-12-29");
        assertEquals(Response.Status.OK.getStatusCode(), hotelsResp.status());

        List<HotelType> hotels = hotelsResp.entity().getHotels();
        assertFalse(hotels.isEmpty());
        
        //add hotel
        String hotelBookingNo = hotels.get(0).getBookingNo();
        resp = client.addHotel(itineraryNo, hotelBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        //book itinerary
        resp = client.bookItinerary(itineraryNo, "Klinkby Poul", "50408817", "3", "10");
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        //assert that all itinerary status is confirmed and all bookings within it are confirmed
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        assertEquals(StatusType.CONFIRMED,itinerary.getItineraryStatus());
        for(FlightBooking booking : itinerary.getFlightBookingList()){
            assertEquals(StatusType.CONFIRMED,booking.getFlightBookingStatus());
        }
        for(HotelBooking booking : itinerary.getHotelBookingList()){
            assertEquals(StatusType.CONFIRMED,booking.getHotelBookingStatus());
        }        
        
        resp = client.cancelBooking(itineraryNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        //assert that all itinerary status is cancelled and all bookings within it are cancelled
        itinerary = client.getItinerary(itineraryNo).entity();
        assertEquals(StatusType.PARTIALLY_CANCELLED,itinerary.getItineraryStatus());
        for(FlightBooking booking : itinerary.getFlightBookingList()){
            FlightInfoType f = booking.getFlightBooking();
            if(f.isCancellable()){
                assertEquals(StatusType.CANCELLED,booking.getFlightBookingStatus());
            }
            else{
                assertEquals(StatusType.CONFIRMED,booking.getFlightBookingStatus());                
            }
        }
        for(HotelBooking booking : itinerary.getHotelBookingList()){
            assertEquals(StatusType.CANCELLED,booking.getHotelBookingStatus());
        }        
        
    }
}
