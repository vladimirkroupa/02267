package ws.dtu.travelgood;

import common.DateUtils;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.ExpirationDateType;
import flightdata.BookFlightQuery;
import flightdata.CancelFlightQuery;
import flightdata.FlightInfoType;
import hotelreservationtypes.HotelBookingWithCreditCard;
import hotelreservationtypes.HotelType;
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.CancelHotelOperationFault;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import ws.lameduck.CancelFlightFault;

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
    
    @GET
    @Path("itinerary/{itineraryNo}")
    @Produces(MediaType.APPLICATION_XML)
    public Itinerary getItinerary(@PathParam("itineraryNo") String itineraryNo) {
        validateItineraryNo(itineraryNo);
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        return itinerary;
    }
    
    @POST
    @Path("itinerary/{itineraryNo}/book")
    public Response bookItinerary(@PathParam("itineraryNo") String itineraryNo,
            @QueryParam("ccName") String ccName,
            @QueryParam("ccNumber") String ccNumber,
            @QueryParam("expMonth") Integer expMonth,
            @QueryParam("expYear") Integer expYear) {

        if (ccName == null) {
            throw new WebApplicationException(new IllegalArgumentException("Card holder name must not be null."), Response.Status.BAD_REQUEST);
        }
        if (ccNumber == null) {
            throw new WebApplicationException(new IllegalArgumentException("Card number must not be null."), Response.Status.BAD_REQUEST);
        }
        if (expMonth == null) {
            throw new WebApplicationException(new IllegalArgumentException("Card expiration month must not be null"), Response.Status.BAD_REQUEST);
        }
        if (expYear == null) {
            throw new WebApplicationException(new IllegalArgumentException("Card expiration year must not be null"), Response.Status.BAD_REQUEST);
        }

        validateItineraryNo(itineraryNo);

        Itinerary itinerary = itineraryMap.get(itineraryNo);
        CreditCardInfoType cc = toCreditCardInfoType(ccName, ccNumber, expMonth, expYear);

        //store the customers credit card so that it can be used for cancellation
        itineraryNoToCreditCardMap.put(itineraryNo, cc);

        boolean flightsBooked = bookFlight(itinerary.getFlightBookingList(), cc);
        boolean hotelsBooked = bookHotels(itinerary.getHotelBookingList(), cc);

        if (flightsBooked && hotelsBooked) {
            itinerary.setItineraryStatus(StatusType.CONFIRMED);
            return Response.ok().build();
        } else {
            cancelBooking(itineraryNo);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
    
    private boolean bookFlight(List<FlightBooking> flights, CreditCardInfoType ccInfo) {
        for (FlightBooking booking : flights) {
            BookFlightQuery query = new BookFlightQuery();
            query.setBookingNumber(booking.getFlightBooking().getBookingNumber());
            query.setCreditcardInfo(ccInfo);
            try {
                bookFlight(query);
                booking.setFlightBookingStatus(StatusType.CONFIRMED);
            } catch (BookFlightFault ex) {
                Logger.getLogger(ItineraryResource.class.getName()).log(Level.INFO, "Fault from bookFlight operation", ex);
                return false;
            }
        }
        return true;
    }
    
    private boolean bookHotels(List<HotelBooking> hotels, CreditCardInfoType ccInfo) {
        for (HotelBooking booking : hotels) {
            HotelBookingWithCreditCard query = new HotelBookingWithCreditCard();
            query.setBookingNumber(booking.getHotelBooking().getBookingNo());
            query.setCreditCardInfo(ccInfo);
            try {
                bookHotelOperation(query);
                booking.setHotelBookingStatus(StatusType.CONFIRMED);
            } catch (BookHotelOperationFault ex) {
                Logger.getLogger(ItineraryResource.class.getName()).log(Level.INFO, "Fault from bookHotel operation", ex);
                return false;
            }
        }
        return true;
    }

    private CreditCardInfoType toCreditCardInfoType(String ccName, String ccNumber, int expMonth, int expYear) {
        CreditCardInfoType cc = new CreditCardInfoType();
        ExpirationDateType dt = new ExpirationDateType();
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

    @DELETE
    @Path("itinerary/{itineraryNo}")
    public Response cancelItinerary(@PathParam("itineraryNo") String itineraryNo) {
        validateItineraryNo(itineraryNo);
        Itinerary itinerary = itineraryMap.get(itineraryNo);
        if (!itinerary.getItineraryStatus().equals(StatusType.UNCONFIRMED)) {
            // return HTTP 400
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        itineraryMap.remove(itineraryNo);
        return Response.ok().build();
    }

    @POST
    @Path("itinerary/{itineraryNo}/cancelBooking")
    public Response cancelBooking(@PathParam("itineraryNo") String itineraryNo) {
        CreditCardInfoType creditCard = itineraryNoToCreditCardMap.get(itineraryNo);
        Itinerary itinerary = itineraryMap.get(itineraryNo);

        if (!validateCancellationAllowed(itinerary)) {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
        int numberOfFlights = itinerary.getFlightBookingList().size();
        int numberOfHotels = itinerary.getHotelBookingList().size();
        int cancelledFlights = 0;
        int cancelledHotels = 0;
        for (FlightBooking booking : itinerary.getFlightBookingList()) {
            if (booking.getFlightBookingStatus() == StatusType.CONFIRMED) {
                FlightInfoType flight = booking.getFlightBooking();
                CancelFlightQuery query = new CancelFlightQuery();
                query.setBookingNumber(flight.getBookingNumber());
                query.setCreditcardInfo(creditCard);
                query.setFlightPrice(flight.getFlightPrice());
                try {
                    boolean cancelStatus = cancelFlight(query);
                    if (cancelStatus) {
                        booking.setFlightBookingStatus(StatusType.CANCELLED);
                        cancelledFlights++;
                    }
                } catch (CancelFlightFault ex) {
                    Logger.getLogger(ItineraryResource.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        for (HotelBooking booking : itinerary.getHotelBookingList()) {
            if (booking.getHotelBookingStatus() == StatusType.CONFIRMED) {
                HotelType hotel = booking.getHotelBooking();
                try {
                    boolean cancelStatus = cancelHotelOperation(hotel.getBookingNo());
                    if (cancelStatus) {
                        booking.setHotelBookingStatus(StatusType.CANCELLED);
                        cancelledHotels++;
                    }
                } catch (CancelHotelOperationFault ex) {
                    Logger.getLogger(ItineraryResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (cancelledFlights == numberOfFlights && cancelledHotels == numberOfHotels) {
            itinerary.setItineraryStatus(StatusType.CANCELLED);
        } else {
            itinerary.setItineraryStatus(StatusType.PARTIALLY_CANCELLED);
        }
        return Response.ok().build();
    }
    
    private boolean validateCancellationAllowed(Itinerary itinerary){
        Date earliestFlightDate=null;
        for (FlightBooking booking : itinerary.getFlightBookingList()) {
            Date flightDate = DateUtils.toDate(booking.getFlightBooking().getFlight().getDatetimeLift());
            if(earliestFlightDate==null){
                earliestFlightDate = flightDate;
            }
            if(earliestFlightDate.compareTo(flightDate)>0){
                earliestFlightDate = flightDate;
            }
        }        
        
        Date curDate = new Date();
        final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
        int diffInDays = (int) ((earliestFlightDate.getTime()-curDate.getTime())/ DAY_IN_MILLIS );
        if(diffInDays<2){
            System.out.println("Validation error due to first flight in "+ earliestFlightDate+
                    " which is less than 2 days from current date");
            return false;
        }
        return true;
    }

    @DELETE
    public void reset() {
        HotelBookingOffers.reset();
        FlightBookingData.reset();
        
        itineraryMap.clear();
        itineraryNoToCreditCardMap.clear();
        itineraryNo = 1;
        
        resetNiceView("");
        resetLameDuck("");
    }
    
    private void validateItineraryNo(String itineraryNo) {
        if (!itineraryMap.containsKey(itineraryNo)) {
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

    private static boolean cancelFlight(flightdata.CancelFlightQuery cancelFlightQuery) throws CancelFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.cancelFlight(cancelFlightQuery);
    }

    private static boolean cancelHotelOperation(java.lang.String bookingCancellation) throws CancelHotelOperationFault {
        hotelservice._02267.dtu.dk.wsdl.HotelService service = new hotelservice._02267.dtu.dk.wsdl.HotelService();
        hotelservice._02267.dtu.dk.wsdl.HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.cancelHotelOperation(bookingCancellation);
    }

    private static void resetNiceView(java.lang.String reset) {
        hotelservice._02267.dtu.dk.wsdl.HotelService service = new hotelservice._02267.dtu.dk.wsdl.HotelService();
        hotelservice._02267.dtu.dk.wsdl.HotelServicePortType port = service.getHotelServiceSOAPPort();
        port.resetOperation(reset);
    }

    private static void resetLameDuck(java.lang.String resetQuery) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        port.reset(resetQuery);
    }
    
}
