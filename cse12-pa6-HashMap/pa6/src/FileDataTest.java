import static org.junit.Assert.*;

import org.junit.*;

public class FileDataTest {

    @Test
    public void testFileData() {
        FileData fileSys = new FileData("Assignment6", "/home", "2020/03/28");
        assertEquals("Assignment6", fileSys.name);
        assertEquals("/home", fileSys.dir);
        assertEquals("2020/03/28", fileSys.lastModifiedDate);
    }

    @Test
    public void testFileData2() {
        FileData fileSys = new FileData("Assignment6", "/home", "2020/03/28");
        assertEquals("{Name: Assignment6, Directory: /home, Modified Date: 2020/03/28}", fileSys.toString());
    }
}
