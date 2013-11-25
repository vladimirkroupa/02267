package dk.dtu.ws.hotelservice.domain.dataset;

import dk.dtu.ws.hotelservice.domain.Address;
import dk.dtu.ws.hotelservice.domain.Hotel;
import java.util.Arrays;
import java.util.List;

public class StaticHotelSource {
    
    public static List<Hotel> hotels() {
        Address address1 = new Address("Dynamovej", "15", "Copenhagen", "2200");
        Hotel h1 = new Hotel("Royal Hotel", address1, 3, 50.0);
        
        Address address2 = new Address("Elektrovej", "27", "Copenhagen", "2200");
        Hotel h2 = new Hotel("DTU Hostel", address2, 10, 20.0);
        
        Address address3 = new Address("Herlev Ringvej", "75", "Herlev", "2730");
        Hotel h3 = new Hotel("Herlev Hospital", address3, 15, 100.0);

        Address address4 = new Address("Herlev Ringvej", "129", "Herlev", "2730");
        Hotel h4 = new Hotel("Farfars Hotel", address4, 5, 30.0);
        
        return Arrays.asList(h1, h2, h3, h4);
    }
    
    
}
