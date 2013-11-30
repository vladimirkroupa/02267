/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ming
 */
public class NewEmptyJUnitTest {
    
    public NewEmptyJUnitTest() {
    }
    @Test
    public void toDate() {
      String dateinput="2013-12-06T12:00:00.000+00:00";
        DatatypeFactory df = null;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            //  Logger.getLogger(lameduckService.class.getName()).log(Level.SEVERE, null, ex);
        }
        XMLGregorianCalendar date = df.newXMLGregorianCalendar(dateinput);
        System.out.println(date);

    }
}
