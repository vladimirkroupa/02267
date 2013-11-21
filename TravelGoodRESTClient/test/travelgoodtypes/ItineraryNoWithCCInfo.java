
package travelgoodtypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;


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
 *         &lt;element name="ccInfo" type="{urn://types.fastmoney.imm.dtu.dk}creditCardInfoType"/>
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
    "ccInfo"
})
@XmlRootElement(name = "itineraryNoWithCCInfo")
public class ItineraryNoWithCCInfo {

    @XmlElement(required = true)
    protected String itineraryNo;
    @XmlElement(required = true)
    protected CreditCardInfoType ccInfo;

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
     * Gets the value of the ccInfo property.
     * 
     * @return
     *     possible object is
     *     {@link CreditCardInfoType }
     *     
     */
    public CreditCardInfoType getCcInfo() {
        return ccInfo;
    }

    /**
     * Sets the value of the ccInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreditCardInfoType }
     *     
     */
    public void setCcInfo(CreditCardInfoType value) {
        this.ccInfo = value;
    }

}
