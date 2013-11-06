package dk.dtu.ws.hotelservice.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author prasopes
 */

public class HotelRepository {
    
    /** City => Hotels map */
    private final Map<String, List<Hotel>> hotels = new HashMap<String, List<Hotel>>();
    
    public HotelRepository(Hotel... hotels) {
        for (Hotel hotel : hotels) {
            add(hotel);
        }
    }
    
    private void add(Hotel hotel) {
        String city = hotel.getAddress().getCity();
        if (! this.hotels.containsKey(city)) {
           this.hotels.put(city, new ArrayList<Hotel>());
        }
        List<Hotel> cityHotels = this.hotels.get(city);
        cityHotels.add(hotel);
    }
    
    public List<Hotel> listHotelsAvailable(String city, Date checkIn, Date checkOut) {
        return listHotelsAvailable(city, checkIn, checkOut);
    }
    
    public List<Hotel> listHotelsAvailable(String city, DateTime checkIn, DateTime checkOut) {
        List<Hotel> availableHotels = new ArrayList<Hotel>();

        List<Hotel> hotelsInCity = hotels.get(city);
        for (Hotel hotel : hotelsInCity) {
            if (hotel.hasAvailableRoom(checkIn, checkOut)) {
                availableHotels.add(hotel);
            }
        }
        return availableHotels;
    }
    
}
