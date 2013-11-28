package lameduck.ws;

import common.DateUtils;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.AccountType;
import dk.dtu.imm.fastmoney.types.CreditCardFaultType;
import flightdata.BookFlightQuery;
import flightdata.CancelFlightQuery;
import flightdata.FlightFaultType;
import flightdata.FlightInfoList;
import flightdata.FlightInfoType;
import flightdata.GetFlightQuery;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;

/**
 *
 * @author AhmadReza
 */
@WebService(serviceName = "lameDuckService", 
        portName = "lameDuckPortTypeBindingPort", 
        endpointInterface = "ws.lameduck.LameDuckPortType", 
        targetNamespace = "http://lameDuck.ws", 
        wsdlLocation = "WEB-INF/wsdl/lameDuckService/lameDuckWSDL.wsdl")
public class LameDuckService {

    private FlightDB fdb = new FlightDB();
    private List<FlightInfoType> bookedFlights = new ArrayList();
    static final int GROUP_NUMBER = 2;
    static final String LAMEDUCK_ACCOUNT_NO = "50208812";

    public FlightInfoList getFlights(GetFlightQuery getFlightQuery) {
        FlightInfoList matchedFlights = new FlightInfoList();
        

        for (FlightInfoType flightInfo : fdb.flightInfoList.getFlightInfo()) {
           
            int day = flightInfo.getFlight().getDatetimeLift().getDay();
            int month = flightInfo.getFlight().getDatetimeLift().getMonth();
            int year = flightInfo.getFlight().getDatetimeLift().getYear();
            XMLGregorianCalendar flightDate = DateUtils.toXmlGregCal(day, month, year);
            
            boolean datesSame = getFlightQuery.getDate().toGregorianCalendar().equals(flightDate.toGregorianCalendar());
            boolean startAirSame = flightInfo.getFlight().getStartAirpot().equals(getFlightQuery.getStartDest());
            boolean destAirSame = flightInfo.getFlight().getDestinationAirport().equals(getFlightQuery.getFinalDest());
            
            if (datesSame && startAirSame && destAirSame) {
                matchedFlights.getFlightInfo().add(flightInfo);
            }
        }
        return matchedFlights;
    }

    public boolean bookFlight(BookFlightQuery bookFlightQuery) throws BookFlightFault {
        AccountType lameDuckAccount = new AccountType();
        lameDuckAccount.setName("LameDuck");
        lameDuckAccount.setNumber(LAMEDUCK_ACCOUNT_NO);
        
        String bookingNumber = bookFlightQuery.getBookingNumber();
                
        FlightInfoType flightInfo = getFlightByBookingNumber(bookingNumber);
        if (flightInfo == null) {
            throw createBookingFault("No flight found", "Booking no:" + bookingNumber);
        } else if (isFlightBooked(flightInfo)) {
            throw createBookingFault("Flight already booked", "Booking no:" + bookingNumber);
        }
        
        int flightPrice = flightInfo.getFlightPrice();
        try {
            validateCreditCard(GROUP_NUMBER, bookFlightQuery.getCreditcardInfo(), flightPrice);
            chargeCreditCard(GROUP_NUMBER, bookFlightQuery.getCreditcardInfo(), flightPrice, lameDuckAccount);
            bookedFlights.add(flightInfo);
            System.out.println("New flight booked, booking number is:  " + bookedFlights.get(0).getBookingNumber());
        } catch (CreditCardFaultMessage ex) {
            CreditCardFaultType fault = ex.getFaultInfo();
            if (fault != null) {
                throw createBookingFault("Could not process credit card: " + fault.getMessage(), fault.getMessage());
            } else {
                throw createBookingFault("Could not process credit card", "Credit card information are invalid");
            }
        }
        return true;
    }

    public boolean cancelFlight(CancelFlightQuery cancelFlightQuery) throws CancelFlightFault {
        AccountType lameDuckAccount = new AccountType();
        lameDuckAccount.setName("LameDuck");
        lameDuckAccount.setNumber("50208812");

        int refundAmount = cancelFlightQuery.getFlightPrice() / 2;
        
        String bookingNumber = cancelFlightQuery.getBookingNumber();

        FlightInfoType flightInfo = findBookedFlight(cancelFlightQuery.getBookingNumber());
        if (flightInfo == null) {
            throw createCancellationFault("No flight found", "Booking no:" + bookingNumber);
        }
        if(!flightInfo.isCancellable()){
            throw createCancellationFault("Flight has non cancellable policy", "Booking no:" + bookingNumber);            
        }
        try {
            refundCreditCard(GROUP_NUMBER, cancelFlightQuery.getCreditcardInfo(), refundAmount, lameDuckAccount);
            System.out.println("Flight with booking number " + bookedFlights.get(0).getBookingNumber() + " has been canceled.");
            bookedFlights.remove(flightInfo);
        } catch (CreditCardFaultMessage ex) {
            CreditCardFaultType fault = ex.getFaultInfo();
            if (fault != null) {
                throw createCancellationFault("Could not process credit card", fault.getMessage());
            } else {
                throw createCancellationFault("Could not process credit card", "Credit card information are invalid");
            }
        }
        return true;
    }
    
    public void reset(String resetQuery){
        bookedFlights.clear();
    }

    public boolean isFlightBooked(FlightInfoType flightInfo) {
        String flightBookingNo = flightInfo.getBookingNumber();
        for (FlightInfoType bookedFlight : bookedFlights) {
            if (flightBookingNo.equals(bookedFlight.getBookingNumber())) {
                return true;
            }
        }
        return false;
    }

    public FlightInfoType getFlightByBookingNumber(String bookingNumber) {
        FlightInfoType thisFlightInfo = new FlightInfoType();
        List<FlightInfoType> fligthInfo = fdb.flightInfoList.getFlightInfo();
        for (int i = 0; i < fligthInfo.size(); i++) {

            FlightInfoType fund_flightInfo = fligthInfo.get(i);

            if (fund_flightInfo.getBookingNumber().equalsIgnoreCase(bookingNumber)) {
                thisFlightInfo = fund_flightInfo;
            }
        }

        return thisFlightInfo;
    }

    public FlightInfoType findBookedFlight(String bookingNumber) {
        for (FlightInfoType bookedFlight : bookedFlights) {
            if (bookedFlight.getBookingNumber().equals(bookingNumber)) {
                return bookedFlight;
            }
        }
        return null;        
    }

    private BookFlightFault createBookingFault(String message, String detail) {
        FlightFaultType flightFault = new FlightFaultType();
        flightFault.setErrorMessage(message);
        flightFault.setErrorDetail(detail);
        return new BookFlightFault(message, flightFault);
    }

    private CancelFlightFault createCancellationFault(String message, String detail) {
        FlightFaultType flightFault = new FlightFaultType();
        flightFault.setErrorMessage(message);
        flightFault.setErrorDetail(detail);
        return new CancelFlightFault(message, flightFault);
    }

    public boolean validateCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount) throws CreditCardFaultMessage {
        dk.dtu.imm.fastmoney.BankService service = new dk.dtu.imm.fastmoney.BankService();
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.validateCreditCard(group, creditCardInfo, amount);
    }

    public boolean chargeCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        dk.dtu.imm.fastmoney.BankService service = new dk.dtu.imm.fastmoney.BankService();
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.chargeCreditCard(group, creditCardInfo, amount, account);
    }

    public boolean refundCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        dk.dtu.imm.fastmoney.BankService service = new dk.dtu.imm.fastmoney.BankService();
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.refundCreditCard(group, creditCardInfo, amount, account);
    }
}
