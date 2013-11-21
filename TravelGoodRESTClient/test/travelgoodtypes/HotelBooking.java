
package travelgoodtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import hotelreservationtypes.HotelType;


/**
 * <p>Java class for hotelBooking complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="hotelBooking">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hotelBooking" type="{urn://hotelReservationTypes}hotelType"/>
 *         &lt;element name="hotelBookingStatus" type="{urn://travelGoodTypes}statusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hotelBooking", propOrder = {
    "hotelBooking",
    "hotelBookingStatus"
})
public class HotelBooking {

    @XmlElement(required = true)
    protected HotelType hotelBooking;
    @XmlElement(required = true)
    protected StatusType hotelBookingStatus;

    /**
     * Gets the value of the hotelBooking property.
     * 
     * @return
     *     possible object is
     *     {@link HotelType }
     *     
     */
    public HotelType getHotelBooking() {
        return hotelBooking;
    }

    /**
     * Sets the value of the hotelBooking property.
     * 
     * @param value
     *     allowed object is
     *     {@link HotelType }
     *     
     */
    public void setHotelBooking(HotelType value) {
        this.hotelBooking = value;
    }

    /**
     * Gets the value of the hotelBookingStatus property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getHotelBookingStatus() {
        return hotelBookingStatus;
    }

    /**
     * Sets the value of the hotelBookingStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setHotelBookingStatus(StatusType value) {
        this.hotelBookingStatus = value;
    }

}
