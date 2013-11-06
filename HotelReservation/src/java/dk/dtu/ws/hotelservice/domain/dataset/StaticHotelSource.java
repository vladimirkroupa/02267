package dk.dtu.ws.hotelservice.domain.dataset;

import dk.dtu.ws.hotelservice.domain.Address;
import dk.dtu.ws.hotelservice.domain.Hotel;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author prasopes
 */
public class StaticHotelSource {
    
    public static List<Hotel> hotels() {
        Address address1 = new Address("Dynamovej", "15", "Copenhagen", "2750");
        Hotel h1 = new Hotel("Royal Hotel", address1, 3, 50.0);
        Address address2 = new Address("Elektrovej", "27", "Copenhagen", "2750");
        Hotel h2 = new Hotel("DTU Hostel", address2, 10, 20.0);
        return Arrays.asList(h1, h2);
    }
    
}
