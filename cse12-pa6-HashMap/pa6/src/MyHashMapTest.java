import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.*;

public class MyHashMapTest {
	
	private DefaultMap<String, String> testMap; // use this for basic tests
	private DefaultMap<String, String> mapWithCap; // use for testing proper rehashing
	public static final String TEST_KEY = "Test Key";
	public static final String TEST_VAL = "Test Value";
	
	@Before
	public void setUp() {
		testMap = new MyHashMap<>();
		mapWithCap = new MyHashMap<>(4, MyHashMap.DEFAULT_LOAD_FACTOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPut_nullKey() {
		testMap.put(null, TEST_VAL);
	}

	@Test
	public void testKeys_nonEmptyMap() {
		// You don't have to use array list 
		// This test will work with any object that implements List
		List<String> expectedKeys = new ArrayList<>(5);
		for(int i = 0; i < 5; i++) {
			// key + i is used to differentiate keys since they must be unique
			testMap.put(TEST_KEY + i, TEST_VAL + i);
			expectedKeys.add(TEST_KEY + i);
		}
		List<String> resultKeys = testMap.keys();
		// we need to sort because hash map doesn't guarantee ordering
		Collections.sort(resultKeys);
		assertEquals(expectedKeys, resultKeys);
	}
	
	/* Add more of your tests below */
	@Test
	public void testIsEmpty(){
		setUp();
		assertEquals(true, testMap.isEmpty());
		testMap.put("key1", "value2");
		assertEquals(false, testMap.isEmpty());
		testMap.remove(TEST_KEY);
		assertEquals(false, testMap.isEmpty());
		testMap.remove("key1");
		assertEquals(true, testMap.isEmpty());
	}

	@Test
	public void testPutReplace(){
		setUp();
		assertEquals(false, mapWithCap.replace("key1", "value1"));
		assertEquals(true, mapWithCap.put("key1", "value1"));
		assertEquals(true, mapWithCap.replace("key1", "value2"));
		mapWithCap.set("key2", "value2");
		mapWithCap.set("key1", "value3");
		assertEquals("value2", mapWithCap.get("key2"));
		assertEquals("value3", mapWithCap.get("key1"));
	}

	@Test
	public void testExpandCapacity(){
		setUp();
		mapWithCap.put("key0", "value0");
		mapWithCap.put("key1", "value1");
		mapWithCap.put("key2", "value2");
		mapWithCap.put("key3", "value3");
		assertEquals(4, mapWithCap.size());
	}

	@Test
	public void testKeys(){
		setUp();
		mapWithCap.put("key0", "value0");
		mapWithCap.put("key1", "value1");
		mapWithCap.put("key2", "value2");
		mapWithCap.put("key3", "value3");
		assertEquals(true, mapWithCap.containsKey("key3"));
		assertEquals(false, mapWithCap.containsKey("Key3"));
		for(int i=0; i<mapWithCap.keys().size(); i++)
		{
			assertEquals("key"+i, mapWithCap.keys().get(i));
		}
	}

	@Test
	public void testRemove(){
		setUp();
		mapWithCap.put("key0", "value0");
		mapWithCap.put("key1", "value1");
		mapWithCap.put("key2", "value2");
		mapWithCap.put("key3", "value3");
		assertEquals(false, mapWithCap.remove("key4"));
		assertEquals(true, mapWithCap.remove("key3"));
		assertEquals(false, mapWithCap.remove("key3"));
	}
}