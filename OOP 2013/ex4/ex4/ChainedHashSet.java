import java.util.LinkedList;
import java.util.ArrayList;

public class ChainedHashSet extends SimpleHashSet {

	private int size, currentCapacity, previousCapacity;

	private ArrayList<LinkedList<String>> hashArray ;

	// ------------------------- three
	// constructors--------------------------------
	public ChainedHashSet() {
		super((float)3 / 4, (float)1 / 4);
		currentCapacity = defaultCapacity;
		previousCapacity = defaultCapacity;
		hashArray = new ArrayList<LinkedList<String>>();
		addToArray(hashArray, defaultCapacity);
		

	}

	public ChainedHashSet(float upperLoadFactor, float lowerLoadFactor) {
		super(upperLoadFactor, lowerLoadFactor);
		this.currentCapacity = defaultCapacity;
		hashArray = new ArrayList<LinkedList<String>>();
		addToArray(hashArray, defaultCapacity);

	}

	public ChainedHashSet(String[] data) {
	
		super((float)3 / 4, (float)1 / 4);
		this.size=0;
		currentCapacity = defaultCapacity;
		previousCapacity = defaultCapacity;
		hashArray = new ArrayList<LinkedList<String>>();
		addToArray(hashArray, defaultCapacity);
		addData(data);

	}

	// ------------------------end of
	// constructors---------------------------------

	// ------------------------help
	// functions---------------------------------------
	private void addToArray(ArrayList<LinkedList<String>> array, int length) {

		for (int i = 0; i < length; i++) {
			array.add(new LinkedList<String>());

		}

	}

	private void addData(String[] data) {

		for (int i = 0; i < data.length; i++) {
			add(data[i]);

		}
	}

	private void reHash() {

		
		String[] data = new String[size];
		int k = 0;
		for (int i = 0; i < previousCapacity; i++) {
			LinkedList<String> linkedList = hashArray.get(i);
			for (int j = 0; j < linkedList.size(); j++) {
				data[k++] = linkedList.get(j);
			}
		}
		hashArray = new ArrayList<LinkedList<String>>();
		for (int i = 0; i < currentCapacity; i++) {
			hashArray.add(new LinkedList<String>());
		}
		for (int i = 0; i < k; i++) {
			if(data[i]==null){
				continue;
			}
			int key = keyCalculate(data[i], currentCapacity);
			hashArray.get(key).add(data[i]);
		}
	}

	// --------------------------end of help
	// functions-----------------------------------

	// -----------------------------------
	public boolean contains(String searchVal) {
		
		if(size==0){
			return false;
			
		}
		int key = keyCalculate(searchVal, currentCapacity);
		if (hashArray.get(key).contains(searchVal)) {
			return true;
		}
		return false;

	}

	// ------------------------------------
	public boolean add(String newVal) {
		if (contains(newVal)) {
			return false;
		}

		int key = keyCalculate(newVal, this.currentCapacity);

		hashArray.get(key).add(newVal);
		this.size++;

		if ((float) size / (float) currentCapacity > upperLoadFactor) {
			previousCapacity = currentCapacity;
			currentCapacity *= 2;
			reHash();
		}
		return true;

	}

	// --------------------------------------

	public boolean delete(String toDelete) {

		if (contains(toDelete)) {
			int key = keyCalculate(toDelete, this.currentCapacity);

			hashArray.get(key).remove(toDelete);
			this.size--;

			if ((float) size / (float) currentCapacity < lowerLoadFactor) {
				previousCapacity = currentCapacity;
				currentCapacity /= 2;
				reHash();
			}
			return true;

		}
		return false;

	}

	// --------------------------------------
	// don't forget this is the length of the hash table (MIT)
	public int size() {
		return size;
	}

	// ---------------------------------------

	// don't forget capacity is the number of elements

	public int capacity() {
		return currentCapacity;
	}

}
