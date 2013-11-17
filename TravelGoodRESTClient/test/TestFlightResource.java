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
public class TestFlightResource {
    
    public TestFlightResource() {
    }
    
    @Test
    public void testGetFlights(){
        Client client = new Client();
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/flights");
        webResource.accept(MediaType.APPLICATION_XML);
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("date","2013-09-18");
        param.add("startDest","Copenhagen, Denmark");
        param.add("finalDest","London, Heathrow, England");
        String response = webResource.queryParams(param).get(String.class);
        System.out.println(response);
        Assert.assertTrue(response.contains("1234567"));
    }
}
