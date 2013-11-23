
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.junit.Test;
import travelgoodtypes.Itinerary;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import travelgoodtypes.StatusType;

/**
 *
 * @author Batman bin Suparman
 */
public class TestItineraryResource {

    private static final String ITINERARIES = "http://localhost:8080/TravelGoodREST/webresources/itineraries";
    private static final String ITINERARY = "http://localhost:8080/TravelGoodREST/webresources/itinerary/";

    public TestItineraryResource() {
    }

    @Test
    public void testCreateItineary() {
        Client client = new Client();
        WebResource webResource = client.resource(ITINERARIES);
        String itineraryNo = webResource.post(String.class);
        assertNotNull(itineraryNo);

        webResource = client.resource(ITINERARIES);
        String itineraryNo2 = webResource.post(String.class);
        assertNotNull(itineraryNo2);
        assertThat(itineraryNo, not(equalTo(itineraryNo2)));

        webResource = client.resource(ITINERARY + itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);

        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertNotNull(itinerary.getItineraryNo());
    }

    @Test
    public void testAddFlightToItinerary() {
        Client client = new Client();
        WebResource webResource = client.resource(ITINERARIES);
        String itineraryNo = webResource.post(String.class);
        assertNotNull(itineraryNo);

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/flights");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("date", "2013-09-18");
        param.add("startDest", "Copenhagen, Denmark");
        param.add("finalDest", "London, Heathrow, England");
        ClientResponse res = webResource.queryParams(param).get(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

        webResource = client.resource(ITINERARY + itineraryNo + "/flight/1234567");
        res = webResource.put(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

        webResource = client.resource(ITINERARY + itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertEquals(itineraryNo, itinerary.getItineraryNo());
        assertTrue(itinerary.getFlightBookingList().size() > 0);
    }

    private HotelList listHotels(Client client) {
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/hotels");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("city", "Copenhagen");
        param.add("arrivalDate", "2013-12-10");
        param.add("departureDate", "2013-12-22");
        HotelList response = webResource.queryParams(param).get(HotelList.class);
        return response;
    }
    
    //private 
    
    @Test
    public void testAddHotelToItinerary() {
        Client client = new Client();
        WebResource webResource = client.resource(ITINERARIES);
        String itineraryNo = webResource.post(String.class);
        assertNotNull(itineraryNo);

        HotelList response = listHotels(client);
        String firstHotelBookingNo = response.getHotels().get(0).getBookingNo();

        webResource = client.resource(ITINERARY + itineraryNo + "/hotel/" + firstHotelBookingNo);
        webResource.put(String.class);

        webResource = client.resource(ITINERARY + itineraryNo);
        ClientResponse res = webResource.get(ClientResponse.class);
        Itinerary itinerary = res.getEntity(Itinerary.class);
        
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertEquals(itineraryNo, itinerary.getItineraryNo());
        assertTrue(itinerary.getHotelBookingList().size() > 0);
    }
    
    @Test
    public void testAddBoth() {
        Client client = new Client();
        WebResource webResource = client.resource(ITINERARIES);
        String itineraryNo = webResource.post(String.class);
        assertNotNull(itineraryNo);

        // add hotel
            
        HotelList response = listHotels(client);
        String firstHotelBookingNo = response.getHotels().get(0).getBookingNo();

        webResource = client.resource(ITINERARY + itineraryNo + "/hotel/" + firstHotelBookingNo);
        webResource.put(String.class);     
        
        // add flight
        
        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/flights");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("date", "2013-09-18");
        param.add("startDest", "Copenhagen, Denmark");
        param.add("finalDest", "London, Heathrow, England");
        webResource.queryParams(param).get(ClientResponse.class);

        webResource = client.resource(ITINERARY + itineraryNo + "/flight/1234567");
        ClientResponse res = webResource.put(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

        // add second hotel
        
        response = listHotels(client);
        String secondHotelBookingNo = response.getHotels().get(1).getBookingNo();

        webResource = client.resource(ITINERARY + itineraryNo + "/hotel/" + secondHotelBookingNo);
        webResource.put(String.class);
        
        // check itinerary
        
        webResource = client.resource(ITINERARY + itineraryNo);
        res = webResource.get(ClientResponse.class);
        Itinerary itinerary = res.getEntity(Itinerary.class);
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertTrue(itinerary.getHotelBookingList().size() == 2);
        assertTrue(itinerary.getFlightBookingList().size() == 1);
    }
    
    @Test
    public void testCancelItinerary() {
        Client client = new Client();
        WebResource webResource = client.resource(ITINERARIES);
        String itineraryNo = webResource.post(String.class);
        assertNotNull(itineraryNo);

        webResource = client.resource(ITINERARIES);
        String itineraryNo2 = webResource.post(String.class);
        assertNotNull(itineraryNo2);
        assertThat(itineraryNo, not(equalTo(itineraryNo2)));

        webResource = client.resource(ITINERARY + itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);

        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertNotNull(itinerary.getItineraryNo());
        
        // cancel
        
        webResource = client.resource(ITINERARY + itineraryNo);
        ClientResponse res = webResource.delete(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        webResource = client.resource(ITINERARY + itineraryNo);
        res = webResource.get(ClientResponse.class);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), res.getStatus());
    }
    
    @Test
    public void testBookItinerary() {
        Client client = new Client();
        WebResource webResource = client.resource(ITINERARIES);
        String itineraryNo = webResource.post(String.class);

        // add hotels
        
        HotelList response = listHotels(client);
        
        for (HotelType ht : response.getHotels()) {
            String bookingNo = ht.getBookingNo();
            webResource = client.resource(ITINERARY + itineraryNo + "/hotel/" + bookingNo);
            
            webResource.put(String.class);
        }

        // add flight
        
        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/flights");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("date", "2013-09-18");
        param.add("startDest", "Copenhagen, Denmark");
        param.add("finalDest", "London, Heathrow, England");
        webResource.queryParams(param).get(ClientResponse.class);

        webResource = client.resource(ITINERARY + itineraryNo + "/flight/1234567");
        webResource.put(ClientResponse.class);
        
        // test
        
        webResource = client.resource(ITINERARY + itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertNotNull(itinerary.getItineraryNo());
        assertTrue(itinerary.getFlightBookingList().size() > 0);
        assertTrue(itinerary.getHotelBookingList().size() > 0);
        
        // book
        
        webResource = client.resource(ITINERARY + itineraryNo + "/book");
        ClientResponse res = webResource.post(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        // check booking status
        
        webResource = client.resource(ITINERARY + itineraryNo);
        res = webResource.get(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        itinerary = res.getEntity(Itinerary.class);
        assertEquals(StatusType.CONFIRMED, itinerary.getItineraryStatus());
    }
    
}