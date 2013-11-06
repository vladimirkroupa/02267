package dk.dtu.ws.hotelservice.domain;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class HotelTest {
    
    private Hotel hotel;
    
    @Before
    public void setUp() {
        hotel = new Hotel("Royal Hotel", new Address("Lyngbyvej", "20", "Lyngby", "2750"), 3, 50.0);
    }

    @Test
    public void testPrice() {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(5);
        double expectedPrice = 250.0;
        double actualPrice = hotel.getPriceForStay(from.toDate(), to.toDate());
        assertEquals(expectedPrice, actualPrice, 0.1);
    }

    @Test
    public void testBooking() throws OverbookingException {
        LocalDate today = LocalDate.now();
        LocalDate tommorow = today.plusDays(1);
        LocalDate day_after_tommorow = tommorow.plusDays(1);
        
        assertEquals(3, hotel.freeRoomsOn(today));
        assertEquals(3, hotel.freeRoomsOn(tommorow));
        assertTrue(hotel.hasAvailableRoom(today, day_after_tommorow));
        
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
        assertEquals(1, hotel.freeRoomsOn(today));
        assertEquals(1, hotel.freeRoomsOn(tommorow));
        assertEquals(3, hotel.freeRoomsOn(day_after_tommorow));
        assertTrue(hotel.hasAvailableRoom(today, day_after_tommorow));

        hotel.bookHotel(day_after_tommorow, day_after_tommorow.plusDays(1));
        assertEquals(1, hotel.freeRoomsOn(today));
        assertEquals(1, hotel.freeRoomsOn(tommorow));
        assertEquals(2, hotel.freeRoomsOn(day_after_tommorow));                                
        assertTrue(hotel.hasAvailableRoom(today, day_after_tommorow));
        
        hotel.bookHotel(today.minusDays(1), today);
        assertFalse(hotel.hasAvailableRoom(today, day_after_tommorow));
        hotel.cancelBooking(new Interval(today.minusDays(1).toDateTimeAtStartOfDay(), today.toDateTimeAtStartOfDay()));
        assertTrue(hotel.hasAvailableRoom(today, day_after_tommorow));
    }
    
    @Test(expected = OverbookingException.class)
    public void testOverbooking() throws OverbookingException {
        LocalDate today = LocalDate.now();
        LocalDate tommorow = today.plusDays(1);
        
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
        hotel.bookHotel(today, tommorow);
    }
    
}
