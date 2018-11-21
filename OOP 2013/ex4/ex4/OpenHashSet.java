public class OpenHashSet extends SimpleHashSet {

	private int capacity, updatedCapacity, previousCapacity;
	private String[] elements;
	private int[] occupiedhCells;
	private int key;
	float newKey;
	private int size;
	private final float c1 = (float) 1 / 2, c2 = (float) 1 / 2;

	// ------------------------constructors
	public OpenHashSet() {
		super((float) 3 / 4, (float) 1 / 4);
		capacity = defaultCapacity;
		elements = new String[capacity];
		occupiedhCells = new int[capacity];
	}

	public OpenHashSet(float upperLoadFactor, float lowerLoadFactor) {
		super(upperLoadFactor, lowerLoadFactor);
		capacity = defaultCapacity;
		elements = new String[capacity];
		occupiedhCells = new int[capacity];

		resetOccupiedCells();

	}

	OpenHashSet(String[] data) {
		super((float) 3 / 4, (float) 1 / 4);
		capacity = defaultCapacity;
		elements = new String[capacity];
		occupiedhCells = new int[capacity];
		this.size = 0;
		addElements(data);
	}

	// -------------------------------------help methods-------------
	private void resetOccupiedCells() {
		for (int i = 0; i < capacity; i++) {
			occupiedhCells[i] = 0;

		}
	}

	private void addElements(String[] data) {
		for (int i = 0; i < data.length; i++) {
			key = keyCalculate(data[i], capacity);
			elements[key] = data[i];
			size++;

		}

	}

	private int newKeyCalculate(String Value) {
		int i = 1;
		newKey = (key + c1 * i + c2 * i * i)%capacity;
		while (occupiedhCells[(int) newKey] != 0 && (int) newKey < capacity) {
			i++;
			newKey = (newKey + c1 * i + c2 * i * i)%capacity;

		}
		return (int) newKey;

	}

	private void rehash(){
		
		String[] tempString = new String[previousCapacity];
		for (int i=0;i<previousCapacity;i++){
			if(occupiedhCells[i]==0){
			tempString[i]=elements[i];
			}else{
				tempString[i]=null;
			}
		}
			
		
		elements = new String[capacity];
		occupiedhCells = new int[capacity];
		resetOccupiedCells();
		
		for(int j=0; j< tempString.length;j++){
			if(tempString[j]== null){
				continue;
			}
			key = keyCalculate(tempString[j],capacity);
			if (occupiedhCells[key] == 1) {
				elements[key]=tempString[j];
				occupiedhCells[j]=0;
				
				
			}
			else{
				int i = 1;
				newKey = (key + c1 * i + c2 * i * i)%capacity;
				while (occupiedhCells[(int) newKey] == 0) {
					i++;
					newKey = (newKey + c1 * i + c2 * i * i)%capacity;
				}
				
				elements[(int)newKey]=tempString[j];
				occupiedhCells[j]=0;
					
				
			}
		
			
			
				
			}
		
		}
			
		
		
	    
		// -----------------------------------------------------------------------

	public boolean add(String newVal) {

		if (contains(newVal)) {
			return false;

		}

		key = keyCalculate(newVal, capacity);
		if (occupiedhCells[key] == 0) {
			elements[key] = newVal;
		} else {
			elements[(int) newKeyCalculate(newVal)] = newVal;
		}
		size++;

		if ((double) size / (double) (capacity) >= upperLoadFactor) {

			updatedCapacity = capacity * 2;
			previousCapacity = capacity;
			capacity = updatedCapacity;

			rehash();

		}

		return true;
	}

	public boolean contains(String searchVal) {
		if (size == 0) {
			return false;

		}

		if (searchVal == null) {
			return false;
		}
		key = keyCalculate(searchVal, capacity);
		if (elements[key] == searchVal) {
			return true;
		}
		int i = 1;
		newKey = (key + c1 * i + c2 * i * i)%capacity;
		while ((int) newKey < capacity && occupiedhCells[(int) newKey] == 1) {
			if (elements[(int) newKey] == searchVal) {
				return true;
			} else {
				i++;
				newKey =( newKey + c1 * i + c2 * i * i)%capacity;
				

			}

		}

		return false;

	}

	public boolean delete(String toDelete) {
		if (!contains(toDelete)) {
			return false;

		}

		if (elements[keyCalculate(toDelete, capacity)] == toDelete) {
			occupiedhCells[keyCalculate(toDelete, capacity)] = 1;
			size--;

		} else {

			int i = 1;
			newKey = (key + c1 * i + c2 * i * i)%capacity;
			while (elements[(int) newKey] != toDelete) {

				i++;
				newKey =  (newKey + c1 * i + c2 * i * i)%capacity;

			}

			occupiedhCells[(int) newKey] = 1;
			size--;

		}

		if ((double) size / (double) capacity <= lowerLoadFactor) {

			updatedCapacity = capacity / 2;
			previousCapacity = capacity;
			capacity = updatedCapacity;

			rehash();
		}

		return true;

	}

	public int size() {
		return size;

	}

	public int capacity() {
		return capacity;
	}


}
