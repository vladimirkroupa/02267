import com.sun.jersey.api.client.ClientResponse;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import javax.ws.rs.core.Response;
import org.junit.Test;
import travelgoodtypes.Itinerary;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import testutil.ItineraryResourceClient;
import travelgoodtypes.FlightBooking;
import travelgoodtypes.HotelBooking;
import travelgoodtypes.StatusType;
import testutil.ResponseWrapper;

/**
 *
 * @author Batman bin Suparman
 */
public class TestItineraryResource {

    private final ItineraryResourceClient client;

    public TestItineraryResource() {
        this.client = new ItineraryResourceClient();
    }
    
    @Before
    public void clean() {
        client.reset();
    }
    
    @Test
    public void testCreateItineary() {
        // create first itinerary
        ResponseWrapper<String> itineraryNoResp = client.createItinerary();
        assertEquals(Response.Status.OK.getStatusCode(), itineraryNoResp.status());
        String itineraryNo = itineraryNoResp.entity();

        // create second itinerary
        itineraryNoResp = client.createItinerary();
        assertEquals(Response.Status.OK.getStatusCode(), itineraryNoResp.status());
        String itineraryNo2 = itineraryNoResp.entity();

        // itinerary numbers must not be same
        assertThat(itineraryNo, not(equalTo(itineraryNo2)));

        // get itinerary
        ResponseWrapper<Itinerary> itineraryResp = client.getItinerary(itineraryNo);
        Itinerary itinerary = itineraryResp.entity();

        assertNotNull(itinerary.getItineraryNo());
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());        
    }

    @Test
    public void testAddFlightToItinerary() {
        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        // list flights
        ResponseWrapper respWrapper = client.listFlights("2013-09-18", "CPH", "LHR");
        assertEquals(Response.Status.OK.getStatusCode(), respWrapper.status());

        // add flight
        ClientResponse resp = client.addFlight(itineraryNo, "1234567");
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

        // get itinerary
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertEquals(itineraryNo, itinerary.getItineraryNo());
        assertTrue(itinerary.getFlightBookingList().size() == 1);
        for (FlightBooking flight : itinerary.getFlightBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, flight.getFlightBookingStatus());
        }
    }

    @Test
    public void testAddHotelToItinerary() {
        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        // list hotels
        ResponseWrapper<HotelList> respWrapper = client.listHotels("Copenhagen", "2013-12-10", "2013-12-22");
        assertEquals(Response.Status.OK.getStatusCode(), respWrapper.status());
        HotelList response = respWrapper.entity();
        
        // add hotel
        String firstHotelBookingNo = response.getHotels().get(0).getBookingNo();
        ClientResponse resp = client.addHotel(itineraryNo, firstHotelBookingNo);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());

        // get itinerary 
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertEquals(itineraryNo, itinerary.getItineraryNo());
        assertTrue(itinerary.getHotelBookingList().size() == 1);
        for (FlightBooking flight : itinerary.getFlightBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, flight.getFlightBookingStatus());
        }
    }
    
    @Test
    public void testAddBoth() {
        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        // add first hotel
        HotelList hotelList = client.listHotels("Copenhagen", "2013-09-17", "2013-09-18").entity();        
        String firstHotelBookingNo = hotelList.getHotels().get(0).getBookingNo();
        client.addHotel(itineraryNo, firstHotelBookingNo);
        
        // add flight
        client.listFlights("2013-09-18", "CPH", "LHR");
        client.addFlight(itineraryNo, "1234567");
        
        // add second hotel
        HotelList hotelList2 = client.listHotels("Herlev", "2013-09-18", "2013-09-20").entity();
        String secondHotelBookingNo = hotelList2.getHotels().get(1).getBookingNo();
        client.addHotel(itineraryNo, secondHotelBookingNo);

        // get itinerary 
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertTrue(itinerary.getHotelBookingList().size() == 2);
        assertTrue(itinerary.getFlightBookingList().size() == 1);
        for (FlightBooking flight : itinerary.getFlightBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, flight.getFlightBookingStatus());
        }
        for (HotelBooking hotel : itinerary.getHotelBookingList()) {
            assertEquals(StatusType.UNCONFIRMED, hotel.getHotelBookingStatus());
        }                                
    }
    
    @Test
    public void testCancelEmptyItinerary() {
        // create itinerary
        String itineraryNo = client.createItinerary().entity();
        
        // get itinerary 
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        
        assertNotNull(itinerary.getItineraryNo());
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        
        // cancel itinerary
        client.cancelItinerary(itineraryNo);
        
        // get itinerary again
        ResponseWrapper<Itinerary> itineraryResp = client.getItinerary(itineraryNo);
        
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), itineraryResp.status());
    }
    
    @Test
    public void testBookItineraryAllSucceed() {
        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        // add hotels
        HotelList hotelList = client.listHotels("Copenhagen", "2013-09-17", "2013-09-18").entity();
        for (HotelType hotel : hotelList.getHotels()) {
            client.addHotel(itineraryNo, hotel.getBookingNo());
        }
        
        // add flight
        client.listFlights("2013-09-18", "CPH", "LHR");
        client.addFlight(itineraryNo, "1234567");
        
        // book itinerary
        ClientResponse res = client.bookItinerary(itineraryNo, "Anne Strandberg", "50408816", "5", "9");
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        // check that booking status is confirmed
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        assertEquals(StatusType.CONFIRMED, itinerary.getItineraryStatus());
        for (FlightBooking flight : itinerary.getFlightBookingList()) {
            assertEquals(StatusType.CONFIRMED, flight.getFlightBookingStatus());
        }
        for (HotelBooking hotel : itinerary.getHotelBookingList()) {
            assertEquals(StatusType.CONFIRMED, hotel.getHotelBookingStatus());
        }
    }
    
    @Test
    public void testBookItineraryHotelBookingFailure() {
        // create itinerary
        String itineraryNo = client.createItinerary().entity();

        // add hotel
        HotelList hotelList = client.listHotels("Copenhagen", "2013-09-17", "2013-09-19").entity();
        String bookingNo = hotelList.getHotels().get(0).getBookingNo();
        client.addHotel(itineraryNo, bookingNo);
        
        // add flight
        client.listFlights("2013-09-18", "CPH", "LHR");
        client.addFlight(itineraryNo, "1234567");
        
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
        
    @Test
    public void testCancelBooking() {
        // create itinerary
        String itineraryNo = client.createItinerary().entity(); 
        
        // add hotel
        HotelList hotelList = client.listHotels("Lyngby", "2013-09-17", "2013-09-19").entity();
        String bookingNo1 = hotelList.getHotels().get(0).getBookingNo();
        String bookingNo2 = hotelList.getHotels().get(1).getBookingNo();
        client.addHotel(itineraryNo, bookingNo1);
        client.addHotel(itineraryNo, bookingNo2);
           
        client.listFlights("2014-01-05", "CPH", "FNJ");
        client.addFlight(itineraryNo, "8001001");        
        
        // book itinerary
        ClientResponse res = client.bookItinerary(itineraryNo, "Klinkby Poul", "50408817", "3", "10");
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());        
        
        // check that booking status is confirmed
        Itinerary itinerary = client.getItinerary(itineraryNo).entity();
        assertEquals(StatusType.CONFIRMED, itinerary.getItineraryStatus());  
        
        // cancel booking
        res = client.cancelBooking(itineraryNo);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        // check that booking status is partially cancelled
        itinerary = client.getItinerary(itineraryNo).entity();
        assertEquals(StatusType.PARTIALLY_CANCELLED, itinerary.getItineraryStatus());          
                     
    }

}