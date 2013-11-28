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
       
        
                        
        FlightInfoType flightInfo1 = new FlightInfoType();
        flightInfo1.setBookingNumber("1234567");
        flightInfo1.setFlight(createFlight("SAS","CPH","LHR",
                DateUtils.toXmlGregCal("2013-09-18", "17:00"),
                DateUtils.toXmlGregCal("2013-09-18", "19:00")));
        flightInfo1.setFlightPrice(1500);
        flightInfo1.setAirlineReservationServiceName("LameDuck");
        flightInfo1.setCancellable(true);
        
        FlightInfoType flightInfo2 = new FlightInfoType();
        flightInfo2.setBookingNumber("1234568");
        flightInfo2.setFlight(createFlight("SAS","CPH","PEK",
                DateUtils.toXmlGregCal("2013-12-02", "12:00"),
                DateUtils.toXmlGregCal("2013-12-03", "19:30")));
        flightInfo2.setFlightPrice(7500);
        flightInfo2.setAirlineReservationServiceName("LameDuck");
        flightInfo2.setCancellable(true);

        FlightInfoType flightInfo3 = new FlightInfoType();
        flightInfo3.setBookingNumber("1234570");
        flightInfo3.setFlight(createFlight("British Airways","CPH","AMS",
                DateUtils.toXmlGregCal("2013-12-05", "12:00"),
                DateUtils.toXmlGregCal("2013-12-05", "14:30")));
        flightInfo3.setFlightPrice(1030);
        flightInfo3.setAirlineReservationServiceName("LameDuck");
        flightInfo3.setCancellable(true);

        FlightInfoType flightInfo4 = new FlightInfoType();
        flightInfo4.setBookingNumber("1234571");
        flightInfo4.setFlight(createFlight("British Airways","CPH","OSL",
                DateUtils.toXmlGregCal("2013-12-06", "12:00"),
                DateUtils.toXmlGregCal("2013-12-06", "14:30")));
        flightInfo4.setFlightPrice(750);
        flightInfo4.setAirlineReservationServiceName("LameDuck");
        flightInfo4.setCancellable(true);

        FlightInfoType flightInfo5 = new FlightInfoType();
        flightInfo5.setBookingNumber("1234572");
        flightInfo5.setFlight(createFlight("British Airways","CPH","OSL",
                DateUtils.toXmlGregCal("2013-12-06", "12:00"),
                DateUtils.toXmlGregCal("2013-12-06", "14:30")));
        flightInfo5.setFlightPrice(750);
        flightInfo5.setAirlineReservationServiceName("LameDuck");
        flightInfo5.setCancellable(true);

        FlightInfoType flightInfo6 = new FlightInfoType();
        flightInfo6.setBookingNumber("1234573");
        flightInfo6.setFlight(createFlight("British Airways","CPH","OSL",
                DateUtils.toXmlGregCal("2013-12-06", "12:00"),
                DateUtils.toXmlGregCal("2013-12-06", "14:30")));
        flightInfo6.setFlightPrice(750);
        flightInfo6.setAirlineReservationServiceName("LameDuck");
        flightInfo6.setCancellable(true);
        
        
        //------------ DATA USED FOR TESTING SCENARIOS -------------
        //Flight Copenhagen to Stockholm
        FlightInfoType flightInfo7 = new FlightInfoType();
        flightInfo7.setBookingNumber("6001001");
        flightInfo7.setFlight(createFlight("Singapore Airlines","CPH","ARL",
                DateUtils.toXmlGregCal("2013-12-27", "16:00"),
                DateUtils.toXmlGregCal("2013-12-27", "18:30")));
        flightInfo7.setFlightPrice(300);
        flightInfo7.setAirlineReservationServiceName("LameDuck");
        flightInfo7.setCancellable(true);   
        
        //Flight Stockholm to Copenhagen
        FlightInfoType flightInfo8 = new FlightInfoType();
        flightInfo8.setBookingNumber("6001002");
        flightInfo8.setFlight(createFlight("Singapore Airlines","ARL","CPH",
                DateUtils.toXmlGregCal("2013-12-28", "16:00"),
                DateUtils.toXmlGregCal("2013-12-28", "18:30")));
        flightInfo8.setFlightPrice(400);
        flightInfo8.setAirlineReservationServiceName("LameDuck");
        flightInfo8.setCancellable(true); 
        
        
        //Flight Copenhagen to Singapore
        FlightInfoType flightInfo9 = new FlightInfoType();
        flightInfo9.setBookingNumber("7001001");
        flightInfo9.setFlight(createFlight("Singapore Airlines","CPH","SIN",
                DateUtils.toXmlGregCal("2014-01-01", "02:00"),
                DateUtils.toXmlGregCal("2014-01-01", "14:30")));
        flightInfo9.setFlightPrice(900);
        flightInfo9.setAirlineReservationServiceName("LameDuck");
        flightInfo9.setCancellable(true);   
        
        //Flight Singapore back to Copenhagen 
        FlightInfoType flightInfo10 = new FlightInfoType();
        flightInfo10.setBookingNumber("7001002");
        flightInfo10.setFlight(createFlight("Singapore Airlines","SIN","CPH",
                DateUtils.toXmlGregCal("2014-01-03", "18:00"),
                DateUtils.toXmlGregCal("2014-01-04", "16:30")));
        flightInfo10.setFlightPrice(1000);
        flightInfo10.setAirlineReservationServiceName("LameDuck");
        flightInfo10.setCancellable(true);   
        
        //Flight Copenhagen to Pyongyang
        FlightInfoType flightInfo11 = new FlightInfoType();
        flightInfo11.setBookingNumber("8001001");
        flightInfo11.setFlight(createFlight("Air Koryo","CPH","FNJ",
                DateUtils.toXmlGregCal("2014-01-05", "02:00"),
                DateUtils.toXmlGregCal("2014-01-05", "16:30")));
        flightInfo11.setFlightPrice(750);
        flightInfo11.setAirlineReservationServiceName("LameDuck");
        flightInfo11.setCancellable(false);
        
        //Flight Pyongyang to Copenhagen
        FlightInfoType flightInfo12 = new FlightInfoType();
        flightInfo12.setBookingNumber("8001002");
        flightInfo12.setFlight(createFlight("Air Koryo","FNJ","CPH",
                DateUtils.toXmlGregCal("2014-01-07", "23:00"),
                DateUtils.toXmlGregCal("2014-01-12", "08:30")));
        flightInfo12.setFlightPrice(750);
        flightInfo12.setAirlineReservationServiceName("LameDuck");
        flightInfo12.setCancellable(false);              

        flightInfoList.getFlightInfo().add(flightInfo1);
        flightInfoList.getFlightInfo().add(flightInfo2);
        flightInfoList.getFlightInfo().add(flightInfo3);
        flightInfoList.getFlightInfo().add(flightInfo4);
        flightInfoList.getFlightInfo().add(flightInfo5);
        flightInfoList.getFlightInfo().add(flightInfo6);
        flightInfoList.getFlightInfo().add(flightInfo7);
        flightInfoList.getFlightInfo().add(flightInfo8);
        flightInfoList.getFlightInfo().add(flightInfo9);
        flightInfoList.getFlightInfo().add(flightInfo10);        
        flightInfoList.getFlightInfo().add(flightInfo11);
        flightInfoList.getFlightInfo().add(flightInfo12);        
        

    }

    /**
     * @param date date in format yyyy-MM-dd
     * @param time in format hh:mm
     * @return {@XMLGregorianCalendar} instance with everything set to zero except year, month, day, hours and minutes
     */
    public final XMLGregorianCalendar date(String date, String time) {
        return DateUtils.toXmlGregCal(date, time);
    }

    
    public FlightType createFlight(String carrier, String startAirport, String destinationAirport,
            XMLGregorianCalendar dateTimeLift, XMLGregorianCalendar dateTimeLanding){
        FlightType flight = new FlightType();
        flight.setCarrier(carrier);
        flight.setStartAirpot(startAirport);
        flight.setDestinationAirport(destinationAirport);
        flight.setDatetimeLift(dateTimeLift);
        flight.setDatetimeLanding(dateTimeLanding);
        return flight;        

    }
}
