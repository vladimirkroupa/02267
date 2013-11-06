package dk.dtu.ws.hotelservice;

import dk.dtu.ws.hotelservice.domain.Address;
import dk.dtu.ws.hotelservice.domain.Booking;
import dk.dtu.ws.hotelservice.domain.NiceView;
import hotelservice._02267.dtu.dk.wsdl.AddressType;
import hotelservice._02267.dtu.dk.wsdl.HotelType;
import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;

public class WSTypeConverter {

    public static HotelType toHotelType(Booking booking) {
        HotelType hotel = new HotelType();        
        hotel.setAddress(toAddressType(booking.getHotel().getAddress()));
        hotel.setBookingNo(booking.getBookingNumber());
        hotel.setCcGuaranteeReq(booking.ccAuthRequired());
        hotel.setHotelName(booking.getHotel().getName());
        hotel.setHotelServiceName(NiceView.SERVICE_NAME);
        hotel.setPrice(booking.getPrice());
        return hotel;
    }
    
    public static AddressType toAddressType(Address address) {
        AddressType result = new AddressType();
        result.setCity(address.getCity());
        result.setCountry(address.getCountry());
        result.setHouseNo(address.getHouseNo());
        result.setZip(address.getZip());
        return result;
    }
    
    public static Date toDate(XMLGregorianCalendar xgc) {
        return xgc.toGregorianCalendar().getTime();
    }
}
