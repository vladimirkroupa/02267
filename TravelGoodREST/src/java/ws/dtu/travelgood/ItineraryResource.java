package ws.dtu.travelgood;

import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import travelgoodtypes.Itinerary;

/**
 *
 * @author prasopes
 */
public class ItineraryResource {

    @GET
    @Path("itineraries")
    @Produces (MediaType.APPLICATION_XML)
    public String createItinerary() {
        return "12345";
    }
    
    @GET
    @Path("itinerary/{itineraryNo}")
    @Produces (MediaType.APPLICATION_XML)
    public Itinerary getItinerary(@PathParam("itineraryNo") String itineraryNo) {
        System.out.println(itineraryNo);
        return null;
    }
    
}
