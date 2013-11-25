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

/**
 *
 * @author Henry Lie
 */
public class TestHotelResource {
    
    public TestHotelResource() {
    }
    
    @Test
    public void testGetHotels(){
        Client client = new Client();
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/hotels");
        webResource.accept(MediaType.APPLICATION_XML);
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("city","Copenhagen");
        param.add("arrivalDate","2013-12-10");
        param.add("departureDate","2013-12-22");
        HotelList response = webResource.queryParams(param).get(HotelList.class);
        
        Assert.assertEquals(expectedHotel1().getHotelName(), response.getHotels().get(0).getHotelName());
        Assert.assertEquals(expectedHotel1().getAddress().getCity(), response.getHotels().get(0).getAddress().getCity());
        Assert.assertEquals(expectedHotel1().getAddress().getCountry(), response.getHotels().get(0).getAddress().getCountry());
        Assert.assertEquals(expectedHotel1().getAddress().getHouseNo(), response.getHotels().get(0).getAddress().getHouseNo());
        Assert.assertEquals(expectedHotel1().getAddress().getStreet(), response.getHotels().get(0).getAddress().getStreet());
        Assert.assertEquals(expectedHotel1().getAddress().getZip(), response.getHotels().get(0).getAddress().getZip());
        Assert.assertEquals(expectedHotel1().getPrice(), response.getHotels().get(0).getPrice(), 0.00001);
        Assert.assertEquals(expectedHotel1().getHotelServiceName(), response.getHotels().get(0).getHotelServiceName());
        Assert.assertEquals(expectedHotel1().isCcGuaranteeReq(), response.getHotels().get(0).isCcGuaranteeReq());
        
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
        ht.setPrice(240.0f);
        ht.setCcGuaranteeReq(true);
        ht.setHotelServiceName("NiceView");
        return ht;
    }
}
