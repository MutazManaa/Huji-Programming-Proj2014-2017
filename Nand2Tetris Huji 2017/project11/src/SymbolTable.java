import java.util.HashMap;
/**creat sympol table for given code*/
public class SymbolTable {

	private HashMap<String,Identifier> classTable;
	private HashMap<String,Identifier> subroutineTable;
	private HashMap<IdentifierKind,Integer> indices = new HashMap<>();
	private String className;
	private String subroutineName;


	/**get class name*/
	SymbolTable(String className){
		classTable = new HashMap<>();
		indices.put(IdentifierKind.STATIC,0);
		indices.put(IdentifierKind.FIELD,0);
		this.className = className;
	}
	/**get subroutine name*/
	void startSubroutine(String subroutineName){
		subroutineTable = new HashMap<>();
		indices.put(IdentifierKind.ARG,0);
		indices.put(IdentifierKind.VAR,0);
		this.subroutineName = subroutineName;
	}

	/**fill the tables with class details and subroutine details*/
	void define(String name, String type, IdentifierKind kind)
	{
		if(kind == IdentifierKind.STATIC || kind == IdentifierKind.FIELD)
			classTable.put(name,new Identifier(name, type, kind, indices.get(kind)));
		else
			subroutineTable.put(name,new Identifier(name, type, kind, indices.get(kind)));

		indices.put(kind,indices.get(kind)+1);
	}

	/**return the class name*/
	public String getClassName() {
		return className;
	}
	/**return the subroutine name*/
	public String getSubroutineName() {
		return subroutineName;
	}

	/**return index of identifier kind*/
	int VarCount(IdentifierKind kind)
	{
//		int count =0;
//		if(kind == IdentifierKind.STATIC || kind == IdentifierKind.FIELD)
//		{
//			for(Identifier identifier: classTable.values())
//				if(identifier.getKind()==kind)
//					count++;
//		} else
//		{
//			for(Identifier identifier: subroutineTable.values())
//				if(identifier.getKind()==kind)
//					count++;
//		}
//		return count;
		return indices.get(kind);
	}

	/**return identifier kind*/
	IdentifierKind KindOf(String name)
	{
		if(classTable.containsKey(name))
			return classTable.get(name).getKind();
		else if(subroutineTable.containsKey(name))
			return subroutineTable.get(name).getKind();

		return IdentifierKind.NONE;
	}
	/**return type of string (class or subroutinm, etc)*/
	String TypeOf(String name)
	{
		if(classTable.containsKey(name))
			return classTable.get(name).getType();
		else if(subroutineTable.containsKey(name))
			return subroutineTable.get(name).getType();

		return "";
	}/**return index of string (class or subroutinm, etc)*/
	int IndexOf(String name)
	{
		if(classTable.containsKey(name))
			return classTable.get(name).getIndex();
		else if(subroutineTable.containsKey(name))
			return subroutineTable.get(name).getIndex();

		return 0;
	}

}
