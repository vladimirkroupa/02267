package dk.dtu.ws.hotelservice;

import dk.dtu.ws.hotelservice.domain.Address;
import dk.dtu.ws.hotelservice.domain.Booking;
import dk.dtu.ws.hotelservice.domain.NiceView;
import hotelreservationtypes.AddressType;
import hotelreservationtypes.HotelType;

public class WSTypeConverter {

    public static HotelType toHotelType(Booking booking) {
        HotelType hotel = new HotelType();        
        hotel.setAddress(toAddressType(booking.getHotel().getAddress()));
        hotel.setBookingNo(booking.getBookingNumber());
        hotel.setCcGuaranteeReq(booking.ccAuthRequired());
        hotel.setHotelName(booking.getHotel().getName());
        hotel.setHotelServiceName(NiceView.SERVICE_NAME);
        hotel.setPrice(booking.getPrice());
        hotel.setCancellable(booking.getHotel().getCancellable());
        return hotel;
    }
    
    public static AddressType toAddressType(Address address) {
        AddressType result = new AddressType();
        result.setStreet(address.getStreet());
        result.setHouseNo(address.getHouseNo());
        result.setCity(address.getCity());
        result.setZip(address.getZip());
        result.setCountry(address.getCountry());        
        return result;
    }
}
