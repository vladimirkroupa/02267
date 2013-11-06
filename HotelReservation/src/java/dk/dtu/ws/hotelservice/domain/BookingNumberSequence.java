package dk.dtu.ws.hotelservice.domain;

/**
 *
 * @author prasopes
 */
public class BookingNumberSequence {

    private static int number = 1;
   
    public static String nextBookingNo() {        
        String result = String.format("%05i", number);
        number++;
        return result;
    }
    
}
