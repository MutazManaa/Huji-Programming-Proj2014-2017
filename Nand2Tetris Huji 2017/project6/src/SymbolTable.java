
/*Class SymbolTable represent the decimal adress of the registrs used in the sympolic assembley */
import java.util.*;
public class SymbolTable {
	private Map<String, Integer> symbolTable = new HashMap<String,Integer>();

	SymbolTable() {
		symbolTable.put("SP", 0);
		symbolTable.put("LCL", 1);
		symbolTable.put("ARG", 2);
		symbolTable.put("THIS", 3);
		symbolTable.put("THAT", 4);
		symbolTable.put("R0", 0);
		symbolTable.put("R1", 1);
		symbolTable.put("R2", 2);
		symbolTable.put("R3", 3);
		symbolTable.put("R4", 4);
		symbolTable.put("R5", 5);
		symbolTable.put("R6", 6);
		symbolTable.put("R7", 7);
		symbolTable.put("R8", 8);
		symbolTable.put("R9", 9);
		symbolTable.put("R10", 10);
		symbolTable.put("R11", 11);
		symbolTable.put("R12", 12);
		symbolTable.put("R13", 13);
		symbolTable.put("R14", 14);
		symbolTable.put("R15", 15);
		symbolTable.put("SCREEN", 0x4000);
		symbolTable.put("KBD", 0x6000);
	}

	/*return true if the sympolic key is in the sympolic table*/ 
	public boolean contains(String key)
	{
		return symbolTable.containsKey(key);
	}

	/*add pair of sympole and it's decimal adress*/
	public void addEntry(String symbol, int address)
	{
		symbolTable.put(symbol, address);//maybe to check if contains ...
	}

	/*return the decimal adress given it's sympol*/
	public int GetAddress(String symbol)
	{
		if(symbolTable.containsKey(symbol))
		{
			return symbolTable.get(symbol);
		}
		return -1;
	}

}

