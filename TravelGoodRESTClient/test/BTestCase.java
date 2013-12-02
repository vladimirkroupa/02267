
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
public class BTestCase {

    private final ItineraryResourceClient client;

    public BTestCase() {
        this.client = new ItineraryResourceClient();
    }

    @Before
    public void clean() {
        client.reset();
    }

    /**
     * Booking fails scenario.
     */
    @Test
    public void testB() {
        ClientResponse resp;

        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        /* first flight */

        // list flights
        ResponseWrapper<FlightInfoList> flightsResp = client.listFlights("2014-01-01", "CPH", "SIN");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());

        List<FlightInfoType> flights = flightsResp.entity().getFlightInfo();
        assertFalse(flights.isEmpty());

        // add flight
        String flightBookingNo = flights.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

        /* first hotel */

        // list hotels
        ResponseWrapper<HotelList> hotelsResp = client.listHotels("Luton", "2014-01-01", "2014-01-03");
        assertEquals(Response.Status.OK.getStatusCode(), hotelsResp.status());

        List<HotelType> hotels = hotelsResp.entity().getHotels();
        assertFalse(hotels.isEmpty());

        // add hotel
        String hotelBookingNo = hotels.get(0).getBookingNo();
        resp = client.addHotel(itineraryNo, hotelBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

        /* second flight */

        // list flights
        ResponseWrapper<FlightInfoList> flightsResp2 = client.listFlights("2014-01-03", "SIN", "CPH");
        assertEquals(Response.Status.OK.getStatusCode(), flightsResp.status());

        List<FlightInfoType> flights2 = flightsResp2.entity().getFlightInfo();
        assertFalse(flights.isEmpty());

        // add flight
        String flightBookingNo2 = flights2.get(0).getBookingNumber();
        resp = client.addFlight(itineraryNo, flightBookingNo2);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

        // book itinerary
        ClientResponse res = client.bookItinerary(itineraryNo, "Bech Camilla", "50408822", "7", "9");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), res.getStatus());

        // check that booking status is cancelled
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        assertEquals(StatusType.CANCELLED, itinerary.getItineraryStatus());
        for (FlightBooking flight : itinerary.getFlightBookingList()) {
            assertEquals(StatusType.CANCELLED, flight.getFlightBookingStatus());
        }
        for (HotelBooking hotel : itinerary.getHotelBookingList()) {
            assertEquals(StatusType.CANCELLED, hotel.getHotelBookingStatus());
        }
    }
}