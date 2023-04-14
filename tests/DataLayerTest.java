import datalayer.DataLayer;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataLayerTest {
    @BeforeClass public static void connect() {

        DataLayer.setUserName("root");
        DataLayer.setPassword("new123Password!");
        DataLayer.loadDriver();
        DataLayer.getConnection("CollegeConnection");
    }
    @Test
    public void testRebuildTables() {
        DataLayer dl = new DataLayer();
        boolean result = dl.rebuildTables();
        assert(result);
    }

    @Test
    public void testAddFacultyMember() {
        DataLayer dl = new DataLayer();
        boolean result = dl.addFacultyMember("Severus", "Snape", "ss@hogwarts.edu", "magicIsCool",
                "1111111111", "1 hogsmeade village");
        assert(true);
    }
}
