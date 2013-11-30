/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import flightdata.FlightInfoType;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import travelgoodtypes.XItinerary;

/**
 *
 * @author ming
 */
public class TestP2 {

    static String itNo = "no.3344";
    static String cID = "id.5555";

    public TestP2() {
    }

    @BeforeClass
    public static void setUpClass() {
        //reset the flight which are booked; without this, 
        //second running of this test will fail since we dont allow to book a flight which is already booked
        DataReset.lameDuckDataReset("");
        DataReset.HotelDataReset("");
        //initial process by create itinerary
        travelgood.XCreateQuery createQuery = new travelgood.XCreateQuery();
        createQuery.setCustomerID(cID);
        createQuery.setItineraryNo(itNo);
        OperationsBPEL.createItineraryOperation(createQuery);
    }

    @Test
    public void p2_test() throws CancelItineraryFault {
        List<FlightInfoType> flightList;
        //palnning a flight
        flightList = OperationsBPEL.listFlight("CPH", "OSL", CommonMethod.toDate("2013-12-06T12:00:00.000+00:00"), cID, itNo);
        OperationsBPEL.addFlight(flightList.get(0), cID, itNo);
          //get itinerary
         XItinerary itinerary_book_before= OperationsBPEL.getItinerary(cID,itNo);
         assertStatus("unconfirmed",itinerary_book_before);
           //  cancel itinerary
         ResultType cancelResult=OperationsBPEL.CancelItinerary(cID,itNo);
         assertEquals("success",cancelResult.getStatus());
    }
        public void assertStatus(String assertStatus, XItinerary  itinerary){   
        for(int i=0;i<itinerary.getFlightBookingList().size();i++){
            String bookingNo=itinerary.getFlightBookingList().get(i).getFlightInfo().getBookingNumber();
            String bookingStatus=itinerary.getFlightBookingList().get(i).getBookingStatus();   
            System.out.println("Itineary contain flight : "+bookingNo +" , status : "+bookingStatus);
            assertEquals(assertStatus,bookingStatus);
        }
        
        for(int i=0;i<itinerary.getHotelBookingList().size();i++){
            String bookingNo=itinerary.getHotelBookingList().get(i).getHotelInfo().getBookingNo();
            String bookingStatus=itinerary.getHotelBookingList().get(i).getBookingStatus();
            System.out.println("Itineary contain hotel : "+  bookingNo +" , status : "+bookingStatus);
            assertEquals(assertStatus,bookingStatus);
        }     
    }
}
