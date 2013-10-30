package dk.dtu.ws.hotelservice;

import hotelservice._02267.dtu.dk.wsdl.AddressType;
import hotelservice._02267.dtu.dk.wsdl.HotelBookingArrayType;
import hotelservice._02267.dtu.dk.wsdl.HotelBookingType;
import hotelservice._02267.dtu.dk.wsdl.HotelQueryType;
import java.util.List;

/**
 *
 * @author prasopes
 */
public class HotelRepository {
    
    private HotelBookingType booking1;
    private HotelBookingType booking2;
    private HotelBookingType booking3;
            
    public HotelRepository() {
        AddressType address1 = new AddressType();
        address1.setCity("Copenhagen");
        address1.setCountry("Denmark");
        address1.setHouseNo("15");
        address1.setStreet("Dynamovej");
        address1.setZip("2750");
        
        booking1 = new HotelBookingType();        
        booking1.setAddress(address1);
        booking1.setBookingNo("000001");
        booking1.setCcGuaranteeReq(true);        
        booking1.setHotelServiceName("NiceView");
        booking1.setHotelName("Royal Hotel");      
        booking1.setPrice(450.0f);
        
        AddressType address2 = new AddressType();
        address2.setCity("Copenhagen");
        address2.setCountry("Denmark");
        address2.setHouseNo("27");
        address2.setStreet("Elektrovej");
        address2.setZip("2750");

        booking2 = new HotelBookingType();        
        booking2.setAddress(address2);
        booking2.setBookingNo("000002");
        booking2.setCcGuaranteeReq(false);        
        booking2.setHotelServiceName("NiceView");
        booking2.setHotelName("Cheap Hotel");      
        booking2.setPrice(250.0f);      
        
        AddressType address3 = new AddressType();
        address3.setCity("Copenhagen");
        address3.setCountry("Denmark");
        address3.setHouseNo("7");
        address3.setStreet("Magnetovej");
        address3.setZip("2750");

        booking3 = new HotelBookingType();        
        booking3.setAddress(address3);
        booking3.setBookingNo("000003");
        booking3.setCcGuaranteeReq(false);        
        booking3.setHotelServiceName("NiceView");
        booking3.setHotelName("DTU Dormitory"); 
        booking3.setPrice(50.0f);        
    }
    
    public HotelBookingArrayType listHotels(HotelQueryType hotelQuery) {
        //hotelQuery.getArrivalDate();
        //hotelQuery.getDepartureDate();
        //hotelQuery.getCity();
        HotelBookingArrayType result = new HotelBookingArrayType();
        List<HotelBookingType> bookings = result.getBookings();
        bookings.add(booking1);
        bookings.add(booking2);
        bookings.add(booking3);
        return result;
    }
    
}
