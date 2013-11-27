package dk.dtu.ws.hotelservice.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class Hotel {

    private final String name;
    private final Address address;
    private final double pricePerNight;
    private final int rooms;
    private final List<Interval> bookings;
    private final boolean cancellable;

    public Hotel(String name, Address address, int rooms, double pricePerNight, boolean cancellable) {
        this.name = name;
        this.address = address;
        this.rooms = rooms;
        this.pricePerNight = pricePerNight;
        this.bookings = new ArrayList<Interval>();
        this.cancellable = cancellable;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getRooms() {
        return rooms;
    }
    
    public double getPriceForStay(Date checkIn, Date checkOut) {
        Interval stay = new Interval(new LocalDate(checkIn).toDateTimeAtStartOfDay(), new LocalDate(checkOut).toDateTimeAtStartOfDay());
        return getPriceForStay(stay);
    }
    
    public double getPriceForStay(Interval stay) {
        int days = Days.daysIn(stay).getDays();
        return days * pricePerNight;
    }
    
    public boolean getCancellable(){
        return cancellable;
    }

    public void bookHotel(Date checkIn, Date checkOut) throws OverbookingException {
        bookHotel(new LocalDate(checkIn), new LocalDate(checkOut));
    }
    
    public void bookHotel(LocalDate checkIn, LocalDate checkOut) throws OverbookingException {
        if (!hasAvailableRoom(checkIn, checkOut)) {
            throw new OverbookingException("No rooms available for selected period.");
        }
        Interval booking = new Interval(checkIn.toDateTimeAtStartOfDay(), checkOut.toDateTimeAtStartOfDay());
        bookings.add(booking);
    }
    
    public boolean cancelBooking(Interval interval) {
        return bookings.remove(interval);
    }

    public boolean hasAvailableRoom(Date from, Date to) {
        return hasAvailableRoom(new LocalDate(from), new LocalDate(to));
    }
    
    public boolean hasAvailableRoom(LocalDate from, LocalDate to) {
        while (from.compareTo(to) <= 0) {            
            if (freeRoomsOn(from) == 0) {
                return false;
            }
            from = from.plusDays(1);
        }
        return true;
    }

    int freeRoomsOn(LocalDate day) {
        DateTime dayDT = day.toDateTimeAtStartOfDay();
        int freeRooms = this.rooms;
        for (Interval booking : bookings) {            
            if (booking.contains(dayDT) || booking.getEnd().equals(dayDT)) {
                freeRooms--;
            }
        }
        return freeRooms;
    }

    void cancelAllBookings() {
        bookings.clear();
    }
    
    @Override
    public String toString() {
        return "Hotel{" + "name=" + name + ", address=" + address + 
               ", pricePerNight=" + pricePerNight + ", rooms=" + rooms + 
               ", bookings=" + bookings +",cancellable="+cancellable+"}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.address != null ? this.address.hashCode() : 0);
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.pricePerNight) ^ (Double.doubleToLongBits(this.pricePerNight) >>> 32));
        hash = 37 * hash + this.rooms;
        hash = 37 * hash + (this.bookings != null ? this.bookings.hashCode() : 0);        
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
        final Hotel other = (Hotel) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.address != other.address && (this.address == null || !this.address.equals(other.address))) {
            return false;
        }
        if (Double.doubleToLongBits(this.pricePerNight) != Double.doubleToLongBits(other.pricePerNight)) {
            return false;
        }
        if (this.rooms != other.rooms) {
            return false;
        }
        if (this.bookings != other.bookings && (this.bookings == null || !this.bookings.equals(other.bookings))) {
            return false;
        }
        return true;
    }

}
