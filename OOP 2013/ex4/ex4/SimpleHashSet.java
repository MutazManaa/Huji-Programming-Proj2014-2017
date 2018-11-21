
public abstract class SimpleHashSet  implements SimpleSet {
	
	protected float upperLoadFactor ;
	protected float lowerLoadFactor;
	protected final int defaultCapacity = 16; 
	
	public SimpleHashSet(){
		upperLoadFactor = (float)3/4;
		lowerLoadFactor = (float)1/4;
			
	}
	
	public SimpleHashSet(float upperLoadFactor, float lowerLoadFactor) {
		this.upperLoadFactor = upperLoadFactor;
		this.lowerLoadFactor = lowerLoadFactor;
	}
	
	public abstract boolean add(String newVal);
	
	public abstract boolean contains(String searchVal);
	
	public abstract boolean delete(String toDelete);
	
	public abstract int size();
	
	public abstract int capacity();
	
	
	
//----------------------------------------------------------------
	
	protected int keyCalculate(String value,int capacity){
		
		int key=(value.hashCode()&(capacity-1));
		return key;
		
	}
	
	

}

