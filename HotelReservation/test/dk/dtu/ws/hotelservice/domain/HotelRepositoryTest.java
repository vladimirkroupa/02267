package dk.dtu.ws.hotelservice.domain;

import java.util.Arrays;
import java.util.List;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class HotelRepositoryTest {
    
    private HotelRepository hotelRepo;
    private Hotel hotel1 = hotel1();
    private Hotel hotel2 = hotel2();
    
    @Before
    public void setUp() {
        hotelRepo = new HotelRepository(hotel1, hotel2);
    }

    @Test
    public void testHotelsAvailableNoBookings() {
        LocalDate today = LocalDate.now();
        LocalDate tommorow = today.plusDays(1);
        List<Hotel> expectedHotels = Arrays.asList(hotel1, hotel2);
        List<Hotel> actualHotels = hotelRepo.listHotelsAvailable("Copenhagen", today, tommorow);
        assertEquals(expectedHotels, actualHotels);
    }

    @Test
    public void testListHotelsAvailableFullyBooked() throws OverbookingException {
        LocalDate today = LocalDate.now();
        LocalDate tommorow = today.plusDays(1);
        hotel1.bookHotel(today, tommorow);
        hotel1.bookHotel(today, tommorow);
        hotel1.bookHotel(today, tommorow);
        List<Hotel> expectedHotels = Arrays.asList(hotel2);
        List<Hotel> actualHotels = hotelRepo.listHotelsAvailable("Copenhagen", today, tommorow);
        assertEquals(expectedHotels, actualHotels);
    }
    
    private static Hotel hotel1() {
        Address address = new Address("Dynamovej", "15", "Copenhagen", "2750");
        return new Hotel("Royal Hotel", address, 3, 50.0, true);
    }
    
    private static Hotel hotel2() {
        Address address = new Address("Elektrovej", "27", "Copenhagen", "2750");
        return new Hotel("DTU Hostel", address, 10, 20.0, true);
    }
    
}
