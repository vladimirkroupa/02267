/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import flightdata.FlightInfoListType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.lameduck.BookFlightFault;

/**
 *
 * @author s114671
 */
public class lameduckJUnitTest {
    
    public lameduckJUnitTest() {
    }
@Test
    public void  testGetFlights() {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        flightdata.FlightInfoListType testResult=port.getFlights("", "", date("2013-12-06T14:30:00.000+00:00"));
        
        for(int i=0;i<testResult.getFlightInfo().size();i++){
             System.out.println("all start flight airport:"+testResult.getFlightInfo().get(i).getFlight().getStartAirpot());
        
        }
    } 

    public XMLGregorianCalendar date(String dateinput) {
            
            //Demos and Usage of javax.xml.datatype.DatatypeFactory.newXMLGregorianCalendar
            //(int year,int month,int day,int hour,int minute,int second,int millisecond,int timezone)
            DatatypeFactory df = null;
            try {
                df = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException ex) {
              //  Logger.getLogger(lameduckService.class.getName()).log(Level.SEVERE, null, ex);
            }
            XMLGregorianCalendar date = df.newXMLGregorianCalendar(dateinput);
            return date;

        }
}
