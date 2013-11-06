package dk.dtu.ws.hotelservice.domain;

import java.util.Date;
import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author prasopes
 */
public class HotelTest {
    
    private Hotel hotel;
    
    @Before
    public void setUp() {
        hotel = new Hotel("Royal Hotel", new Address("Lyngbyvej", "20", "Lyngby", "2750"), 3, 50.0);
    }

    @Test
    public void testPrice() {
        DateTime from = DateTime.now();
        DateTime to = from.plusDays(5);
        double expectedPrice = 250.0;
        double actualPrice = hotel.getPriceForStay(from, to);
        assertEquals(expectedPrice, actualPrice, 0.1);
    }

    @Test
    public void testBooking() {
        DateTime today = DateTime.now();
        DateTime tommorow = today.plusDays(1);
        DateTime day_after_tommorow = tommorow.plusDays(1);
        
        assertEquals(3, hotel.freeRoomsOn(today));
        assertEquals(3, hotel.freeRoomsOn(tommorow));
        assertTrue(hotel.hasAvailableRoom(today, day_after_tommorow));
        
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
        assertEquals(1, hotel.freeRoomsOn(today));
        assertEquals(1, hotel.freeRoomsOn(tommorow));
        assertTrue(hotel.hasAvailableRoom(today, day_after_tommorow));

        hotel.bookHotel(day_after_tommorow, day_after_tommorow);
        assertEquals(1, hotel.freeRoomsOn(today));
        assertEquals(1, hotel.freeRoomsOn(tommorow));
        assertEquals(2, hotel.freeRoomsOn(day_after_tommorow));                                
        assertTrue(hotel.hasAvailableRoom(today, day_after_tommorow));
        
        hotel.bookHotel(today.minusDays(1), today);
        assertFalse(hotel.hasAvailableRoom(today, day_after_tommorow));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOverbooking() {
        DateTime today = DateTime.now();
        DateTime tommorow = today.plusDays(1);
        
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
    }
}
