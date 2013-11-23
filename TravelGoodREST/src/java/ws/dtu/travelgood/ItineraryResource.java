package ws.dtu.travelgood;

import flightdata.FlightInfoType;
import flightdata.FlightType;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import travelgoodtypes.FlightBooking;

/**
 *
 * @author prasopes
 */
@Path("/")
public class ItineraryResource {
    
    private static Map<String, Itinerary> itineraryMap = new HashMap<String, Itinerary>();
    private static int itineraryNo = 1;

    @POST
    @Path("itineraries")
    @Produces(MediaType.TEXT_PLAIN)
    public String createItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryNo(nextItineraryNo());
        itinerary.setItineraryStatus(StatusType.UNCONFIRMED);

        FlightBooking f = new FlightBooking();
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

        itineraryMap.put(itinerary.getItineraryNo(), itinerary);

        return itinerary.getItineraryNo();
    }

    @POST
    @Path("itinerary/{itineraryNo}")
    @Produces(MediaType.APPLICATION_XML)
    public Itinerary addHotel(
            @PathParam("itineraryNo") String itineraryNo,
            @QueryParam("hotelBookingNo") String hotelBookingNo) {

        validateItineraryNo(itineraryNo);
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        //itinerary.getHotelBookingList().
        
        return null;
    }

//    private void saveOfferedBookings(HotelList bookings) {
//        for (HotelType booking : bookings.getHotels()) {
//            HotelBookingOffers.addOfferedBooking(booking);
//        }
//    }
    
    @GET
    @Path("itinerary/{itineraryNo}")
    @Produces(MediaType.APPLICATION_XML)
    public Itinerary getItinerary(@PathParam("itineraryNo") String itineraryNo) {
        validateItineraryNo(itineraryNo);
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        return itinerary;
    }

    private void validateItineraryNo(String itineraryNo) {
        if (! itineraryMap.containsKey(itineraryNo)) {
            // return HTTP 400
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
    
    private String nextItineraryNo() {
        return String.valueOf(itineraryNo++);
    }

    
    private static boolean bookHotelOperation(hotelreservationtypes.HotelBookingWithCreditCard bookingWithCreditCard) throws BookHotelOperationFault {
        hotelservice._02267.dtu.dk.wsdl.HotelService service = new hotelservice._02267.dtu.dk.wsdl.HotelService();
        hotelservice._02267.dtu.dk.wsdl.HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.bookHotelOperation(bookingWithCreditCard);
    }
}
