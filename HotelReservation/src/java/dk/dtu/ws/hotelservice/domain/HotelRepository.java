package dk.dtu.ws.hotelservice.domain;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDate;

public class HotelRepository {
    
    /** City => Hotels map */
    private final Multimap<String, Hotel> hotels = HashMultimap.create();
    
    public HotelRepository(Hotel... hotels) {
        this(Arrays.asList(hotels));
    }
        
    public HotelRepository(Collection<Hotel> hotels) {
        for (Hotel hotel : hotels) {
            add(hotel);
        }
    }
    
    private void add(Hotel hotel) {
        String city = hotel.getAddress().getCity();
        hotels.put(city, hotel);
    }
    
    public List<Hotel> listHotelsAvailable(String city, Date checkIn, Date checkOut) {
        return listHotelsAvailable(city, new LocalDate(checkIn), new LocalDate(checkOut));
    }
    
    public List<Hotel> listHotelsAvailable(String city, LocalDate checkIn, LocalDate checkOut) {
        List<Hotel> availableHotels = new ArrayList<Hotel>();

        Collection<Hotel> hotelsInCity = hotels.get(city);
        for (Hotel hotel : hotelsInCity) {
            if (hotel.hasAvailableRoom(checkIn, checkOut)) {
                availableHotels.add(hotel);
            }
        }
        return availableHotels;
    }
 
    void cancelAllBookings() {
        for (Hotel h : hotels.values()) {
            h.cancelAllBookings();
        }
    }
    
}
