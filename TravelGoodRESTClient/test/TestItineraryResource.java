
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import hotelreservationtypes.HotelList;
import org.junit.Test;
import travelgoodtypes.Itinerary;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henry Lie
 */
public class TestItineraryResource {
    
    public TestItineraryResource() {
    }
    
    @Test
    public void testCreateItineary(){
        Client client = new Client();
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itineraries");
        String itineraryNo = webResource.post(String.class);
        System.out.println(itineraryNo);
        
        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itineraries");
        itineraryNo = webResource.post(String.class);
        System.out.println(itineraryNo);
        
        webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itinerary/"+itineraryNo);
        Itinerary itinerary = webResource.get(Itinerary.class);

        System.out.println(itinerary);        
        String itineraryString = webResource.get(String.class);

        System.out.println(itineraryString);        
        //System.out.println(itinerary.getItineraryStatus());

        
    }
}