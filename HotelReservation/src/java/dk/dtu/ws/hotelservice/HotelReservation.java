/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dtu.ws.hotelservice;

import dk.dtu.ws.hotelservice.domain.HotelRepositoryOld;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.ExpirationDateType;
import dk.dtu.imm.fastmoney.types.ValidateCreditCard;
import dk.dtu.ws.bankservice.client.BankServiceClient;
import dk.dtu.ws.hotelservice.domain.HotelRepository;
import dk.dtu.ws.hotelservice.domain.NiceView;
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.CancelHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.HotelArrayType;
import hotelservice._02267.dtu.dk.wsdl.HotelBookingWithCreditCardType;
import hotelservice._02267.dtu.dk.wsdl.HotelFaultType;
import hotelservice._02267.dtu.dk.wsdl.HotelQueryType;
import java.util.ArrayList;
import java.util.Date;
import javax.jws.WebService;

/**
 *
 * @author Henry Lie
 */
@WebService(serviceName = "hotelService", portName = "hotelServiceSOAPPort", endpointInterface = "hotelservice._02267.dtu.dk.wsdl.HotelServicePortType", targetNamespace = "http://dk.dtu.02267.hotelservice/WSDL", wsdlLocation = "WEB-INF/wsdl/HotelReservation/hotelservice.wsdl")
public class HotelReservation {

    private NiceView niceView = new NiceView();

    public HotelArrayType getHotelsOperation(HotelQueryType hotelQuery) {
        Date from = WSTypeConverter.toDate(hotelQuery.getArrivalDate());
        Date to = WSTypeConverter.toDate(hotelQuery.getDepartureDate());
        return niceView.listHotels(hotelQuery.getCity(), from, to);
    }

    public boolean bookHotelOperation(HotelBookingWithCreditCardType bookingWithCreditCard) throws BookHotelOperationFault, CreditCardFaultMessage {
        return niceView.bookHotel(bookingWithCreditCard.getBookingNumber(), bookingWithCreditCard.getValidateCreditCardInfo().getCreditCardInfo());
    }

    public boolean cancelHotelOperation(String bookingCancellation) throws CancelHotelOperationFault {
        return niceView.cancelBooking(bookingCancellation);
    }
    
    public void resetOperation(String reset) {
        niceView.reset();
    }
    
}
