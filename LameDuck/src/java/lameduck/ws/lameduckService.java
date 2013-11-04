/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

import javax.jws.WebService;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author s114671
 */
@WebService(serviceName = "lameDuckService", portName = "lameDuckPortTypeBindingPort", endpointInterface = "ws.lameduck.LameDuckPortType", targetNamespace = "http://lameDuck.ws", wsdlLocation = "WEB-INF/wsdl/lameduckService/lameDuckWSDL.wsdl")
public class lameduckService {

     public flightdata.FlightInfoListType getFlights(java.lang.String startDest, java.lang.String finalDest, javax.xml.datatype.XMLGregorianCalendar date) {
        //TODO implement this method
        flightDB fdb=new flightDB();
        return fdb.flightInfoList;
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean bookFlight(int bookingNumber, ws.lameduck.CreditcardInfoType creditcardInfo) throws BookFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean cancelFlight(int bookingNumber, double flightPrice, ws.lameduck.CreditcardInfoType creditcardInfo) throws CancelFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public class flightDB {
        flightdata.FlightInfoListType flightInfoList=new flightdata.FlightInfoListType();
        public flightDB() {

            flightdata.FlightType flight1 = new flightdata.FlightType();
            flight1.setCarrier("SAS");
            flight1.setStartAirpot("Copenhagen, Denmark");
            flight1.setDestinationAirport("London, Heathrow, England");
            flight1.setDatetimeLanding(date("2013-09-18T19:00:00.000+00:00"));
            flight1.setDatetimeLift(date("2013-09-18T17:00:00.000+00:00"));
           
            flightdata.FlightType flight2 = new flightdata.FlightType();
            flight2.setCarrier("SAS");
            flight2.setStartAirpot("Copenhagen, Denmark4122");
            flight2.setDestinationAirport("Beijing, China");           
            flight2.setDatetimeLift(date("2013-12-02T12:00:00.000+00:00"));
            flight2.setDatetimeLanding(date("2013-12-03T19:30:00.000+00:00"));
            
            flightdata.FlightType flight3 = new flightdata.FlightType();
            flight3.setCarrier("British Airways");
            flight3.setStartAirpot("Copenhagen, Denmark");
            flight3.setDestinationAirport("Amsterdam, Netherlands");           
            flight3.setDatetimeLift(date("2013-12-05T12:00:00.000+00:00"));
            flight3.setDatetimeLanding(date("2013-12-05T14:30:00.000+00:00"));
            
            flightdata.FlightType flight4 = new flightdata.FlightType();
            flight4.setCarrier("British Airways");
            flight4.setStartAirpot("Copenhagen, Denmark");
            flight4.setDestinationAirport("Oslo, Norway");           
            flight4.setDatetimeLift(date("2013-12-06T12:00:00.000+00:00"));
            flight4.setDatetimeLanding(date("2013-12-06T14:30:00.000+00:00"));
            
            
            flightdata.FlightInfoType flightInfo1 = new flightdata.FlightInfoType();
            flightInfo1.setBookingNumber(1234567);
            flightInfo1.setFlight(flight1);
            flightInfo1.setFlightPrice(1500.30);
            flightInfo1.setAirlineReservationServiceName("LameDuck");
            
            flightdata.FlightInfoType flightInfo2 = new flightdata.FlightInfoType();
            flightInfo2.setBookingNumber(1234568);
            flightInfo2.setFlight(flight2);
            flightInfo2.setFlightPrice(7500.50);
            flightInfo2.setAirlineReservationServiceName("LameDuck");
            
            flightdata.FlightInfoType flightInfo3 = new flightdata.FlightInfoType();
            flightInfo3.setBookingNumber(1234570);
            flightInfo3.setFlight(flight3);
            flightInfo3.setFlightPrice(1030.99);
            flightInfo3.setAirlineReservationServiceName("LameDuck");
            
            flightdata.FlightInfoType flightInfo4 = new flightdata.FlightInfoType();
            flightInfo4.setBookingNumber(1234571);
            flightInfo4.setFlight(flight4);
            flightInfo4.setFlightPrice(750.00);
            flightInfo4.setAirlineReservationServiceName("LameDuck");
            
            flightInfoList.getFlightInfo().add(flightInfo1);
            flightInfoList.getFlightInfo().add(flightInfo2);
            flightInfoList.getFlightInfo().add(flightInfo3);
            flightInfoList.getFlightInfo().add(flightInfo4);
        }

        public XMLGregorianCalendar date(String dateinput) {
            
            //Demos and Usage of javax.xml.datatype.DatatypeFactory.newXMLGregorianCalendar
            //(int year,int month,int day,int hour,int minute,int second,int millisecond,int timezone)
            DatatypeFactory df = null;
            try {
                df = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException ex) {
                Logger.getLogger(lameduckService.class.getName()).log(Level.SEVERE, null, ex);
            }
            XMLGregorianCalendar date = df.newXMLGregorianCalendar(dateinput);
            return date;

        }
    }
    
}
