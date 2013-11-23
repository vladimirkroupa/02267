/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.dtu.travelgood;

import flightdata.FlightInfoType;
import hotelreservationtypes.HotelType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Henry Lie
 */
public class FlightBookingData {
    private static final Map<String, FlightInfoType> bookings = new HashMap<String, FlightInfoType>();

        
    public static void addOfferedBooking(FlightInfoType booking) {
        bookings.put(booking.getBookingNumber(), booking);
    }
    
    public static FlightInfoType findOfferedBooking(String bookingNo) {
        return bookings.get(bookingNo);
    }
}
