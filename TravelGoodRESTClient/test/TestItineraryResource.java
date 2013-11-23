import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Test;
import travelgoodtypes.Itinerary;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import travelgoodtypes.FlightBooking;
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

        System.out.println(itinerary.getItineraryNo());
        System.out.println(itinerary.getItineraryStatus());
        
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
        param.add("date","2013-09-18");
        param.add("startDest","Copenhagen, Denmark");
        param.add("finalDest","London, Heathrow, England");
        webResource.queryParams(param).get(String.class);
                        
        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        param = new MultivaluedMapImpl();
        param.add("flightBookingNo", "1234567");   
        Itinerary itinerary = webResource.queryParams(param).put(Itinerary.class);
        
        System.out.println(itinerary.getItineraryNo());
        System.out.println(itinerary.getItineraryStatus());
        
        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertNotNull(itinerary.getItineraryNo());
        assertTrue(itinerary.getFlightBookingList().size() > 0);        
    }
}