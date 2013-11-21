package ws.dtu.travelgood;

import com.sun.mail.iap.Response;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import travelgoodtypes.Itinerary;

/**
 *
 * @author prasopes
 */
@Path("/")
public class ItineraryResource {

    private Map<String, Itinerary> itineraryMap = new HashMap<String, Itinerary>();
    private int itineraryIndex = 1;
    
    @POST
    @Path("itineraries")
    @Produces (MediaType.TEXT_PLAIN)
    public String createItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryNo(""+itineraryIndex);
        itineraryIndex++;
        itineraryMap.put(itinerary.getItineraryNo(),itinerary);
        return itinerary.getItineraryNo();
    }
    
    @GET
    @Path("itinerary/{itineraryNo}")
    @Produces (MediaType.APPLICATION_XML)
    public Itinerary getItinerary(@PathParam("itineraryNo") String itineraryNo) {
        if (! itineraryMap.containsKey(itineraryNo)) {
            throw new IllegalArgumentException("Itinerary doesn't exist.");
        }
        return itineraryMap.get(itineraryNo);
    }
    
}
