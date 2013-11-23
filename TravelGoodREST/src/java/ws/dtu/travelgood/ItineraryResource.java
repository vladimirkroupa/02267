package ws.dtu.travelgood;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import flightdata.FlightInfoType;
import flightdata.FlightType;
import java.util.ArrayList;
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
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
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
    @Produces (MediaType.TEXT_PLAIN)
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
    
    @PUT
    @Path("itinerary/{itineraryNo}")
    @Produces (MediaType.APPLICATION_XML)    
    public Response addFlight(@PathParam("itineraryNo") String itineraryNo, @QueryParam("flightBookingNo") String flightBookingNo){
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        
        //check in the flightBooking if they already exist
        boolean bookingAlreadyExist = false;
        ArrayList flightBookingList = (ArrayList) itinerary.getFlightBookingList();
        for (Object o : flightBookingList){
            FlightBooking booking = (FlightBooking) o;
            FlightInfoType fInfo = booking.getFlightBooking();
            if(fInfo.getBookingNumber().equals(flightBookingNo)){
                bookingAlreadyExist = true;
                break;
            }
        }
        
        if(!bookingAlreadyExist){
            FlightBooking booking = new FlightBooking();
            
            Client client = new Client();
            WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/flights");
            webResource.accept(MediaType.APPLICATION_XML);
            MultivaluedMap param = new MultivaluedMapImpl();
            param.add("date","2013-09-18");
            param.add("startDest","Copenhagen, Denmark");
            param.add("finalDest","London, Heathrow, England");
            
            
            booking.setFlightBooking(null);
            booking.setFlightBookingStatus(StatusType.UNCONFIRMED);
            itinerary.getFlightBookingList().add(null);
        }
        
        return Response.ok().entity(itinerary).build();
    } 
    
}
