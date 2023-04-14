import datalayer.DataLayer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
public class DataLayerTests {
    @Test
    public void helloWorld() {
        assertEquals("Hello, World!", "Hello, World!");
    }

    @Test
    public void testLoadDriver() {
        DataLayer dl = new DataLayer();
        boolean loaded = dl.loadDriver();
        assert(loaded);
    }

    @Test
    public void testConnection() {
        DataLayer dl = new DataLayer();
        dl.loadDriver();
        // specific to local machine
        dl.setUserName("root");
        dl.setPassword("new123Password!");
        boolean connected = dl.getConnection("classicmodels");
        assert(connected);
    }

}
