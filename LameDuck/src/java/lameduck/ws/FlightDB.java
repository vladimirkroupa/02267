/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author moonlight
 */
 public class FlightDB {

        flightdata.FlightInfoListType flightInfoList = new flightdata.FlightInfoListType();

        public FlightDB() {

            flightdata.FlightType flight1 = new flightdata.FlightType();
            flight1.setCarrier("SAS");
            flight1.setStartAirpot("Copenhagen, Denmark");
            flight1.setDestinationAirport("London, Heathrow, England");
            flight1.setDatetimeLift(date("2013-09-18T17:00:00.000+00:00"));
            flight1.setDatetimeLanding(date("2013-09-18T19:00:00.000+00:00"));

            flightdata.FlightType flight2 = new flightdata.FlightType();
            flight2.setCarrier("SAS");
            flight2.setStartAirpot("Copenhagen, Denmark");
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

            flightdata.FlightType flight5 = new flightdata.FlightType();
            flight5.setCarrier("British Airways");
            flight5.setStartAirpot("Copenhagen, Denmark");
            flight5.setDestinationAirport("Oslo, Norway");
            flight5.setDatetimeLift(date("2013-12-06T12:00:00.000+00:00"));
            flight5.setDatetimeLanding(date("2013-12-06T14:30:00.000+00:00"));

            flightdata.FlightType flight6 = new flightdata.FlightType();
            flight6.setCarrier("British Airways");
            flight6.setStartAirpot("Copenhagen, Denmark");
            flight6.setDestinationAirport("Oslo, Norway");
            flight6.setDatetimeLift(date("2013-12-06T12:00:00.000+00:00"));
            flight6.setDatetimeLanding(date("2013-12-06T14:30:00.000+00:00"));



            flightdata.FlightInfoType flightInfo1 = new flightdata.FlightInfoType();
            flightInfo1.setBookingNumber("1234567");
            flightInfo1.setFlight(flight1);
            flightInfo1.setFlightPrice(1500);
            flightInfo1.setAirlineReservationServiceName("LameDuck");

            flightdata.FlightInfoType flightInfo2 = new flightdata.FlightInfoType();
            flightInfo2.setBookingNumber("1234568");
            flightInfo2.setFlight(flight2);
            flightInfo2.setFlightPrice(7500);
            flightInfo2.setAirlineReservationServiceName("LameDuck");

            flightdata.FlightInfoType flightInfo3 = new flightdata.FlightInfoType();
            flightInfo3.setBookingNumber("1234570");
            flightInfo3.setFlight(flight3);
            flightInfo3.setFlightPrice(1030);
            flightInfo3.setAirlineReservationServiceName("LameDuck");

            flightdata.FlightInfoType flightInfo4 = new flightdata.FlightInfoType();
            flightInfo4.setBookingNumber("1234571");
            flightInfo4.setFlight(flight4);
            flightInfo4.setFlightPrice(750);
            flightInfo4.setAirlineReservationServiceName("LameDuck");

            flightdata.FlightInfoType flightInfo5 = new flightdata.FlightInfoType();
            flightInfo5.setBookingNumber("1234572");
            flightInfo5.setFlight(flight5);
            flightInfo5.setFlightPrice(750);
            flightInfo5.setAirlineReservationServiceName("LameDuck");

            flightdata.FlightInfoType flightInfo6 = new flightdata.FlightInfoType();
            flightInfo6.setBookingNumber("1234573");
            flightInfo6.setFlight(flight6);
            flightInfo6.setFlightPrice(750);
            flightInfo6.setAirlineReservationServiceName("LameDuck");

            flightInfoList.getFlightInfo().add(flightInfo1);
            flightInfoList.getFlightInfo().add(flightInfo2);
            flightInfoList.getFlightInfo().add(flightInfo3);
            flightInfoList.getFlightInfo().add(flightInfo4);
            flightInfoList.getFlightInfo().add(flightInfo5);
            flightInfoList.getFlightInfo().add(flightInfo6);

        }

        public XMLGregorianCalendar date(String dateinput) {

            //Demos and Usage of javax.xml.datatype.DatatypeFactory.newXMLGregorianCalendar
            //(int year,int month,int day,int hour,int minute,int second,int millisecond,int timezone)
            DatatypeFactory df = null;
            try {
                df = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException ex) {
               // Logger.getLogger(LameDuckService.class.getName()).log(Level.SEVERE, null, ex);
            }
            XMLGregorianCalendar date = df.newXMLGregorianCalendar(dateinput);
            return date;
        }
    }
