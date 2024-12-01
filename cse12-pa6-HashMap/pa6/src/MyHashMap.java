import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements DefaultMap<K, V> {
	public static final double DEFAULT_LOAD_FACTOR = 0.75;
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	public static final String ILLEGAL_ARG_CAPACITY = "Initial Capacity must be non-negative";
	public static final String ILLEGAL_ARG_LOAD_FACTOR = "Load Factor must be positive";
	public static final String ILLEGAL_ARG_NULL_KEY = "Keys must be non-null";
	
	private double loadFactor;
	private int capacity;
	private int size;

	// Use this instance variable for Separate Chaining conflict resolution
	private List<HashMapEntry<K, V>>[] buckets;  
	
	// Use this instance variable for Linear Probing
	private HashMapEntry<K, V>[] entries; 	

	public MyHashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * 
	 * @param initialCapacity the initial capacity of this MyHashMap
	 * @param loadFactor the load factor for rehashing this MyHashMap
	 * @throws IllegalArgumentException if initialCapacity is negative or loadFactor not
	 * positive
	 */
	@SuppressWarnings("unchecked")
	public MyHashMap(int initialCapacity, double loadFactor) throws IllegalArgumentException {
		if(initialCapacity < 0) {throw new IllegalArgumentException(ILLEGAL_ARG_CAPACITY);}
		if(loadFactor <= 0) {throw new IllegalArgumentException(ILLEGAL_ARG_LOAD_FACTOR);}
		
		this.loadFactor = loadFactor;
		this.capacity = initialCapacity;
		this.size = 0;

		// if you use Separate Chaining
		buckets = (List<HashMapEntry<K, V>>[]) new List<?>[capacity];

		// if you use Linear Probing
		entries = (HashMapEntry<K, V>[]) new HashMapEntry<?, ?>[initialCapacity];
	}

	@Override
	public boolean put(K key, V value) throws IllegalArgumentException {
		if(key == null) {throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);}
		expandCapacity();
		// can also use key.hashCode() assuming key is not nullexpandCapacity();
		int keyHash = Math.abs(Objects.hashCode(key) % capacity);
		
		HashMapEntry <K,V> entry = new HashMapEntry<>(key, value);
		if(containsKey(key)){
			return false;
		}
		if(buckets[keyHash] == null){
			buckets[keyHash] = new ArrayList<HashMapEntry<K,V>>();
		}
		buckets[keyHash].add(entry);
		size += 1;
		return true;
	}

	@Override
	public boolean replace(K key, V newValue) throws IllegalArgumentException {
		if(key == null) {throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);}
		int keyHash = Math.abs(Objects.hashCode(key) % capacity);
		if(buckets[keyHash] == null || !containsKey(key)) {return false;}
		for(int i=0; i<buckets[keyHash].size(); i++){
			if(buckets[keyHash].get(i).getKey().equals(key)){
				buckets[keyHash].get(i).setValue(newValue);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(K key) throws IllegalArgumentException {
		if(key == null) {throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);}
		if(containsKey(key)){
			int keyHash = Math.abs(Objects.hashCode(key) % capacity);
			for(int i=0;i<buckets[keyHash].size();i++){
				if(buckets[keyHash].get(i).getKey().equals(key)) {
					buckets[keyHash].remove(i);
					size -= 1;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void set(K key, V value) throws IllegalArgumentException {
		if(key == null) {throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);}
		if(put(key, value)) {return;}
		replace(key, value);
	}

	@Override
	public V get(K key) throws IllegalArgumentException {
		if(key == null) {throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);}
		int keyHash = Math.abs(Objects.hashCode(key) % capacity);
		if(buckets[keyHash] != null && containsKey(key)) {
			for(int i=0; i<buckets[keyHash].size(); i++){
				if(buckets[keyHash].get(i).getKey().equals(key)) {
					return buckets[keyHash].get(i).getValue();
				}
			}
		}
		return null;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public boolean containsKey(K key) throws IllegalArgumentException {
		if(key == null) {throw new IllegalArgumentException(ILLEGAL_ARG_NULL_KEY);}
		int keyHash = Math.abs(Objects.hashCode(key) % capacity);
		if(buckets[keyHash] == null) {return false;}
		for(int i=0;i<buckets[keyHash].size();i++){
			if(buckets[keyHash].get(i).getKey().equals(key)) {return true;}
		}
		return false;
	}

	@Override
	public List<K> keys() {
		List<K> keyList = new ArrayList<K>();
		for(int i=0; i<buckets.length; i++){
			if(buckets[i] != null) {
				for(int j=0;j<buckets[i].size();j++){
					keyList.add(buckets[i].get(j).getKey());
				}
			}	
		}
		return keyList;
	}
	
	@SuppressWarnings("unchecked")
	private void expandCapacity(){
		if(((double)size) / capacity >= loadFactor) {
			this.capacity *= 2;
			this.size = 0;
			List<HashMapEntry<K,V>>[] newBuckets = (List<HashMapEntry<K, V>>[]) new List<?>[capacity];
			List<HashMapEntry<K,V>>[] temp = buckets;
			buckets = newBuckets;
			for(int i=0; i<temp.length; i++){
				if(temp[i] != null) {
					for(int j=0;j<temp[i].size();j++){
						put(temp[i].get(j).getKey(),temp[i].get(j).getValue());
					}
				}	
			}
		}
	}

	private static class HashMapEntry<K, V> implements DefaultMap.Entry<K, V> {
		
		K key;
		V value;
		
		private HashMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public void setValue(V value) {
			this.value = value;
		}
	}
}
