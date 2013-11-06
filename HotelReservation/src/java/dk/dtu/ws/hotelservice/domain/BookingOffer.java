package dk.dtu.ws.hotelservice.domain;

import org.joda.time.Interval;

/**
 *
 * @author prasopes
 */
public class BookingOffer {
 
    private final Hotel hotel;
    private final String bookingNo;
    private final Interval days;
    
    public BookingOffer(Hotel hotel, String bookingNo, Interval days) {
        this.hotel = hotel;
        this.bookingNo = bookingNo;
        this.days = days;
    }
    
    public int getPrice() {
        //return hotel.get
        return 0;
    }
    
}
