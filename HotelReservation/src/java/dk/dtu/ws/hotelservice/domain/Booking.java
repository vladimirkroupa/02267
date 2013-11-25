package dk.dtu.ws.hotelservice.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class Booking {

    public static final double CC_AUTH_THRESHOLD = 100.0;
    
    private final Hotel hotel;
    private final String bookingNo;
    private final Interval stay;
    
    public Booking(Hotel hotel, String bookingNo, Date from, Date to) {
        this.hotel = hotel;
        this.bookingNo = bookingNo;
        DateTime checkIn = (new LocalDate(from)).toDateTimeAtStartOfDay();
        DateTime checkOut = (new LocalDate(to)).toDateTimeAtStartOfDay();
        this.stay = new Interval(checkIn, checkOut);
    }
    
    public int getPrice() {
        return new BigDecimal(hotel.getPriceForStay(stay)).intValue();
    }
    
    public String getBookingNumber() {
        return bookingNo;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public Interval getStay() {
        return stay;
    }
    
    public boolean ccAuthRequired() {
        return getPrice() > CC_AUTH_THRESHOLD;
    }
    
    public boolean book() throws OverbookingException {
        LocalDate from = getStay().getStart().toLocalDate();
        LocalDate to = getStay().getEnd().toLocalDate();        
        getHotel().bookHotel(from, to);
        return true;
    }
    
    public boolean cancel() {
        return getHotel().cancelBooking(getStay());
    }
    
    /* generated */
    
    @Override
    public String toString() {
        return "Booking{" + "hotel=" + hotel + ", bookingNo=" + bookingNo + ", stay=" + stay + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.hotel != null ? this.hotel.hashCode() : 0);
        hash = 61 * hash + (this.bookingNo != null ? this.bookingNo.hashCode() : 0);
        hash = 61 * hash + (this.stay != null ? this.stay.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Booking other = (Booking) obj;
        if (this.hotel != other.hotel && (this.hotel == null || !this.hotel.equals(other.hotel))) {
            return false;
        }
        if ((this.bookingNo == null) ? (other.bookingNo != null) : !this.bookingNo.equals(other.bookingNo)) {
            return false;
        }
        if (this.stay != other.stay && (this.stay == null || !this.stay.equals(other.stay))) {
            return false;
        }
        return true;
    }
    
}
