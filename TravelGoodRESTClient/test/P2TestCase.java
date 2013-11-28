
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
public class P2TestCase {
 
    private final ItineraryResourceClient client;

    public P2TestCase() {
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
    public void testP2() {
        ClientResponse resp;
        
        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        /* add flight */
        
        // list flights
        ResponseWrapper<FlightInfoList> flightsResp = client.listFlights("2013-09-18", "CPH", "LHR");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());

        List<FlightInfoType> flights = flightsResp.entity().getFlightInfo();
        assertFalse(flights.isEmpty());
        
        // add flight
        String flightBookingNo = flights.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        /* add hotel */
        
        // list hotels
        ResponseWrapper<HotelList> hotelsResp = client.listHotels("Luton", "2013-09-18", "2013-12-05");
        assertEquals(Response.Status.OK.getStatusCode(), hotelsResp.status());

        List<HotelType> hotels = hotelsResp.entity().getHotels();
        assertFalse(hotels.isEmpty());
        
        // add hotel
        String hotelBookingNo = hotels.get(0).getBookingNo();
        resp = client.addHotel(itineraryNo, hotelBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

        /* check itinerary */
        
        // get itinerary 
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertThat(itinerary.getHotelBookingList().size(), is(1));
        assertThat(itinerary.getFlightBookingList().size(), is(1));
        for (FlightBooking flight : itinerary.getFlightBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, flight.getFlightBookingStatus());
        }
        for (HotelBooking hotel : itinerary.getHotelBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, hotel.getHotelBookingStatus());
        }                                
        
        // cancel itinerary
        resp = client.cancelItinerary(itineraryNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        
        // should not be able to get cancelled itinerary
        ResponseWrapper<Itinerary> itineraryResp = client.getItinerary(itineraryNo);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), itineraryResp.status());
    }
    
}
