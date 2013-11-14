package ws.dtu.travelgood;

import hotelreservationtypes.HotelQuery;
import hotelreservationtypes.HotelQueryResult;
import hotelservice._02267.dtu.dk.wsdl.HotelService;
import hotelservice._02267.dtu.dk.wsdl.HotelServicePortType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("hotels")
public class HotelResource {
    
    @GET
    @Produces (MediaType.APPLICATION_XML)
    public HotelQueryResult listHotels(
            @QueryParam("city") String city, 
            @QueryParam("arrivalDate") String arrivalDate, 
            @QueryParam("departureDate") String departureDate) {        
        HotelQuery hotelQuery = createHotelQuery(city, arrivalDate, departureDate);
        HotelQueryResult result = getHotelsOperation(hotelQuery);
        return result;
    }
    
    private HotelQuery createHotelQuery(String city, String arrivalDate, String departureDate) {
        HotelQuery hqt = new HotelQuery();
        hqt.setCity(city);
        hqt.setArrivalDate(WSTypeConverter.toGregorianCalendar(arrivalDate));
        hqt.setDepartureDate(WSTypeConverter.toGregorianCalendar(departureDate));
        return hqt;
    }
    
    private static HotelQueryResult getHotelsOperation(HotelQuery hotelQuery) {
        HotelService service = new HotelService();
        HotelServicePortType port = service.getHotelServiceSOAPPort();
        return port.getHotelsOperation(hotelQuery);
    }
    
}
