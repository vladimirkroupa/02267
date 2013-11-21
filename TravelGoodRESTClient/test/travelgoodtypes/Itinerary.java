
package travelgoodtypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itineraryNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="itineraryStatus" type="{urn://travelGoodTypes}statusType"/>
 *         &lt;element name="flightBookingList" type="{urn://travelGoodTypes}flightBooking" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="hotelBookingList" type="{urn://travelGoodTypes}hotelBooking" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "itineraryNo",
    "itineraryStatus",
    "flightBookingList",
    "hotelBookingList"
})
@XmlRootElement(name = "itinerary")
public class Itinerary {

    @XmlElement(required = true)
    protected String itineraryNo;
    @XmlElement(required = true)
    protected StatusType itineraryStatus;
    protected List<FlightBooking> flightBookingList;
    protected List<HotelBooking> hotelBookingList;

    /**
     * Gets the value of the itineraryNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItineraryNo() {
        return itineraryNo;
    }

    /**
     * Sets the value of the itineraryNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItineraryNo(String value) {
        this.itineraryNo = value;
    }

    /**
     * Gets the value of the itineraryStatus property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getItineraryStatus() {
        return itineraryStatus;
    }

    /**
     * Sets the value of the itineraryStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setItineraryStatus(StatusType value) {
        this.itineraryStatus = value;
    }

    /**
     * Gets the value of the flightBookingList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flightBookingList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFlightBookingList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FlightBooking }
     * 
     * 
     */
    public List<FlightBooking> getFlightBookingList() {
        if (flightBookingList == null) {
            flightBookingList = new ArrayList<FlightBooking>();
        }
        return this.flightBookingList;
    }

    /**
     * Gets the value of the hotelBookingList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hotelBookingList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHotelBookingList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HotelBooking }
     * 
     * 
     */
    public List<HotelBooking> getHotelBookingList() {
        if (hotelBookingList == null) {
            hotelBookingList = new ArrayList<HotelBooking>();
        }
        return this.hotelBookingList;
    }

}
