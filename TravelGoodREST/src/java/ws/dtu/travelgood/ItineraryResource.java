package ws.dtu.travelgood;

import com.google.common.base.Preconditions;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.ExpirationDateType;
import flightdata.BookFlightQuery;
import flightdata.FlightInfoType;
import hotelreservationtypes.HotelBookingWithCreditCard;
import hotelreservationtypes.HotelType;
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import travelgoodtypes.Itinerary;
import travelgoodtypes.StatusType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import travelgoodtypes.FlightBooking;
import travelgoodtypes.HotelBooking;
import ws.lameduck.BookFlightFault;

/**
 *
 * @author prasopes
 */
@Path("/")
public class ItineraryResource {
    
    private static Map<String, Itinerary> itineraryMap;
    private static int itineraryNo;
    private static Map<String, CreditCardInfoType> itineraryNoToCreditCardMap;
    
    static {
        itineraryNo = 1;
        itineraryMap = new HashMap<String, Itinerary>();
        itineraryNoToCreditCardMap = new HashMap<String, CreditCardInfoType>();
    }

    @POST
    @Path("itineraries")
    @Produces(MediaType.TEXT_PLAIN)
    public String createItinerary() {
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryNo(nextItineraryNo());
        itinerary.setItineraryStatus(StatusType.UNCONFIRMED);
       
        itineraryMap.put(itinerary.getItineraryNo(), itinerary);
        return itinerary.getItineraryNo();
    }
    
    @POST
    @Path("itinerary/{itineraryNo}/book")    
    public Response bookItinerary(@PathParam("itineraryNo") String itineraryNo, 
        @QueryParam("expMonth") Integer expMonth,
        @QueryParam("expYear") Integer expYear,
        @QueryParam("ccName") String ccName,
        @QueryParam("ccNumber") String ccNumber){

        
        Preconditions.checkNotNull(ccName, "Card holder name must not be null.");
        Preconditions.checkNotNull(ccNumber, "Card number must not be null.");
        Preconditions.checkNotNull(expYear, "Card expiration year must not be null");
        Preconditions.checkNotNull(expMonth, "Card expiration month must not be null");
        
        validateItineraryNo(itineraryNo);
        
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        CreditCardInfoType cc = toCreditCardInfoType(ccName, ccNumber, expMonth, expYear);
        
        //store the customers credit card so that it can be used for cancellation
        itineraryNoToCreditCardMap.put(itineraryNo, cc);
        
        for (FlightBooking booking : itinerary.getFlightBookingList()) {
            BookFlightQuery query = new BookFlightQuery();
            query.setBookingNumber(itineraryNo);
            query.setCreditcardInfo(cc);
            try {
                boolean success = bookFlight(query);
                if (success) {
                    booking.setFlightBookingStatus(StatusType.CONFIRMED);
                }
            } catch (BookFlightFault ex) {
                Logger.getLogger(ItineraryResource.class.getName()).log(Level.SEVERE, null, ex);
                //todo: handle exceptions
            }
        }
        
        for (HotelBooking booking : itinerary.getHotelBookingList()) {
            HotelBookingWithCreditCard query = new HotelBookingWithCreditCard();
            query.setBookingNumber(itineraryNo);
            query.setCreditCardInfo(cc);
            try {
                boolean success = bookHotelOperation(query);
                if (success) {
                    booking.setHotelBookingStatus(StatusType.CONFIRMED);
                }                
            } catch (BookHotelOperationFault ex) {
                Logger.getLogger(ItineraryResource.class.getName()).log(Level.SEVERE, null, ex);
                //todo: handle exceptions
            }
        }    
        
        itinerary.setItineraryStatus(StatusType.CONFIRMED);
        
        return Response.ok().build();
    }

    private CreditCardInfoType toCreditCardInfoType(String ccName, String ccNumber, int expMonth, int expYear) {
        CreditCardInfoType cc = new CreditCardInfoType();
        ExpirationDateType dt= new ExpirationDateType();
        cc.setName(ccName);
        dt.setMonth(expMonth);
        dt.setYear(expYear);
        cc.setExpirationDate(dt);
        cc.setNumber(ccNumber);    
        return cc;
    }
            
            
    
    @PUT
    @Path("itinerary/{itineraryNo}/hotel/{hotelBookingNo}")
    @Produces(MediaType.APPLICATION_XML)
    public Response addHotel(
            @PathParam("itineraryNo") String itineraryNo,
            @PathParam("hotelBookingNo") String hotelBookingNo) {

        validateItineraryNo(itineraryNo);
                
        HotelType hotelBooking = findOfferedBooking(hotelBookingNo);        
        if (hotelBooking == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        HotelBooking hb = new HotelBooking();
        hb.setHotelBookingStatus(StatusType.UNCONFIRMED);
        hb.setHotelBooking(hotelBooking);
        itinerary.getHotelBookingList().add(hb);        
        System.out.println(itineraryMap.get(itineraryNo));

        return Response.ok().build();
    }

    private HotelType findOfferedBooking(String bookingNo) {
        return HotelBookingOffers.findOfferedBooking(bookingNo);
    }
    
    @PUT
    @Path("itinerary/{itineraryNo}/flight/{flightBookingNo}")
    @Produces(MediaType.APPLICATION_XML)
    public Response addFlight(@PathParam("itineraryNo") String itineraryNo, @PathParam("flightBookingNo") String flightBookingNo) {
        validateItineraryNo(itineraryNo);

        Itinerary itinerary = itineraryMap.get(itineraryNo);

        //check in the flightBooking if they already exist
        boolean bookingAlreadyExist = false;
        for (FlightBooking booking : itinerary.getFlightBookingList()) {
            FlightInfoType fInfo = booking.getFlightBooking();
            if (fInfo.getBookingNumber().equals(flightBookingNo)) {
                bookingAlreadyExist = true;
                break;
            }
        }

        if (!bookingAlreadyExist) {
            FlightInfoType flight = FlightBookingData.findOfferedBooking(flightBookingNo);

            if (flight != null) {
                FlightBooking booking = new FlightBooking();
                booking.setFlightBooking(flight);
                booking.setFlightBookingStatus(StatusType.UNCONFIRMED);
                itinerary.getFlightBookingList().add(booking);
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("itinerary/{itineraryNo}")
    @Produces(MediaType.APPLICATION_XML)
    public Itinerary getItinerary(@PathParam("itineraryNo") String itineraryNo) {
        validateItineraryNo(itineraryNo);
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        return itinerary;
    }

    @DELETE
    @Path("itinerary/{itineraryNo}")
    public Response cancelItinerary(@PathParam("itineraryNo") String itineraryNo) {
        validateItineraryNo(itineraryNo);
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        if (! itinerary.getItineraryStatus().equals(StatusType.UNCONFIRMED)) {
            // return HTTP 400
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        itineraryMap.remove(itineraryNo);
        return Response.ok().build();
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

    private static boolean bookFlight(flightdata.BookFlightQuery bookFlightQuery) throws BookFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.bookFlight(bookFlightQuery);
    }

}
