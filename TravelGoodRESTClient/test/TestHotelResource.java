import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import hotelreservationtypes.AddressType;
import hotelreservationtypes.HotelList;
import hotelreservationtypes.HotelType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Assert;
import org.junit.Test;
import testutil.ItineraryResourceClient;

/**
 *
 * @author Henry Lie
 */
public class TestHotelResource {
    
    private final ItineraryResourceClient client = new ItineraryResourceClient();
    
    @Test
    public void testGetHotels(){
        HotelList response = client.listHotels("Copenhagen", "2013-12-10", "2013-12-22").entity();
        
        Assert.assertEquals(expectedHotel1().getHotelName(), response.getHotels().get(0).getHotelName());
        Assert.assertEquals(expectedHotel1().getAddress().getCity(), response.getHotels().get(0).getAddress().getCity());
        Assert.assertEquals(expectedHotel1().getAddress().getCountry(), response.getHotels().get(0).getAddress().getCountry());
        Assert.assertEquals(expectedHotel1().getAddress().getHouseNo(), response.getHotels().get(0).getAddress().getHouseNo());
        Assert.assertEquals(expectedHotel1().getAddress().getStreet(), response.getHotels().get(0).getAddress().getStreet());
        Assert.assertEquals(expectedHotel1().getAddress().getZip(), response.getHotels().get(0).getAddress().getZip());
        Assert.assertEquals(expectedHotel1().getPrice(), response.getHotels().get(0).getPrice(), 0.00001);
        Assert.assertEquals(expectedHotel1().getHotelServiceName(), response.getHotels().get(0).getHotelServiceName());
        Assert.assertEquals(expectedHotel1().isCcGuaranteeReq(), response.getHotels().get(0).isCcGuaranteeReq());
        
        Assert.assertEquals(expectedHotel2().getHotelName(), response.getHotels().get(1).getHotelName());
        Assert.assertEquals(expectedHotel2().getAddress().getCity(), response.getHotels().get(1).getAddress().getCity());
        Assert.assertEquals(expectedHotel2().getAddress().getCountry(), response.getHotels().get(1).getAddress().getCountry());
        Assert.assertEquals(expectedHotel2().getAddress().getHouseNo(), response.getHotels().get(1).getAddress().getHouseNo());
        Assert.assertEquals(expectedHotel2().getAddress().getStreet(), response.getHotels().get(1).getAddress().getStreet());
        Assert.assertEquals(expectedHotel2().getAddress().getZip(), response.getHotels().get(1).getAddress().getZip());
        Assert.assertEquals(expectedHotel2().getPrice(), response.getHotels().get(1).getPrice(), 0.00001);
        Assert.assertEquals(expectedHotel2().getHotelServiceName(), response.getHotels().get(1).getHotelServiceName());
        Assert.assertEquals(expectedHotel2().isCcGuaranteeReq(), response.getHotels().get(1).isCcGuaranteeReq());        
        
    }
    
    private HotelType expectedHotel1() {
        HotelType ht = new HotelType();
        ht.setHotelName("DTU Hostel");
        AddressType address = new AddressType();
        address.setCity("Copenhagen");
        address.setCountry("Denmark");
        address.setHouseNo("27");
        address.setStreet("Elektrovej");
        address.setZip("2200");
        ht.setAddress(address);
        //ht.setBookingNo("00013");
        ht.setPrice(240.0f);
        ht.setCcGuaranteeReq(true);
        ht.setHotelServiceName("NiceView");
        return ht;
    }
    
    private HotelType expectedHotel2() {
        HotelType ht = new HotelType();
        ht.setHotelName("Royal Hotel");
        AddressType address = new AddressType();
        address.setCity("Copenhagen");
        address.setCountry("Denmark");
        address.setHouseNo("15");
        address.setStreet("Dynamovej");
        address.setZip("2200");
        ht.setAddress(address);
        //ht.setBookingNo("00013");
        ht.setPrice(600.0f);
        ht.setCcGuaranteeReq(true);
        ht.setHotelServiceName("NiceView");
        return ht;
    }
}
