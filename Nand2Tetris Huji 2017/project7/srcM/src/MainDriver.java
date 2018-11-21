
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;

/**
 * MainDriver: the main class of the project get sympolic assembley file and parse it to
 * non-sympolic assembley instructions and then output binary text file of translated
 * assembley instructions to binary  representation
 */
public class MainDriver {

	static CodeWriter cdWr = new CodeWriter();
	
	public static void main(String[] args){
	
		if(args.length != 1)
		{
			System.out.println("Bad Input!, Usage: MainDriver file.vm/directory");
		}


		Path file = new File(args[0]).toPath();

		boolean isDirectory = Files.isDirectory(file);   // Check if it's a directory
		boolean isFile =      Files.isRegularFile(file); // Check if it's a regular file
		
		
		if(isFile)
		{	
			//if single file we assume that it is valid one
			String fileName = Paths.get(args[0]).toString().split("\\.")[0];
			handle(fileName,args[0]); 
		}else if(isDirectory)
		{
			  File dir = new File(args[0]);
			  File[] directoryListing = dir.listFiles();
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
			    	
			    	String absPath = child.getAbsolutePath();
			    	if(Paths.get(absPath).toString().split("\\.")[1] == "vm")
			    	{
			    		handle(Paths.get(absPath).toString().split("\\.")[0],absPath);
			    	}
			    }
		}

		
	}	



		cdWr.close();
	}
	
private static void handle(String fileName,String absPath){
			
		
		cdWr.setFileName(fileName);
		Parser parser = new Parser(absPath);// path for VM file


		while (parser.hasMoreCommands())
		{
			parser.advance();
			if (parser.commandType() == CommandType.C_ARITHMETIC)
			{
				cdWr.writeArithmetic(parser.arg1());;

			}
			else if(parser.commandType() == CommandType.C_PUSH || parser.commandType() == CommandType.C_POP)
			{
				cdWr.WritePushPop(parser.commandType(),parser.arg1(),parser.arg2());
			}


		}
		parser.reset();
	}

}
