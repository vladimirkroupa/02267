package ws.dtu.travelgood;

import hotelreservationtypes.HotelType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author prasopes
 */
public class HotelBookingOffers {

    private static final Map<String, HotelType> bookings = new HashMap<String, HotelType>();
    
    public static void addOfferedBooking(HotelType booking) {
        bookings.put(booking.getBookingNo(), booking);
    }
    
    public static HotelType findOfferedBooking(String bookingNo) {
        return bookings.get(bookingNo);
    }
    
    public static void reset() {
        bookings.clear();
    }
            
}
