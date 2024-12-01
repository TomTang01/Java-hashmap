import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.*;

public class FileSystemTest {

    FileSystem fileSys;

    public void setUp() {
        fileSys = new FileSystem();
    }

    @Test
    public void testAdd() {
        setUp();
        assertEquals(true, fileSys.add("mySample.txt", "/home", "02/01/2021"));
        assertEquals(true, fileSys.add("mySample.txt", "/home/document", "02/01/2021"));
        assertEquals(false, fileSys.add("mySample.txt", "/home/document", "02/09/2021"));
    }

    @Test
    public void testRemove() {
        setUp();
        fileSys.add("mySample.txt", "/home", "02/01/2021");
        fileSys.add("mySample.txt", "/home/document", "02/01/2021");
        fileSys.add("mySample.txt", "/home/document", "02/09/2021");
        fileSys.add("mySample2.txt", "/home/document", "02/09/2021");
        fileSys.add("mySample.txt", "/home/document/PA6", "02/09/2021");
        assertEquals(true, fileSys.removeFile("mySample.txt", "/home/document"));
        assertEquals(false, fileSys.removeFile("mySample.txt", "/home/document"));
        assertEquals(true, fileSys.removeByName("mySample.txt"));
        assertEquals(null, fileSys.findFile("mySample.txt", "/home/document/PA6"));
        assertNotNull(fileSys.findFile("mySample2.txt", "/home/document"));
    }

    @Test
    public void testDateMap() {
        setUp();
        fileSys.add("mySample2.txt", "/home/document", "02/09/2021");
        fileSys.add("mySample.txt", "/home/document/PA6", "02/09/2021");
        ArrayList<FileData> dateList = fileSys.findFilesByDate("02/09/2021");
        assertEquals(1,fileSys.dateMap.size());
        assertEquals(2,dateList.size());
        assertEquals("mySample2.txt", dateList.get(0).name);
        assertEquals("mySample.txt", dateList.get(1).name);
    }
}
