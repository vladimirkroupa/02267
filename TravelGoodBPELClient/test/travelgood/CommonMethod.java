/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import travelgoodtypes.XItinerary;

/**
 *
 * @author ming
 */
public class CommonMethod {

    public static XMLGregorianCalendar toDate(String dateinput) {
        //Demos and Usage of javax.xml.datatype.DatatypeFactory.newXMLGregorianCalendar
        //(int year,int month,int day,int hour,int minute,int second,int millisecond,int timezone)
        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            //  Logger.getLogger(lameduckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        XMLGregorianCalendar date = df.newXMLGregorianCalendar(dateinput);
        return date;
    }

    public static void printStatus(XItinerary itinerary) {
        for (int i = 0; i < itinerary.getFlightBookingList().size(); i++) {
            String bookingNo = itinerary.getFlightBookingList().get(i).getFlightInfo().getBookingNumber();
            String bookingStatus = itinerary.getFlightBookingList().get(i).getBookingStatus();
            int price = itinerary.getFlightBookingList().get(i).getFlightInfo().getFlightPrice();
            System.out.println("Itineary contain flight: booking number : " + bookingNo + " , price: " + price + " , status : " + bookingStatus);

        }

        for (int i = 0; i < itinerary.getHotelBookingList().size(); i++) {
            String bookingNo = itinerary.getHotelBookingList().get(i).getHotelInfo().getBookingNo();
            String bookingStatus = itinerary.getHotelBookingList().get(i).getBookingStatus();
            float price = itinerary.getHotelBookingList().get(i).getHotelInfo().getPrice();
            System.out.println("Itineary contain hotel: booking number : " + bookingNo + " , price: " + price + " , status : " + bookingStatus);

        }
    }
}
