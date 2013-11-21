
package travelgoodtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import flightdata.FlightInfoType;


/**
 * <p>Java class for flightBooking complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="flightBooking">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flightBooking" type="{urn:flightData}FlightInfoType"/>
 *         &lt;element name="flightBookingStatus" type="{urn://travelGoodTypes}statusType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "flightBooking", propOrder = {
    "flightBooking",
    "flightBookingStatus"
})
public class FlightBooking {

    @XmlElement(required = true)
    protected FlightInfoType flightBooking;
    @XmlElement(required = true)
    protected StatusType flightBookingStatus;

    /**
     * Gets the value of the flightBooking property.
     * 
     * @return
     *     possible object is
     *     {@link FlightInfoType }
     *     
     */
    public FlightInfoType getFlightBooking() {
        return flightBooking;
    }

    /**
     * Sets the value of the flightBooking property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlightInfoType }
     *     
     */
    public void setFlightBooking(FlightInfoType value) {
        this.flightBooking = value;
    }

    /**
     * Gets the value of the flightBookingStatus property.
     * 
     * @return
     *     possible object is
     *     {@link StatusType }
     *     
     */
    public StatusType getFlightBookingStatus() {
        return flightBookingStatus;
    }

    /**
     * Sets the value of the flightBookingStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *     
     */
    public void setFlightBookingStatus(StatusType value) {
        this.flightBookingStatus = value;
    }

}
