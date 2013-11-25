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
    
    @Test
    public void testGetFlights(){
        Client client = new Client();
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/flights");
        webResource.accept(MediaType.APPLICATION_XML);
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("date","2013-09-18");
        param.add("startDest","CPH");
        param.add("finalDest","LHR");
        String response = webResource.queryParams(param).get(String.class);
        
        Assert.assertTrue(response.contains("1234567"));
    }
}
