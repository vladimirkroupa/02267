/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

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
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;

/**
 *
 * @author AhmadReza
 */
@WebService(serviceName = "lameDuckService", portName = "lameDuckPortTypeBindingPort", endpointInterface = "ws.lameduck.LameDuckPortType", targetNamespace = "http://lameDuck.ws", wsdlLocation = "WEB-INF/wsdl/lameduckService/lameDuckWSDL.wsdl")
public class LameDuckService {

    FlightDB fdb = new FlightDB();
    List<FlightInfoType> bookedFlights = new ArrayList();
    FlightInfoList matchedFlights; //= new FlightInfoListType();
    int GROUP_NUMBER = 2;

    public FlightInfoList getFlights(GetFlightQuery getFlightQuery) {
        matchedFlights = new FlightInfoList();
        System.out.println("getFlight() called");
        //TODO implement this method
        List<FlightInfoType> fligthInfo = fdb.flightInfoList.getFlightInfo();
        for (int i = 0; i < fligthInfo.size(); i++) {

            FlightInfoType flightInfo1 = fligthInfo.get(i);
            int day = flightInfo1.getFlight().getDatetimeLift().getDay();
            int month = flightInfo1.getFlight().getDatetimeLift().getMonth();
            int year = flightInfo1.getFlight().getDatetimeLift().getYear();
            // System.out.println("Input: " + date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " Thisflight: " + year + "-" + month + "-" + day);
            //System.out.println("Size: " +fligthInfo.size());
            //System.out.println("Compare Result: " + date.compare(createDate(day, month, year)));
            if (getFlightQuery.getDate().compare(createDate(day, month, year)) == 2) { //2 == that this date matches a flight date

                if (flightInfo1.getFlight().getStartAirpot().equalsIgnoreCase(getFlightQuery.getStartDest())
                        && flightInfo1.getFlight().getDestinationAirport().equalsIgnoreCase(getFlightQuery.getFinalDest())) {

                    matchedFlights.getFlightInfo().add(flightInfo1);
                }
            } else {
                System.out.println("NO FLIGHT FOUND for date,start and dest");
            }
        }
        return matchedFlights;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean bookFlight(BookFlightQuery bookFlightQuery) throws BookFlightFault {
        AccountType lameDuckAccount = new AccountType();
        lameDuckAccount.setName("LameDuck");
        lameDuckAccount.setNumber("50208812");
        String bookingNumber = bookFlightQuery.getBookingNumber();
        FlightInfoType flightInfo = getFlightByBookingNumber(bookingNumber);
        
        if (flightInfo == null) {
            throw createBookingFault("No flight found", "Booking no:" + bookingNumber);
        } else if (flight_isBooked(flightInfo)) {
            throw createBookingFault("Flight already booked", "Booking no:" + bookingNumber);
        }
        
        int flight_price = flightInfo.getFlightPrice();
        try {
            //TODO implement this method
            validateCreditCard(GROUP_NUMBER, bookFlightQuery.getCreditcardInfo(), flight_price);
            chargeCreditCard(GROUP_NUMBER, bookFlightQuery.getCreditcardInfo(), flight_price, lameDuckAccount);
            bookedFlights.add(flightInfo);
            System.out.println("New flight booked, booking number is:  " + bookedFlights.get(0).getBookingNumber());

        } catch (CreditCardFaultMessage ex) {
            CreditCardFaultType fault = ex.getFaultInfo();
            if (fault != null) {
                throw createBookingFault("Could not process credit card", fault.getMessage());
            } else {
                throw createBookingFault("Could not process credit card", "Credit card information are invalid");
            }
            //Logger.getLogger(LameduckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean cancelFlight(CancelFlightQuery cancelFlightQuery) throws CancelFlightFault {
        //TODO implement this method
        AccountType lameDuckAccount = new AccountType();
        lameDuckAccount.setName("LameDuck");
        lameDuckAccount.setNumber("50208812");

        int refundAmount = cancelFlightQuery.getFlightPrice() / 2;
        
        String bookingNumber = cancelFlightQuery.getBookingNumber();

        FlightInfoType flightInfo = getFlightByBookingNumber_in_booked_flight(cancelFlightQuery.getBookingNumber());
        if (flightInfo == null) {
            throw createCancellationFault("No flight found", "Booking no:" + bookingNumber);
            // return false;
        }
        try {
            refundCreditCard(GROUP_NUMBER, cancelFlightQuery.getCreditcardInfo(), refundAmount, lameDuckAccount);
            System.out.println("Flight has booking number " + bookedFlights.get(0).getBookingNumber() + " is canceled.");
            bookedFlights.remove(flightInfo);
            //System.out.println("Flight has booking number "+bookedFlights.get(0).getBookingNumber()+" is canceled.");
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

    public boolean flight_isBooked(FlightInfoType flightInfo) {
        for (int i = 0; i < bookedFlights.size(); i++) {

            FlightInfoType fund_flightInfo = bookedFlights.get(i);

            if (fund_flightInfo.getBookingNumber().equalsIgnoreCase(flightInfo.getBookingNumber())) {
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

    public FlightInfoType getFlightByBookingNumber_in_booked_flight(String bookingNumber) {
        FlightInfoType thisFlightInfo = null;
        // System.out.println("size of booked flight "+bookedFlights.size());
        if (bookedFlights.size() > 0) {
            for (int i = 0; i < bookedFlights.size(); i++) {

                FlightInfoType fund_flightInfo = bookedFlights.get(i);

                if (fund_flightInfo.getBookingNumber().equalsIgnoreCase(bookingNumber)) {
                    thisFlightInfo = fund_flightInfo;
                } else {
                    thisFlightInfo = null;
                }
            }
        }
        return thisFlightInfo;
    }

    public XMLGregorianCalendar createDate(int day, int month, int year) {

        //Demos and Usage of javax.xml.datatype.DatatypeFactory.newXMLGregorianCalendar
        //(int year,int month,int day,int hour,int minute,int second,int millisecond,int timezone)
        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            // Logger.getLogger(LameDuckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        XMLGregorianCalendar date = df.newXMLGregorianCalendar();
        date.setDay(day);
        date.setMonth(month);
        date.setYear(year);
        return date;
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
