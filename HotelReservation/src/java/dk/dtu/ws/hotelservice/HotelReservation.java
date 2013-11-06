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
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.CancelHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.HotelArrayType;
import hotelservice._02267.dtu.dk.wsdl.HotelBookingWithCreditCardType;
import hotelservice._02267.dtu.dk.wsdl.HotelFaultType;
import hotelservice._02267.dtu.dk.wsdl.HotelQueryType;
import java.util.ArrayList;
import javax.jws.WebService;

/**
 *
 * @author Henry Lie
 */
@WebService(serviceName = "hotelService", portName = "hotelServiceSOAPPort", endpointInterface = "hotelservice._02267.dtu.dk.wsdl.HotelServicePortType", targetNamespace = "http://dk.dtu.02267.hotelservice/WSDL", wsdlLocation = "WEB-INF/wsdl/HotelReservation/hotelservice.wsdl")
public class HotelReservation {

    private HotelRepositoryOld hotelRepository = new HotelRepositoryOld();
    private ArrayList<String> bookedHotel = new ArrayList<String>();

    public boolean cancelHotelOperation(String bookingCancellation) throws CancelHotelOperationFault {
        String bookingNo = bookingCancellation;
        //Check if it is valid hotel
        if (hotelRepository.isValidHotel(bookingNo)) {
            //check if there is already existing booking
            if (bookedHotel.contains(bookingNo)) {
                bookedHotel.remove(bookingNo);
                System.out.println("bookedHotel are:" + bookedHotel);
                return true;
            } else {
                HotelFaultType hotelFault = new HotelFaultType();
                hotelFault.setErrorMessage("Booking does not exist");
                hotelFault.setErrorDetail("Booking No: " + bookingNo);
                throw new CancelHotelOperationFault(hotelFault.getErrorMessage(), hotelFault);
            }
        } else {
            HotelFaultType hotelFault = new HotelFaultType();
            hotelFault.setErrorMessage("Invalid hotel specified");
            hotelFault.setErrorDetail("Booking No: " + bookingNo);
            throw new CancelHotelOperationFault(hotelFault.getErrorMessage(), hotelFault);
        }
    }

    public boolean bookHotelOperation(HotelBookingWithCreditCardType bookingWithCreditCard) throws BookHotelOperationFault, CreditCardFaultMessage {

        BankServiceClient bankClient = new BankServiceClient();
        
        boolean isValidCreditCard = false;
        String bookingNo = bookingWithCreditCard.getBookingNumber();
        //Check if it is valid hotel
        if (hotelRepository.isValidHotel(bookingNo)) {
            //check if there is already existing booking
            if (!bookedHotel.contains(bookingNo)) {
                //check if Credit card guarantee is required
                if (hotelRepository.isCCGuaranteeRequired(bookingNo)) {
                    ValidateCreditCard validate = bookingWithCreditCard.getValidateCreditCardInfo();
                    isValidCreditCard = bankClient.validateCreditCard(validate.getGroup(), validate.getCreditCardInfo(), validate.getAmount());
                    bookedHotel.add(bookingNo);
                    return isValidCreditCard;
                } else {
                    bookedHotel.add(bookingNo);
                }
            } else {
                HotelFaultType hotelFault = new HotelFaultType();
                hotelFault.setErrorMessage("Booking already exists");
                hotelFault.setErrorDetail("Booking No: " + bookingNo);
                throw new BookHotelOperationFault(hotelFault.getErrorMessage(), hotelFault);
            }

        } else {
            HotelFaultType hotelFault = new HotelFaultType();
            hotelFault.setErrorMessage("Invalid Hotel Specified");
            hotelFault.setErrorDetail("Booking No: " + bookingNo);
            throw new BookHotelOperationFault(hotelFault.getErrorMessage(), hotelFault);
        }

        return true;
    }

    public HotelArrayType getHotelsOperation(HotelQueryType hotelQuery) {
        return hotelRepository.listHotels(hotelQuery);
    }
    
    public void resetOperation(String reset) {
        bookedHotel.clear();
    }
    
}
