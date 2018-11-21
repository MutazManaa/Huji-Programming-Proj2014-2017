import java.util.*;

/**
 * The class represent the binary code as string of the parser assembley instructions, and contain
 * the A-instructions and C-intructions binary codes
 */
public class Code{


	private Map<String, String> destIns = new HashMap<String,String>();
	private Map<String, String> compCIns = new HashMap<String,String>();
	private Map<String, String> jumpIns = new HashMap<String,String>();
	private final String cInsConst = "1";



	Code()
	{

		destIns.put("","000");
		destIns.put("M","001");
		destIns.put("D","010");
		destIns.put("MD","011");
		destIns.put("A","100");
		destIns.put("AM","101");
		destIns.put("AD","110");
		destIns.put("AMD","111");



		compCIns.put("0","110101010");
		compCIns.put("1","110111111");
		compCIns.put("-1","110111010");
		compCIns.put("D","110001100");
		compCIns.put("A","110110000");
		compCIns.put("!D","110001101");
		compCIns.put("!A","110110001");
		compCIns.put("-D","110001111");
		compCIns.put("-A","110110011");

		compCIns.put("D+1","110011111");
		compCIns.put("A+1","110110111");
		compCIns.put("D-1","110001110");
		compCIns.put("A-1","110110010");
		compCIns.put("D+A","110000010");
		compCIns.put("D-A","110010011");
		compCIns.put("A-D","110000111");
		compCIns.put("D&A","110000000");
		compCIns.put("D&A","110000000");
		compCIns.put("D|A","110010101");

		compCIns.put("M","111110000");
		compCIns.put("!M","111110001");
		compCIns.put("-M","111110011");

		compCIns.put("M+1","111110111");
		compCIns.put("M-1","111110010");
		compCIns.put("D+M","111000010");
		compCIns.put("D-M","111010011");
		compCIns.put("M-D","111000111");
		compCIns.put("D&M","111000000");
		compCIns.put("D|M","111010101");

		compCIns.put("D*A","100000000");
		compCIns.put("A*D","100000000");
		compCIns.put("D*M","101000000");
		compCIns.put("M*D","101000000");

		compCIns.put("D<<","010110000");
		compCIns.put("A<<","010100000");
		compCIns.put("M<<","011100000");
		compCIns.put("D>>","010010000");
		compCIns.put("A>>","010000000");
		compCIns.put("M>>","011000000");



		jumpIns.put("","000");
		jumpIns.put("JGT","001");
		jumpIns.put("JEQ","010");
		jumpIns.put("JGE","011");
		jumpIns.put("JLT","100");
		jumpIns.put("JNE","101");
		jumpIns.put("JLE","110");
		jumpIns.put("JMP","111");

	}

	/**
	 * return 3-bits binary code given mnemonic dest of C-Instruction
	 */
	public int[] dest(String mnemonic)
	{
		return getBits(destIns.get(mnemonic));
	}

	/**
	 * return 7-bit binary code of given mnemonic comp of C-instruction
	 */
	public int[] comp(String mnemonic)
	{
		return getBits(compCIns.get(mnemonic));
	}

	/**
	 * return 3-bit binary code of given mnemonic jump of C-instruction
	 */
	public int[] jump(String mnemonic)
	{
		return getBits(jumpIns.get(mnemonic));
	}
		
	/**
	 * helping method to return bits as int array from string of 0's and 1's
	 */
	public int[] getBits(String str)
	{
		String[] strArray = str.split("");
		int[] bitArray = new int[strArray.length];
		for(int i=0; i<bitArray.length; i++)
		{
   
        	bitArray[i] = Integer.parseInt(strArray[i]);
        }
        	         
   		return bitArray;
    }
	


	/*Helping methods to write the A-instruction and C-instruction to text file*/

	/**
	 * input: decimal adress
	 */
	public String writeAIns(int decimalAdress)
	{
		return String.format("%16s", Integer.toBinaryString(decimalAdress)).replace(' ', '0') + "\n";
	}

	
	public String writeCIns(String dest,String comp,String jumIns)
	{
		return cInsConst +  compCIns.get(comp) + destIns.get(dest) + jumpIns.get(jumIns) + "\n";
	}




}