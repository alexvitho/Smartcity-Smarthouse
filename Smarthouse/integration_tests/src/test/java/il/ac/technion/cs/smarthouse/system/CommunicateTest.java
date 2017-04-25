package il.ac.technion.cs.smarthouse.system;

import org.junit.Test;

import il.ac.technion.cs.smarthouse.system.Communicate;

/** @author Elia Traore
 * @since Jan 16, 2017 */
public class CommunicateTest {
    // JUnits shouldnt be static!
    @Test @SuppressWarnings("static-method") public void sendEmailFromHouseTest() {
        assert Communicate.throughEmailFromHere("smarthouse5777@gmail.com", "ya bishhhh #worksss") != null;
    }
}
