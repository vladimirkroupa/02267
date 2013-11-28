
import com.sun.jersey.api.client.ClientResponse;
import flightdata.FlightInfoList;
import flightdata.FlightInfoType;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.Before;
import testutil.ItineraryResourceClient;
import testutil.ResponseWrapper;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import travelgoodtypes.FlightBooking;
import travelgoodtypes.HotelBooking;
import travelgoodtypes.Itinerary;
import travelgoodtypes.StatusType;

/**
 *
 * @author prasopes
 */
public class P1TestCase {
 
    private final ItineraryResourceClient client;

    public P1TestCase() {
        this.client = new ItineraryResourceClient();
    }
    
    @Before
    public void clean() {
        client.reset();
    }
    
    /**
     * Planning and booking scenario.
     */
    @Test
    public void testP1() {
        ClientResponse resp;
        
        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        /* first flight */
        
        // list flights
        ResponseWrapper<FlightInfoList> flightsResp = client.listFlights("2013-09-18", "CPH", "LHR");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());

        List<FlightInfoType> flights = flightsResp.entity().getFlightInfo();
        assertFalse(flights.isEmpty());
        
        // add flight
        String flightBookingNo = flights.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        /* first hotel */
        
        // list hotels
        ResponseWrapper<HotelList> hotelsResp = client.listHotels("Luton", "2013-09-18", "2013-12-05");
        assertEquals(Response.Status.OK.getStatusCode(), hotelsResp.status());

        List<HotelType> hotels = hotelsResp.entity().getHotels();
        assertFalse(hotels.isEmpty());
        
        // add hotel
        String hotelBookingNo = hotels.get(0).getBookingNo();
        resp = client.addHotel(itineraryNo, hotelBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

        /* second flight */
        
        // list flights
        ResponseWrapper<FlightInfoList> flightsResp2 = client.listFlights("2013-12-02", "CPH", "PEK");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());

        List<FlightInfoType> flights2 = flightsResp2.entity().getFlightInfo();
        assertFalse(flights.isEmpty());
        
        // add flight
        String flightBookingNo2 = flights2.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo2);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        /* third flight */
        
        // list flights
        ResponseWrapper<FlightInfoList> flightsResp3 = client.listFlights("2014-01-05", "CPH", "FNJ");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());

        List<FlightInfoType> flights3 = flightsResp3.entity().getFlightInfo();
        assertFalse(flights.isEmpty());
        
        // add flight
        String flightBookingNo3 = flights3.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo3);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        
        /* second hotel */
        
        // list hotels
        ResponseWrapper<HotelList> hotelsResp2 = client.listHotels("Copenhagen", "2014-01-12", "2014-01-13");
        assertEquals(Response.Status.OK.getStatusCode(), hotelsResp.status());

        List<HotelType> hotels2 = hotelsResp2.entity().getHotels();
        assertFalse(hotels.isEmpty());
        
        // add hotel
        String hotelBookingNo2 = hotels2.get(0).getBookingNo();
        resp = client.addHotel(itineraryNo, hotelBookingNo2);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        
        // get itinerary 
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertTrue(itinerary.getHotelBookingList().size() == 2);
        assertTrue(itinerary.getFlightBookingList().size() == 3);
        for (FlightBooking flight : itinerary.getFlightBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, flight.getFlightBookingStatus());
        }
        for (HotelBooking hotel : itinerary.getHotelBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, hotel.getHotelBookingStatus());
        }                                
        
        // book itinerary
        ClientResponse res = client.bookItinerary(itineraryNo, "Anne Strandberg", "50408816", "5", "9");
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        // check that booking status is confirmed
        Itinerary itinerary2 = client.getItinerary(itineraryNo).entity();
        assertEquals(StatusType.CONFIRMED, itinerary2.getItineraryStatus());
        for (FlightBooking flight : itinerary2.getFlightBookingList()) {
            assertEquals(StatusType.CONFIRMED, flight.getFlightBookingStatus());
        }
        for (HotelBooking hotel : itinerary2.getHotelBookingList()) {
            assertEquals(StatusType.CONFIRMED, hotel.getHotelBookingStatus());
        }
    }
    
}
