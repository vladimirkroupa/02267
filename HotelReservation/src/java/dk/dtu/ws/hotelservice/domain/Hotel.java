package dk.dtu.ws.hotelservice.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 *
 * @author prasopes
 */
public class Hotel {

    private final String name;
    private final Address address;
    private final double pricePerNight;
    private final int rooms;
    private final List<HotelBooking> bookings;

    public Hotel(String name, Address address, int rooms, double pricePerNight) {
        this.name = name;
        this.address = address;
        this.rooms = rooms;
        this.pricePerNight = pricePerNight;
        this.bookings = new ArrayList<HotelBooking>();
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
        return getPriceForStay(new DateTime(checkIn), new DateTime(checkOut));
    }
    
    public double getPriceForStay(DateTime checkIn, DateTime checkOut) {
        int days = Days.daysBetween(checkIn, checkOut).getDays();
        return days * pricePerNight;
    }

    public void bookHotel(Date checkIn, Date checkOut) {
        bookHotel(new DateTime(checkIn), new DateTime(checkOut));
    }
    
    public void bookHotel(DateTime checkIn, DateTime checkOut) {
        if (!hasAvailableRoom(checkIn, checkOut)) {
            throw new IllegalArgumentException("No rooms available for selected period.");
        }
        HotelBooking booking = new HotelBooking(checkIn, checkOut);
        bookings.add(booking);
    }

    public boolean hasAvailableRoom(Date from, Date to) {
        return hasAvailableRoom(new DateTime(from), new DateTime(to));
    }
    
    public boolean hasAvailableRoom(DateTime from, DateTime to) {
        while (from.compareTo(to) <= 0) {            
            if (freeRoomsOn(from) == 0) {
                return false;
            }
            from = from.plusDays(1);
        }
        return true;
    }

    int freeRoomsOn(DateTime day) {
        int freeRooms = this.rooms;
        for (HotelBooking booking : bookings) {
            if (booking.containsDate(day)) {
                freeRooms--;
            }
        }
        return freeRooms;
    }

    @Override
    public String toString() {
        return "Hotel{" + "name=" + name + ", address=" + address + ", pricePerNight=" + pricePerNight + ", rooms=" + rooms + ", bookings=" + bookings + '}';
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
