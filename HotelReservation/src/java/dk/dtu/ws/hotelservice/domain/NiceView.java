package dk.dtu.ws.hotelservice.domain;

import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.CreditCardFaultType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.ws.bankservice.client.BankServiceClient;
import dk.dtu.ws.hotelservice.domain.dataset.StaticHotelSource;
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.HotelFaultType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author prasopes
 */
public class NiceView {

    public static final int GROUP_NUMBER = 1;
    public static final double CC_AUTH_THRESHOLD = 200.0;
    
    private final HotelRepository hotels;
    private final Map<String, BookingOffer> offeredBookings = new HashMap<String, BookingOffer>();
    private final Map<String, Hotel> confirmedBookings = new HashMap<String, Hotel>();
    private final BankServiceClient bank = new BankServiceClient();

    public NiceView() {
        hotels = new HotelRepository(StaticHotelSource.hotels());
    }
    
    public List<Hotel> listHotels(String city, Date checkIn, Date checkOut) {
        return hotels.listHotelsAvailable(city, checkIn, checkOut);
    }
    
    public void bookHotel(String bookingNo, CreditCardInfoType cc) throws BookHotelOperationFault {
        if (validateBookingNo(bookingNo)) {
            //if (ccAuthRequired)
        }
    }
    
    private boolean ccAuthRequired(double amount) {
        return amount > CC_AUTH_THRESHOLD;
    }
    
    private boolean validateBookingNo(String bookingNo) throws BookHotelOperationFault {
        if (! hotels.containsKey(bookingNo)) {
            BookHotelOperationFault fault = createHotelOpFault("Invalid hotel specified", "Booking number: " + bookingNo);
            throw fault;
        }
        return true;
    }
    
    private boolean validateCreditCard(int amount, CreditCardInfoType cc) throws BookHotelOperationFault {
        boolean valid;
        CreditCardFaultType ccFault = null;
        try {
            valid = bank.validateCreditCard(GROUP_NUMBER, cc, amount);
        } catch (CreditCardFaultMessage ex) {
            ccFault = ex.getFaultInfo();
            valid = false;
        }
        if (! valid) {
            String faultMsg = ccFault != null ? ccFault.getMessage() : "Card information are not valid.";
            BookHotelOperationFault bookingFault = createHotelOpFault("Could not process credit card.", faultMsg);
            throw bookingFault;
        }
        return true;
        
    }
    
    private BookHotelOperationFault createHotelOpFault(String message, String detail) {
        HotelFaultType hotelFault = new HotelFaultType();
        hotelFault.setErrorMessage(message);
        hotelFault.setErrorDetail(detail);
        return new BookHotelOperationFault(message, hotelFault);
    }
    
    static class HotelBooking {
        
        Booking booking;
        Hotel hotel;

        public HotelBooking(Booking booking, Hotel hotel) {
            this.booking = booking;
            this.hotel = hotel;
        }

        public Booking getBooking() {
            return booking;
        }

        public Hotel getHotel() {
            return hotel;
        }

        @Override
        public String toString() {
            return "HotelBooking{" + "booking=" + booking + ", hotel=" + hotel + '}';
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + (this.booking != null ? this.booking.hashCode() : 0);
            hash = 71 * hash + (this.hotel != null ? this.hotel.hashCode() : 0);
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
            final HotelBooking other = (HotelBooking) obj;
            if (this.booking != other.booking && (this.booking == null || !this.booking.equals(other.booking))) {
                return false;
            }
            if (this.hotel != other.hotel && (this.hotel == null || !this.hotel.equals(other.hotel))) {
                return false;
            }
            return true;
        }
        
    }
            
}
