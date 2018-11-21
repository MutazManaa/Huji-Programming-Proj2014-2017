/**class represnt identifier properties(name, type,kind and undex*/
public class Identifier {

	private String name;
	private String type;
	private IdentifierKind kind;
	private int index;

	/**construc identifier*/
	Identifier(String name, String type, IdentifierKind kind, int index)
	{
		this.name = name;
		this.type = type;
		this.kind = kind;
		this.index = index;
	}

//	public String getName() {
//		return name;
//	}
	
	/** @return identifier type */
	public String getType() {
		return type;
	}

	/** @return identifier identifeir kind */
	public IdentifierKind getKind() {
		return kind;
	}

	/** @return identifier index */
	public int getIndex() {
		return index;
	}
}
