package dk.dtu.ws.hotelservice.domain;

import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.CreditCardFaultType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.ws.bankservice.client.BankServiceClient;
import dk.dtu.ws.hotelservice.WSTypeConverter;
import dk.dtu.ws.hotelservice.domain.dataset.StaticHotelSource;
import hotelreservationtypes.HotelFaultType;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.CancelHotelOperationFault;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NiceView {

    public static final int GROUP_NUMBER = 2;
    public static final String SERVICE_NAME = "NiceView";
    
    private final HotelRepository hotels;
    private final Map<String, Booking> offeredBookings = new HashMap<String, Booking>();
    private final Map<String, Booking> confirmedBookings = new HashMap<String, Booking>();

    public NiceView() {
        hotels = new HotelRepository(StaticHotelSource.hotels());
    }

    public HotelList listHotels(String city, Date checkIn, Date checkOut) {
        HotelList result = new HotelList();
        for (Booking booking : listBookingOffers(city, checkIn, checkOut)) {
            HotelType ht = WSTypeConverter.toHotelType(booking);
            result.getHotels().add(ht);
        }
        return result;
    }

    List<Booking> listBookingOffers(String city, Date checkIn, Date checkOut) {
        List<Booking> result = new ArrayList<Booking>();
        for (Hotel h : hotels.listHotelsAvailable(city, checkIn, checkOut)) {
            Booking booking = createBookingOffer(checkIn, checkOut, h);
            offeredBookings.put(booking.getBookingNumber(), booking);
            result.add(booking);
        }
        return result;
    }

    public boolean bookHotel(String bookingNo, CreditCardInfoType cc) throws BookHotelOperationFault {
        validateBooking(bookingNo);
                
        Booking booking = offeredBookings.get(bookingNo);

        if (booking.ccAuthRequired()) {
            validateCreditCard(booking.getPrice(), cc);
        }

        offeredBookings.remove(bookingNo);
        confirmedBookings.put(bookingNo, booking);
        try {
            booking.book();
        } catch (OverbookingException ex) {
            throw createBookingFault("Hotel overbooked.", "" + bookingNo);
        }

        return true;
    }

    public boolean cancelBooking(String bookingNo) throws CancelHotelOperationFault {
        validateBookingCancellation(bookingNo);
        Booking booking = confirmedBookings.get(bookingNo);
        confirmedBookings.remove(bookingNo);
        return booking.cancel();
    }
    
    public void reset() {
        this.offeredBookings.clear();
        this.confirmedBookings.clear();
        BookingNumberSequence.reset();
        hotels.cancelAllBookings();
    }

    private Booking createBookingOffer(Date checkIn, Date checkOut, Hotel hotel) {
        String bookingNo = BookingNumberSequence.nextBookingNo();
        Booking booking = new Booking(hotel, bookingNo, checkIn, checkOut);
        return booking;
    }

    private void validateBooking(String bookingNo) throws BookHotelOperationFault {
        if (confirmedBookings.containsKey(bookingNo)) {
            BookHotelOperationFault fault = createBookingFault("Booking already exists.", "Booking number: " + bookingNo);
            throw fault;
        }        
        if (! offeredBookings.containsKey(bookingNo)) {
            BookHotelOperationFault fault = createBookingFault("Invalid booking number specified.", "Booking number: " + bookingNo);
            throw fault;
        }
    }

    private void validateBookingCancellation(String bookingNo) throws CancelHotelOperationFault {
        boolean offered = offeredBookings.containsKey(bookingNo);
        boolean confirmed = confirmedBookings.containsKey(bookingNo);

        if (!confirmed && !offered) {
            throw createCancellationFault("Invalid booking number specified.", "Booking number: " + bookingNo);
        }
        if (offered && !confirmed) {
            throw createCancellationFault("Attempt to cancel unconfirmed booking.", "Booking number: " + bookingNo);
        }
        if (offered && confirmed) {
            throw new IllegalStateException("Invalid booking state.");
        }
    }

    private boolean validateCreditCard(double amount, CreditCardInfoType cc) throws BookHotelOperationFault {
        boolean valid;
        CreditCardFaultType ccFault = null;
        int intAmount = new BigDecimal(amount).intValue();
        try {
            valid = BankServiceClient.validateCreditCard(GROUP_NUMBER, cc, intAmount);
        } catch (CreditCardFaultMessage ex) {
            ccFault = ex.getFaultInfo();
            valid = false;
        }
        if (!valid) {
            String faultMsg = ccFault != null ? ccFault.getMessage() : "Card information are not valid.";
            BookHotelOperationFault bookingFault = createBookingFault("Could not validate credit card.", faultMsg);
            throw bookingFault;
        }
        return true;
    }

    private BookHotelOperationFault createBookingFault(String message, String detail) {
        HotelFaultType hotelFault = new HotelFaultType();
        hotelFault.setErrorMessage(message);
        hotelFault.setErrorDetail(detail);
        return new BookHotelOperationFault(message, hotelFault);
    }

    private CancelHotelOperationFault createCancellationFault(String message, String detail) {
        HotelFaultType hotelFault = new HotelFaultType();
        hotelFault.setErrorMessage(message);
        hotelFault.setErrorDetail(detail);
        return new CancelHotelOperationFault(message, hotelFault);
    }
}
