package dk.dtu.ws.hotelservice;

import hotelservice._02267.dtu.dk.wsdl.BookHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.BookingCancellationType;
import hotelservice._02267.dtu.dk.wsdl.CancelHotelOperationFault;
import hotelservice._02267.dtu.dk.wsdl.HotelBookingArrayType;
import hotelservice._02267.dtu.dk.wsdl.HotelBookingType;
import hotelservice._02267.dtu.dk.wsdl.HotelQueryType;
import java.util.Calendar;
import java.util.Date;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author prasopes
 */
@WebService(serviceName = "hotelService", portName = "hotelServiceSOAPPort", endpointInterface = "hotelservice._02267.dtu.dk.wsdl.HotelServicePortType", targetNamespace = "http://dk.dtu.02267.hotelservice/WSDL", wsdlLocation = "WEB-INF/wsdl/HotelReservation/hotelservice.wsdl")
public class HotelReservation {

    private HotelRepository hotels = new HotelRepository();
    
    public HotelBookingArrayType getHotelsOperation(HotelQueryType hotelQuery) {
        return hotels.listHotels(hotelQuery);
    }
    
    public boolean bookHotelOperation(HotelBookingType hotelBooking) throws BookHotelOperationFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean cancelHotelOperation(BookingCancellationType bookingCancellation) throws CancelHotelOperationFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
        private Date toDate(XMLGregorianCalendar calendar) {
        Calendar cal = calendar.toGregorianCalendar();
        return cal.getTime();
    }
    
}