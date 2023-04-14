import datalayer.DataLayer;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.crypto.Data;

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
        dl.rebuildTables();
        int result = dl.addFacultyMember("Severus", "Snape", "ss@hogwarts.edu", "magicIsCool",
                "1111111111", "1 hogsmeade village");
        assert(result != -1);
    }

    @Test
    public void testAddAbstract() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        int result = dl.addAbstract("Research 1", "My new research project");
        assert(result != -1);
    }

    @Test
    public void testAddFacultyDriver() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        boolean result = dl.addFacultyDriver("Severus", "Snape", "ss@hogwarts.edu", "magicIsCool",
                "1111111111", "1 hogsmeade village", "Research 1", "My new research project");
        assert(result);
    }

    @Test
    public void testAddStudent() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        int result = dl.addStudent("Harry", "Potter", "hp@hogwarts.edu", "weewoo");
        assert(result != -1);
    }

    @Test
    public void testAddStudentTopic() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        int result = dl.addStudentTopic("magic");
        assert (result != -1);
    }

    @Test
    public void testAddStudentDriver() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        boolean result = dl.addStudentDriver("Harry", "Potter", "hp@hogwarts.edu", "weewoo",
                "magic");
        assert (result);
    }

    @Test
    public void testAddGuest() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        int result = dl.addGuest("harrypotter","hp@hogwarts.edu", "weewoo");
        assert (result != -1);
    }

    @Test
    public void testAddGuestTopic() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        int result = dl.addGuestTopic("magical magic");
        assert (result != -1);
    }

    @Test
    public void testAddGuestDriver() {
        DataLayer dl = new DataLayer();
        dl.rebuildTables();
        boolean result = dl.addGuestDriver("harrypotter","hp@hogwarts.edu", "weewoo",
                "magical magic");
        assert (result);
    }


}
