package testutil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import flightdata.FlightInfoList;
import hotelreservationtypes.HotelList;
import javax.ws.rs.core.MultivaluedMap;
import travelgoodtypes.Itinerary;

/**
 * {@link ItineraryResource} client.
 * 
 * @author prasopes
 */
public class ItineraryResourceClient {

    private static final String ITINERARY = "http://localhost:8080/TravelGoodREST/webresources/itinerary/";
    
    private final Client client = new Client();
    
    public ResponseWrapper<String> createItinerary() {
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/itineraries");
        ClientResponse response = webResource.post(ClientResponse.class);
        return new ResponseWrapper<>(String.class, response);
    }

    public ResponseWrapper<Itinerary> getItinerary(String itineraryNo) {
        WebResource webResource = client.resource(ITINERARY + itineraryNo);
        ClientResponse response = webResource.get(ClientResponse.class);
        return new ResponseWrapper<>(Itinerary.class, response);
    }
    
    public ResponseWrapper<HotelList> listHotels(String city, String arrival, String departure) {
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/hotels");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("city", city);
        param.add("arrivalDate", arrival);
        param.add("departureDate", departure);
        ClientResponse response = webResource.queryParams(param).get(ClientResponse.class);
        return new ResponseWrapper<>(HotelList.class, response);
    }

    public ResponseWrapper<FlightInfoList> listFlights(String date, String from, String to) {
        WebResource webResource = client.resource("http://localhost:8080/TravelGoodREST/webresources/flights");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("date", date);
        param.add("startDest", from);
        param.add("finalDest", to);
        ClientResponse response = webResource.queryParams(param).get(ClientResponse.class);
        return new ResponseWrapper<>(FlightInfoList.class, response);
    }

    public ClientResponse addFlight(String itineraryNo, String flightBookingNo) {
        WebResource webResource = client.resource(ITINERARY + itineraryNo + "/flight/" + flightBookingNo);
        ClientResponse res = webResource.put(ClientResponse.class);
        return res;
    }

    public ClientResponse addHotel(String itineraryNo, String hotelBookingNo) {
        WebResource webResource = client.resource(ITINERARY + itineraryNo + "/hotel/" + hotelBookingNo);
        ClientResponse res = webResource.put(ClientResponse.class);
        return res;
    }
    
    public ClientResponse bookItinerary(String itineraryNo, String ccName, String ccNumber, String expMonth, String expYear) {
        WebResource webResource = client.resource(ITINERARY + itineraryNo + "/book");
        MultivaluedMap param = new MultivaluedMapImpl();
        param.add("ccName", ccName);
        param.add("ccNumber", ccNumber);
        param.add("expMonth", expMonth);
        param.add("expYear", expYear);
        ClientResponse res = webResource.queryParams(param).post(ClientResponse.class);
        return res;
    }
    
    public ClientResponse cancelItinerary(String itineraryNo) {
        WebResource webResource = client.resource(ITINERARY + itineraryNo);
        ClientResponse res = webResource.delete(ClientResponse.class);
        return res;
    }
    
}
