import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import hotelreservationtypes.HotelList;
import junit.framework.Assert;
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
    
    public TestItineraryResource() {
    }
    
    @Test
    public void testCreateItineary(){
        Client client = new Client();
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itineraries");        
        String itineraryNo = webResource.post(String.class);
        assertNotNull(itineraryNo);
        
        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itineraries");
        String itineraryNo2 = webResource.post(String.class);
        assertNotNull(itineraryNo2);
        assertThat(itineraryNo, not(equalTo(itineraryNo2)));

        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/" + itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);

        assertEquals(StatusType.UNCONFIRMED, itinerary.getItineraryStatus());
        assertNotNull(itinerary.getItineraryNo());
    }
}