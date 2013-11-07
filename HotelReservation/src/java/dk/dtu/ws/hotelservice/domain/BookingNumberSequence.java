package dk.dtu.ws.hotelservice.domain;

public class BookingNumberSequence {

    private static int number = 1;
   
    public static String nextBookingNo() {        
        String result = String.format("%05d", number);
        number++;
        return result;
    }
 
    static void reset() {
        number = 1;
    }
    
}
