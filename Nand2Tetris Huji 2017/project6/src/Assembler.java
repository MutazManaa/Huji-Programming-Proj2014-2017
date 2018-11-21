

import java.nio.file.Paths;
import java.io.*;

/**
 * Assembler: the main class of the project get sympolic assembley file and parse it to
 * non-sympolic assembley instructions and then output binary text file of translated
 * assembley instructions to binary  representation
 */
public class Assembler {


	
	public static void main(String[] args){
	
		if(args.length != 1)
		{
			System.out.println("Bad Input!, Usage: Assembler file.asm");
		}


		String fileName = Paths.get(args[0]).toString().split("\\.")[0];
		
		//Initalization: creat the sympole table and the parser
		SymbolTable symTable = new SymbolTable();
		int preAllocRAMSympole = 16;
		Parser parser = new Parser(args[0]);// path for assembley file
			
		//first Pass: go over the sympolic assembley file and add new sympols 
		int current_address = 0;
		while (parser.hasMoreCommands())
		{
			parser.advance();
			if (parser.commandType() == CommandType.L_COMMAND)
			{
				symTable.addEntry(parser.symbol(),current_address);
				
			}
			else if(parser.commandType() == CommandType.A_COMMAND || parser.commandType() == CommandType.C_COMMAND)
			{
				current_address++;
			}


		}


		//second pass: go over the non-sympolic file and ouput to the prog.hack the binary representation
		//from prog.asm 

		parser.reset();

		Code code = new Code();
		BufferedWriter out = null;
		try  
		{		
	    	FileWriter fstream = new FileWriter(fileName + ".hack");
    		out = new BufferedWriter(fstream);
    		


			while(parser.hasMoreCommands())
			{
				parser.advance();
				if (parser.commandType()== CommandType.L_COMMAND)
				{
					continue;

				}
				else if(parser.commandType() ==CommandType.A_COMMAND)
				{

					if(isNumeric(parser.symbol()))
					{
						out.write(code.writeAIns(new Integer(parser.symbol())));
					}
					else if(symTable.contains(parser.symbol()))
					{
						out.write(code.writeAIns(symTable.GetAddress(parser.symbol())));
					}
					else
					{
						symTable.addEntry(parser.symbol(),preAllocRAMSympole);
						preAllocRAMSympole++;
						out.write(code.writeAIns(symTable.GetAddress(parser.symbol())));
					}

				}
				else if(parser.commandType() == CommandType.C_COMMAND)
				{
					out.write(code.writeCIns(parser.dest(), parser.comp(), parser.jump()));
				}

			}
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}


		finally
		{
			try {
				if (out != null) {
					out.close();
				}
			}
			catch(Exception e)
				{

				}

		}

	}

	/**
	 * helper function to check of a string is numeric
	 */
	public static boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}
}





