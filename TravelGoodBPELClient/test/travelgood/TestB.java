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
public class TestB {

    static String itNo = "no.6677";
    static String cID = "id.8888";

    public TestB() {
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
    public void test_B1() {
        List<FlightInfoType> flightList;
        List<HotelType> hotelList;
        //palnning a flight
        flightList = OperationsBPEL.listFlight("CPH", "OSL",  CommonMethod.toDate("2013-12-06T12:00:00.000+00:00"), cID, itNo);
        OperationsBPEL.addFlight(flightList.get(0), cID, itNo);//"Oslo, Norway"

        ////palnning a flight // failed at this booking, since the price exceed the credit card amount
        flightList = OperationsBPEL.listFlight("CPH","PEK",  CommonMethod.toDate("2013-12-02T12:00:00.000+00:00"), cID, itNo);
        OperationsBPEL.addFlight(flightList.get(0), cID, itNo);
        
        //planning a hotel
        hotelList = OperationsBPEL.listHotel("Copenhagen",  CommonMethod.toDate("2013-12-06T12:00:00.000+00:00"),  CommonMethod.toDate("2013-12-08T12:00:00.000+00:00"), cID, itNo);
        OperationsBPEL.addHotel(hotelList.get(0), cID, itNo);

        //get itinerary
        XItinerary itinerary_book_before = OperationsBPEL.getItinerary(cID, itNo);
        assertStatus("unconfirmed", itinerary_book_before);
        System.out.println("=================booking===================");
        //book itinerary
        CreditCardInfoType cc = createCreditCard();
        OperationsBPEL.bookItineray(cID, itNo, cc);
        //get itinerary
        XItinerary itinerary_book_after = OperationsBPEL.getItinerary(cID, itNo);
         CommonMethod.printStatus(itinerary_book_after);
        assertEquals(itinerary_book_after.getFlightBookingList().get(0).getBookingStatus(),"canceled");
        assertEquals(itinerary_book_after.getFlightBookingList().get(1).getBookingStatus(),"unconfirmed");
        assertEquals(itinerary_book_after.getHotelBookingList().get(0).getBookingStatus(),"unconfirmed");
    }

    public void assertStatus(String assertStatus, XItinerary itinerary) {
        for (int i = 0; i < itinerary.getFlightBookingList().size(); i++) {
            String bookingNo = itinerary.getFlightBookingList().get(i).getFlightInfo().getBookingNumber();
            String bookingStatus = itinerary.getFlightBookingList().get(i).getBookingStatus();
            int price=itinerary.getFlightBookingList().get(i).getFlightInfo().getFlightPrice();
            System.out.println("Itineary contain flight: booking number : " + bookingNo +" , price: "+ price +" , status : " + bookingStatus);
            assertEquals(assertStatus, bookingStatus);
        }

        for (int i = 0; i < itinerary.getHotelBookingList().size(); i++) {
            String bookingNo = itinerary.getHotelBookingList().get(i).getHotelInfo().getBookingNo();
            String bookingStatus = itinerary.getHotelBookingList().get(i).getBookingStatus();
            float price=itinerary.getHotelBookingList().get(i).getHotelInfo().getPrice();
            System.out.println("Itineary contain hotel: booking number : " + bookingNo  +" , price: "+ price +" , status : " + bookingStatus);
            assertEquals(assertStatus, bookingStatus);
        }
    }

    public CreditCardInfoType createCreditCard() {
        CreditCardInfoType creditcardInfo = new CreditCardInfoType();
        ExpirationDateType expDate = new ExpirationDateType();
        expDate.setMonth(7);
        expDate.setYear(9);
        creditcardInfo.setName("Bech Camilla");
        creditcardInfo.setNumber("50408822");
        creditcardInfo.setExpirationDate(expDate);
        return creditcardInfo;
    }

 
}
