
public class CollectionFacadeSet implements SimpleSet{
	java.util.Collection<java.lang.String> collection;
	public CollectionFacadeSet(java.util.Collection<java.lang.String> collection){
		this.collection = collection;
	}
	
    public boolean add(String newVal){
    	return this.collection.add(newVal);  
    }
	
	public boolean contains(String searchVal){
		return this.collection.contains(searchVal);
	}
	
	public boolean delete(String toDelete){
		return this.contains(toDelete);
	}
	
	public int size(){
		return collection.size();
	}
	

	
	

}
