
package travelgoodtypes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the travelgoodtypes package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateItineraryRequest_QNAME = new QName("urn://travelGoodTypes", "createItineraryRequest");
    private final static QName _OperationSuccessResult_QNAME = new QName("urn://travelGoodTypes", "operationSuccessResult");
    private final static QName _ItineraryNo_QNAME = new QName("urn://travelGoodTypes", "itineraryNo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: travelgoodtypes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ItineraryNoWithCCInfo }
     * 
     */
    public ItineraryNoWithCCInfo createItineraryNoWithCCInfo() {
        return new ItineraryNoWithCCInfo();
    }

    /**
     * Create an instance of {@link ItineraryFault }
     * 
     */
    public ItineraryFault createItineraryFault() {
        return new ItineraryFault();
    }

    /**
     * Create an instance of {@link Itinerary }
     * 
     */
    public Itinerary createItinerary() {
        return new Itinerary();
    }

    /**
     * Create an instance of {@link FlightBooking }
     * 
     */
    public FlightBooking createFlightBooking() {
        return new FlightBooking();
    }

    /**
     * Create an instance of {@link HotelBooking }
     * 
     */
    public HotelBooking createHotelBooking() {
        return new HotelBooking();
    }

    /**
     * Create an instance of {@link ItineraryNoWithBookingNo }
     * 
     */
    public ItineraryNoWithBookingNo createItineraryNoWithBookingNo() {
        return new ItineraryNoWithBookingNo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://travelGoodTypes", name = "createItineraryRequest")
    public JAXBElement<String> createCreateItineraryRequest(String value) {
        return new JAXBElement<String>(_CreateItineraryRequest_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationSuccessType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://travelGoodTypes", name = "operationSuccessResult")
    public JAXBElement<OperationSuccessType> createOperationSuccessResult(OperationSuccessType value) {
        return new JAXBElement<OperationSuccessType>(_OperationSuccessResult_QNAME, OperationSuccessType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://travelGoodTypes", name = "itineraryNo")
    public JAXBElement<String> createItineraryNo(String value) {
        return new JAXBElement<String>(_ItineraryNo_QNAME, String.class, null, value);
    }

}
