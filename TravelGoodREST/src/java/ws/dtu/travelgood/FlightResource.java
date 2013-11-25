package ws.dtu.travelgood;

import common.DateUtils;
import flightdata.FlightInfoList;
import flightdata.FlightInfoType;
import flightdata.GetFlightQuery;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Henry Lie
 */
@Path("flights")
public class FlightResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public FlightInfoList listFlights(
            @QueryParam("date") String flightDate,
            @QueryParam("startDest") String startDest,
            @QueryParam("finalDest") String finalDest){

        GetFlightQuery flightQuery = new GetFlightQuery();
        flightQuery.setDate(DateUtils.toXmlGregCal(flightDate));
        flightQuery.setStartDest(startDest);
        flightQuery.setFinalDest(finalDest);
        
        FlightInfoList flightInfoList = getFlights(flightQuery) ;
        for (FlightInfoType flight : flightInfoList.getFlightInfo()) {
            FlightBookingData.addOfferedBooking(flight);
        }
        
        return flightInfoList;
    }

    private static FlightInfoList getFlights(flightdata.GetFlightQuery getFlightQuery) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.getFlights(getFlightQuery);
    }
}
