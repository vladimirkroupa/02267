/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
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
        String response = webResource.queryParams(param).get(String.class);
        System.out.println(response);
        Assert.assertTrue(response.contains("Royal Hotels"));

    }
}
