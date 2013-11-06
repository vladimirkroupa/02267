
package dk.dtu.ws.hotelservice.domain;

import hotelservice._02267.dtu.dk.wsdl.AddressType;
import hotelservice._02267.dtu.dk.wsdl.HotelArrayType;
import hotelservice._02267.dtu.dk.wsdl.HotelQueryType;
import hotelservice._02267.dtu.dk.wsdl.HotelType;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author prasopes
 * 2013-11-1 Modified by Henry Lie to use HashMap for hotelBooking
 */

public class HotelRepositoryOld {
    
    private HotelType hotel1;
    private HotelType hotel2;
    private HotelType hotel3;
    private HashMap<String,HotelType> bookingNoToHotelMap = new HashMap<String,HotelType>();
            
    public HotelRepositoryOld() {
        String bookingNo = null;
        
        AddressType address1 = new AddressType();
        address1.setCity("Copenhagen");
        address1.setCountry("Denmark");
        address1.setHouseNo("15");
        address1.setStreet("Dynamovej");
        address1.setZip("2750");
        
        hotel1 = new HotelType();        
        hotel1.setAddress(address1);
        bookingNo = "000001";
        hotel1.setBookingNo("bookingNo");
        hotel1.setCcGuaranteeReq(true);        
        hotel1.setHotelServiceName("NiceView");
        hotel1.setHotelName("Royal Hotel");      
        hotel1.setPrice(450.0f);
        bookingNoToHotelMap.put(bookingNo, hotel1);
        
        AddressType address2 = new AddressType();
        address2.setCity("Copenhagen");
        address2.setCountry("Denmark");
        address2.setHouseNo("27");
        address2.setStreet("Elektrovej");
        address2.setZip("2750");

        hotel2 = new HotelType();        
        hotel2.setAddress(address2);
        bookingNo = "000002";        
        hotel2.setBookingNo(bookingNo);
        hotel2.setCcGuaranteeReq(false);        
        hotel2.setHotelServiceName("NiceView");
        hotel2.setHotelName("Cheap Hotel");      
        hotel2.setPrice(250.0f);    
        bookingNoToHotelMap.put(bookingNo, hotel2);
        
        AddressType address3 = new AddressType();
        address3.setCity("Copenhagen");
        address3.setCountry("Denmark");
        address3.setHouseNo("7");
        address3.setStreet("Magnetovej");
        address3.setZip("2750");

        hotel3 = new HotelType();        
        hotel3.setAddress(address3);
        bookingNo = "000003";        
        hotel3.setBookingNo(bookingNo);
        hotel3.setCcGuaranteeReq(false);        
        hotel3.setHotelServiceName("NiceView");
        hotel3.setHotelName("DTU Dormitory"); 
        hotel3.setPrice(50.0f);  
        bookingNoToHotelMap.put(bookingNo, hotel3);        
    }
    
    public HotelArrayType listHotels(HotelQueryType hotelQuery) {
        //hotelQuery.getArrivalDate();
        //hotelQuery.getDepartureDate();
        //hotelQuery.getCity();
        HotelArrayType result = new HotelArrayType();
        List<HotelType> hotels = result.getHotels();
        hotels.add(hotel1);
        hotels.add(hotel2);
        hotels.add(hotel3);
        return result;
    }
    
    public boolean isValidHotel(String bookingNo){
        return bookingNoToHotelMap.containsKey(bookingNo);
    }
    
    public boolean isCCGuaranteeRequired(String bookingNo){
        HotelType hotel = bookingNoToHotelMap.get(bookingNo);
        return hotel.isCcGuaranteeReq();
    }
}
