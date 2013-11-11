/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

import dk.dtu.imm.fastmoney.BankService;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;

/**
 *
 * @author moonlight
 */
@WebService(serviceName = "lameDuckService", portName = "lameDuckPortTypeBindingPort", endpointInterface = "ws.lameduck.LameDuckPortType", targetNamespace = "http://lameDuck.ws", wsdlLocation = "WEB-INF/wsdl/lameDuckService/lameDuckWSDL.wsdl")
public class LameDuckService {
//
//    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankService.wsdl")
//    private BankService service;
    FlightDB fdb = new FlightDB();
    List<flightdata.FlightInfoType> bookedFlights = new ArrayList();
    flightdata.FlightInfoListType matchedFlights; //= new flightdata.FlightInfoListType();
    int GROUP_NUMBER = 2;
    
    public flightdata.FlightInfoListType getFlights(java.lang.String startDest, java.lang.String finalDest, javax.xml.datatype.XMLGregorianCalendar date) {
        matchedFlights = new flightdata.FlightInfoListType();
        System.out.println("getFlight() called");
        //TODO implement this method
        List<flightdata.FlightInfoType> fligthInfo = fdb.flightInfoList.getFlightInfo();
        for (int i = 0; i < fligthInfo.size(); i++) {

            flightdata.FlightInfoType flightInfo1 = fligthInfo.get(i);
            int day = flightInfo1.getFlight().getDatetimeLift().getDay();
            int month = flightInfo1.getFlight().getDatetimeLift().getMonth();
            int year = flightInfo1.getFlight().getDatetimeLift().getYear();
            // System.out.println("Input: " + date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " Thisflight: " + year + "-" + month + "-" + day);
            //System.out.println("Size: " +fligthInfo.size());
            //System.out.println("Compare Result: " + date.compare(createDate(day, month, year)));
            if (date.compare(createDate(day, month, year)) == 2) { //2 == that this date matches a flight date

                if (flightInfo1.getFlight().getStartAirpot().equalsIgnoreCase(startDest)
                        && flightInfo1.getFlight().getDestinationAirport().equalsIgnoreCase(finalDest)) {

                    matchedFlights.getFlightInfo().add(flightInfo1);
                }
            } else {
                System.out.println("NO FLIGHT FOUND for date,start and dest");
            }
        }
        return matchedFlights;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean bookFlight(java.lang.String bookingNumber, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo) throws BookFlightFault {
        dk.dtu.imm.fastmoney.types.AccountType lameDuckAccount= new dk.dtu.imm.fastmoney.types.AccountType();
        lameDuckAccount.setName("LameDuck");
        lameDuckAccount.setNumber("50208812");
        flightdata.FlightInfoType flightInfo=getFlightByBookingNumber(bookingNumber);
        if(flightInfo==null){
            throw new BookFlightFault("No such flight found by booking number in BOOKING process.",
                  "No such flight found by booking number in BOOKING process.");
        } else if(flight_isBooked(flightInfo)){
            throw new BookFlightFault("The flight is already booked, can't book it again.",
                  "The flight is already booked, can't book it again.");
        }
        int flight_price= flightInfo.getFlightPrice();
        try {
            //TODO implement this method
            validateCreditCard(GROUP_NUMBER, creditcardInfo, flight_price);
            chargeCreditCard(GROUP_NUMBER, creditcardInfo, flight_price,lameDuckAccount);
            bookedFlights.add(flightInfo);
            System.out.println("New flight booked, booking number is:  "+bookedFlights.get(0).getBookingNumber());
            
        } catch (CreditCardFaultMessage ex) {
            System.out.println(ex.getFaultInfo().getMessage());
            return false;
            //Logger.getLogger(lameduckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

   public boolean cancelFlight(java.lang.String bookingNumber, int flightPrice, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditcardInfo) throws CancelFlightFault {
        //TODO implement this method
        dk.dtu.imm.fastmoney.types.AccountType lameDuckAccount= new dk.dtu.imm.fastmoney.types.AccountType();
        lameDuckAccount.setName("LameDuck");
        lameDuckAccount.setNumber("50208812");
        
        int refundAmount=flightPrice/2;
        
        flightdata.FlightInfoType flightInfo=getFlightByBookingNumber_in_booked_flight(bookingNumber);
        if(flightInfo==null){
         throw new CancelFlightFault("No such flight found by booking number in CANCEL process.",
                  "No such flight found by booking number in CANCEL process.");
           // return false;
        }
        try{
        refundCreditCard(GROUP_NUMBER, creditcardInfo, refundAmount, lameDuckAccount);
        System.out.println("Flight has booking number "+bookedFlights.get(0).getBookingNumber()+" is canceled.");
        bookedFlights.remove(flightInfo);
        //System.out.println("Flight has booking number "+bookedFlights.get(0).getBookingNumber()+" is canceled.");
        } catch (CreditCardFaultMessage ex) {
            System.out.println(ex.getFaultInfo().getMessage());
            return false;
            //Logger.getLogger(lameduckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    public boolean flight_isBooked( flightdata.FlightInfoType flightInfo){
       for (int i = 0; i < bookedFlights.size(); i++) {

            flightdata.FlightInfoType fund_flightInfo = bookedFlights.get(i);

            if (fund_flightInfo.getBookingNumber().equalsIgnoreCase(flightInfo.getBookingNumber())) {
                return true;
            }
        }
    return false;
    }
    
    public flightdata.FlightInfoType getFlightByBookingNumber(String bookingNumber) {
        flightdata.FlightInfoType thisFlightInfo = new flightdata.FlightInfoType();
        List<flightdata.FlightInfoType> fligthInfo = fdb.flightInfoList.getFlightInfo();
        for (int i = 0; i < fligthInfo.size(); i++) {

            flightdata.FlightInfoType fund_flightInfo = fligthInfo.get(i);

            if (fund_flightInfo.getBookingNumber().equalsIgnoreCase(bookingNumber)) {
                thisFlightInfo = fund_flightInfo;
            }
        }
     
        return thisFlightInfo;
    }
    
    public flightdata.FlightInfoType getFlightByBookingNumber_in_booked_flight(String bookingNumber) {
        flightdata.FlightInfoType thisFlightInfo =null;
     // System.out.println("size of booked flight "+bookedFlights.size());
        if(bookedFlights.size()>0){    
        for (int i = 0; i < bookedFlights.size(); i++) {

            flightdata.FlightInfoType fund_flightInfo = bookedFlights.get(i);

            if (fund_flightInfo.getBookingNumber().equalsIgnoreCase(bookingNumber)) {
                thisFlightInfo = fund_flightInfo;
            }else{
            thisFlightInfo=null;
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
            Logger.getLogger(LameDuckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        XMLGregorianCalendar date = df.newXMLGregorianCalendar();
        date.setDay(day);
        date.setMonth(month);
        date.setYear(year);
        return date;
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
