
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import hotelreservationtypes.HotelList;
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

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
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
        webResource.queryParams(param).get(String.class);

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        param = new MultivaluedMapImpl();
        param.add("flightBookingNo", "1234567");
        ClientResponse res = webResource.queryParams(param).put(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertEquals(itineraryNo, itinerary.getItineraryNo());
        assertTrue(itinerary.getFlightBookingList().size() > 0);
    }

    @Test
    public void testAddHotelToItinerary() {
        Client client = new Client();
        WebResource webResource = client.resource(ITINERARIES);
        String itineraryNo = webResource.post(String.class);
        assertNotNull(itineraryNo);

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/hotels");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("city", "Copenhagen");
        param.add("arrivalDate", "2013-12-10");
        param.add("departureDate", "2013-12-22");
        HotelList response = webResource.queryParams(param).get(HotelList.class);
        String firstHotelBookingNo = response.getHotels().get(0).getBookingNo();

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        param = new MultivaluedMapImpl();
        param.add("hotelBookingNo", firstHotelBookingNo);
        webResource.queryParams(param).put(String.class);

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        ClientResponse res = webResource.get(ClientResponse.class);
        Itinerary itinerary = res.getEntity(Itinerary.class);
        
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertEquals(itineraryNo, itinerary.getItineraryNo());
        assertTrue(itinerary.getHotelBookingList().size() > 0);
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

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);

        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertNotNull(itinerary.getItineraryNo());
        
        // cancel
        
        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        ClientResponse res = webResource.delete(ClientResponse.class);
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
    }
    
}