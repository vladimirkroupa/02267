package lameduck.ws;

import common.DateUtils;
import flightdata.FlightInfoList;
import flightdata.FlightInfoType;
import flightdata.FlightType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author moonlight
 */
public class FlightDB {

    FlightInfoList flightInfoList = new FlightInfoList();

    public FlightDB() {

        FlightType flight1 = new FlightType();
        flight1.setCarrier("SAS");
        flight1.setStartAirpot("CPH");
        flight1.setDestinationAirport("LHR");
        flight1.setDatetimeLift(date("2013-09-18", "17:00"));
        flight1.setDatetimeLanding(date("2013-09-18", "19:00"));

        FlightType flight2 = new FlightType();
        flight2.setCarrier("SAS");
        flight2.setStartAirpot("CPH");
        flight2.setDestinationAirport("PEK");
        flight2.setDatetimeLift(date("2013-12-02", "12:00"));
        flight2.setDatetimeLanding(date("2013-12-03", "19:30"));

        FlightType flight3 = new FlightType();
        flight3.setCarrier("British Airways");
        flight3.setStartAirpot("CPH");
        flight3.setDestinationAirport("AMS");
        flight3.setDatetimeLift(date("2013-12-05", "12:00"));
        flight3.setDatetimeLanding(date("2013-12-05", "14:30"));

        FlightType flight4 = new FlightType();
        flight4.setCarrier("British Airways");
        flight4.setStartAirpot("CPH");
        flight4.setDestinationAirport("OSL");
        flight4.setDatetimeLift(date("2013-12-06", "12:00"));
        flight4.setDatetimeLanding(date("2013-12-06", "14:30"));

        FlightType flight5 = new FlightType();
        flight5.setCarrier("British Airways");
        flight5.setStartAirpot("CPH");
        flight5.setDestinationAirport("OSL");
        flight5.setDatetimeLift(date("2013-12-06", "12:00"));
        flight5.setDatetimeLanding(date("2013-12-06", "14:30"));

        FlightType flight6 = new FlightType();
        flight6.setCarrier("British Airways");
        flight6.setStartAirpot("CPH");
        flight6.setDestinationAirport("OSL");
        flight6.setDatetimeLift(date("2013-12-06", "12:00"));
        flight6.setDatetimeLanding(date("2013-12-06", "14:30"));


        FlightInfoType flightInfo1 = new FlightInfoType();
        flightInfo1.setBookingNumber("1234567");
        flightInfo1.setFlight(flight1);
        flightInfo1.setFlightPrice(1500);
        flightInfo1.setAirlineReservationServiceName("LameDuck");

        FlightInfoType flightInfo2 = new FlightInfoType();
        flightInfo2.setBookingNumber("1234568");
        flightInfo2.setFlight(flight2);
        flightInfo2.setFlightPrice(7500);
        flightInfo2.setAirlineReservationServiceName("LameDuck");

        FlightInfoType flightInfo3 = new FlightInfoType();
        flightInfo3.setBookingNumber("1234570");
        flightInfo3.setFlight(flight3);
        flightInfo3.setFlightPrice(1030);
        flightInfo3.setAirlineReservationServiceName("LameDuck");

        FlightInfoType flightInfo4 = new FlightInfoType();
        flightInfo4.setBookingNumber("1234571");
        flightInfo4.setFlight(flight4);
        flightInfo4.setFlightPrice(750);
        flightInfo4.setAirlineReservationServiceName("LameDuck");

        FlightInfoType flightInfo5 = new FlightInfoType();
        flightInfo5.setBookingNumber("1234572");
        flightInfo5.setFlight(flight5);
        flightInfo5.setFlightPrice(750);
        flightInfo5.setAirlineReservationServiceName("LameDuck");

        FlightInfoType flightInfo6 = new FlightInfoType();
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

    /**
     * @param date date in format yyyy-MM-dd
     * @param time in format hh:mm
     * @return {@XMLGregorianCalendar} instance with everything set to zero except year, month, day, hours and minutes
     */
    public final XMLGregorianCalendar date(String date, String time) {
        return DateUtils.toXmlGregCal(date, time);
    }

}
