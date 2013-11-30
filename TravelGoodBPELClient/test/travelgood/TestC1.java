/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import dk.dtu.imm.fastmoney.types.ExpirationDateType;
import flightdata.FlightInfoType;
import hotelreservationtypes.HotelType;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import travelgoodtypes.XItinerary;

/**
 *
 * @author ming
 */
public class TestC1 {
    static String itNo="no.8989";
    static String cID="id.9999";
    public TestC1() {
          
    }
    
    @BeforeClass
    public static void setUpClass() {
         //reset the flight which are booked; without this, 
        //second running of this test will fail since we dont allow to book a flight which is already booked
        DataReset.lameDuckDataReset(""); 
        DataReset.HotelDataReset("");
        //initial process by create itinerary
        travelgood.XCreateQuery createQuery= new travelgood.XCreateQuery();
        createQuery.setCustomerID(cID);
        createQuery.setItineraryNo(itNo);
        OperationsBPEL.createItineraryOperation(createQuery);
    }
    
@Test
    public void C1_test() throws CancelBookingFault{
         List<FlightInfoType> flightList;
         List<HotelType> hotelList;
        //palnning a flight
        flightList=OperationsBPEL.listFlight("CPH","OSL",  CommonMethod.toDate("2013-12-06T12:00:00.000+00:00"),cID,itNo);
        OperationsBPEL.addFlight(flightList.get(0),cID,itNo);     
         
          //planning a hotel
        hotelList=OperationsBPEL.listHotel("Copenhagen",  CommonMethod.toDate("2013-12-06T12:00:00.000+00:00"),  CommonMethod.toDate("2013-12-12T12:00:00.000+00:00"),cID,itNo);
        OperationsBPEL.addHotel(hotelList.get(0),cID,itNo);
          //planning a hotel
        hotelList=OperationsBPEL.listHotel("Copenhagen",  CommonMethod.toDate("2013-12-06T12:00:00.000+00:00"),  CommonMethod.toDate("2013-12-12T12:00:00.000+00:00"),cID,itNo);
        OperationsBPEL.addHotel(hotelList.get(0),cID,itNo);
        //get itinerary
         XItinerary itinerary_book_before= OperationsBPEL.getItinerary(cID,itNo);
         assertStatus("unconfirmed",itinerary_book_before);
         System.out.println("=================booking===================");
        //book itinerary
         CreditCardInfoType cc= createCreditCard();
         OperationsBPEL.bookItineray(cID,itNo,cc);
         //get itinerary
         XItinerary itinerary_book_after=OperationsBPEL.readItinerary(cID,itNo);
          CommonMethod.printStatus(itinerary_book_after);
         //cancel booking
         System.out.println("===============canceling book====================");
         OperationsBPEL.CancelBooking(cID,itNo);
         XItinerary itinerary=OperationsBPEL.readItinerary(cID,itNo);
          CommonMethod.printStatus(itinerary);
         assertStatus("canceled",itinerary);
    }
   
    
    public void assertStatus(String assertStatus, XItinerary  itinerary){   
        for(int i=0;i<itinerary.getFlightBookingList().size();i++){
            String bookingStatus=itinerary.getFlightBookingList().get(i).getBookingStatus();   
            assertEquals(assertStatus,bookingStatus);
        }
        
        for(int i=0;i<itinerary.getHotelBookingList().size();i++){         
            String bookingStatus=itinerary.getHotelBookingList().get(i).getBookingStatus();
            assertEquals(assertStatus,bookingStatus);
        }     
    }
    
    public CreditCardInfoType createCreditCard(){
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
        ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(5);
        expDate.setYear(9);
        creditcardInfo.setName("Anne Strandberg");
        creditcardInfo.setNumber("50408816");
        creditcardInfo.setExpirationDate(expDate);
        return creditcardInfo;
    }

}
