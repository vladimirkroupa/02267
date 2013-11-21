
package travelgoodtypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for operationSuccessType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="operationSuccessType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="success"/>
 *     &lt;enumeration value="failed"/>
 *     &lt;enumeration value="partialSuccess"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "operationSuccessType")
@XmlEnum
public enum OperationSuccessType {

    @XmlEnumValue("success")
    SUCCESS("success"),
    @XmlEnumValue("failed")
    FAILED("failed"),
    @XmlEnumValue("partialSuccess")
    PARTIAL_SUCCESS("partialSuccess");
    private final String value;

    OperationSuccessType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperationSuccessType fromValue(String v) {
        for (OperationSuccessType c: OperationSuccessType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
