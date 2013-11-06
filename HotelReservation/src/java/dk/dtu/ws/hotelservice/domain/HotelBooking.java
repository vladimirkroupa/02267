package dk.dtu.ws.hotelservice.domain;

import org.joda.time.DateTime;

/**
 *
 * @author prasopes
 */
public class HotelBooking {

    private final DateTime checkIn;
    private final DateTime checkOut;

    public HotelBooking(DateTime checkIn, DateTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public DateTime getCheckIn() {
        return checkIn;
    }

    public DateTime getCheckOut() {
        return checkOut;
    }

    public boolean containsDate(DateTime date) {
        boolean from = (date.equals(checkIn) || date.isAfter(checkIn));
        boolean to = (date.equals(checkOut)) || (date.isBefore(checkOut));
        return from && to;
    }
    
    /* generated */
    
    @Override
    public String toString() {
        return "HotelBooking{" + "checkIn=" + checkIn + ", checkOut=" + checkOut + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.checkIn != null ? this.checkIn.hashCode() : 0);
        hash = 37 * hash + (this.checkOut != null ? this.checkOut.hashCode() : 0);
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
        final HotelBooking other = (HotelBooking) obj;
        if (this.checkIn != other.checkIn && (this.checkIn == null || !this.checkIn.equals(other.checkIn))) {
            return false;
        }
        if (this.checkOut != other.checkOut && (this.checkOut == null || !this.checkOut.equals(other.checkOut))) {
            return false;
        }
        return true;
    }
        
    
}
