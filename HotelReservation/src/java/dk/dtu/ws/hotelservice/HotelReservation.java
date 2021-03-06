package dk.dtu.ws.hotelservice;

import common.DateUtils;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.ws.hotelservice.domain.NiceView;
import hotelreservationtypes.HotelBookingWithCreditCard;
import hotelreservationtypes.HotelQuery;
import hotelreservationtypes.HotelList;
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.CancelHotelOperationFault;
import java.util.Date;
import javax.jws.WebService;

/**
 *
 * @author Henry Lie
 */
@WebService(serviceName = "hotelService", portName = "hotelServiceSOAPPort", endpointInterface = "hotelservice._02267.dtu.dk.wsdl.HotelServicePortType", targetNamespace = "http://dk.dtu.02267.hotelservice/WSDL", wsdlLocation = "WEB-INF/wsdl/HotelReservation/hotelservice.wsdl")
public class HotelReservation {

    private NiceView niceView = new NiceView();

    public HotelList getHotelsOperation(HotelQuery hotelQuery) {
        Date from = DateUtils.toDate(hotelQuery.getArrivalDate());
        Date to = DateUtils.toDate(hotelQuery.getDepartureDate());
        return niceView.listHotels(hotelQuery.getCity(), from, to);
    }

    public boolean bookHotelOperation(HotelBookingWithCreditCard bookingWithCreditCard) throws BookHotelOperationFault, CreditCardFaultMessage {
        return niceView.bookHotel(bookingWithCreditCard.getBookingNumber(), bookingWithCreditCard.getCreditCardInfo());
    }

    public boolean cancelHotelOperation(String bookingCancellation) throws CancelHotelOperationFault {
        return niceView.cancelBooking(bookingCancellation);
    }
    
    public void resetOperation(String reset) {
        niceView.reset();
    }
    
}
