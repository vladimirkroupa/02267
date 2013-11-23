package ws.dtu.travelgood;

import flightdata.FlightInfoType;
import flightdata.FlightType;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import travelgoodtypes.Itinerary;
import travelgoodtypes.StatusType;
import java.util.logging.Logger;
import travelgoodtypes.FlightBooking;


/**
 *
 * @author prasopes
 */
@Path("/")
public class ItineraryResource {

    static Map<String,Itinerary> itineraryMap = new HashMap<String,Itinerary>();
    static int itineraryIndex = 1;
    private Logger log = Logger.getLogger(ItineraryResource.class.getName());

    
    @POST
    @Path("itineraries")
    @Produces (MediaType.APPLICATION_XML)
    public String createItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryNo(""+itineraryIndex);
        itinerary.setItineraryStatus(StatusType.UNCONFIRMED);
        FlightBooking f= new FlightBooking();
        
        FlightInfoType fInfo = new FlightInfoType();

        FlightType flight = new FlightType();
        flight.setCarrier("TestCarrier");
        flight.setStartAirpot("Copenhagen");
        flight.setDestinationAirport("Stockholm");
        
        fInfo.setBookingNumber("0001");
        fInfo.setAirlineReservationServiceName("Test");        
        fInfo.setFlight(flight);
        fInfo.setFlightPrice(1000);
        
        f.setFlightBooking(fInfo);
        f.setFlightBookingStatus(StatusType.CONFIRMED);
        //itinerary.getFlightBookingList().add();
        itineraryIndex++;
        itineraryMap.put(itinerary.getItineraryNo(),itinerary);
        return itinerary.getItineraryNo();
    }
 
    @GET
    @Path("itinerary/{itineraryNo}")
    @Produces (MediaType.APPLICATION_XML)
    public Itinerary getItinerary(@PathParam("itineraryNo") String itineraryNo) {
        
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        return itinerary;

    }
    
    
}
