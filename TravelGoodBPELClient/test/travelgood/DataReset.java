/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood;

/**
 *
 * @author ming
 */
public class DataReset {
      public static void lameDuckDataReset(java.lang.String resetQuery) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        port.reset(resetQuery);
    }

   public static void HotelDataReset(java.lang.String reset) {
        hotelservice._02267.dtu.dk.wsdl.HotelService service = new hotelservice._02267.dtu.dk.wsdl.HotelService();
        hotelservice._02267.dtu.dk.wsdl.HotelServicePortType port = service.getHotelServiceSOAPPort();
        port.resetOperation(reset);
    }
}
