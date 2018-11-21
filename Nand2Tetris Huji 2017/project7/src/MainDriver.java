
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;

/**
 * MainDriver: the main class of the project get vm file and parse it, then translate it 
 * to assembley file
 */
public class MainDriver {

	private CodeWriter cdWr;
	
	public static void main(String[] args){
		//check args
		if(args.length != 1)
		{
			System.out.println("Bad Input!, Usage: MainDriver file.vm/directory");
		}
		MainDriver Translator = new MainDriver();

		Path file = new File(args[0]).toPath();

		boolean isDirectory = Files.isDirectory(file);   // Check if it's a directory
		boolean isFile =      Files.isRegularFile(file); // Check if it's a regular file

		//Translator.cdWr = new CodeWriter(file);
		if(isFile)
		{	
			//if single file we assume that it is valid one

			String fileName = file.getFileName().toString().split("\\.")[0];
            Translator.cdWr = new CodeWriter(file.toString().substring(0,file.toString().lastIndexOf(".")));
			Translator.handle(fileName,args[0]);
			
			//if directory, iterate over all the vm files.
		}else if(isDirectory)
		{
			  File dir = new File(args[0]);
			  File[] directoryListing = dir.listFiles();
			  Translator.cdWr = new CodeWriter(dir.toString()+"/"+dir.getName());
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
			    	
			    	String absPath = child.getAbsolutePath();
			    	if(child.toString().substring(child.toString().lastIndexOf(".")+1).equals("vm"))
			    	{
			    		Translator.handle(child.getName().split("\\.")[0],absPath);
			    	}
			    }
		}

		
	}	




		Translator.cdWr.close();
	}
	
private  void handle(String fileName,String absPath){
			
		
		cdWr.setFileName(fileName);
		Parser parser = new Parser(absPath);// path for VM file


		while (parser.hasMoreCommands())
		{
			parser.advance();
			if (parser.commandType() == CommandType.C_ARITHMETIC)
			{
				cdWr.writeArithmetic(parser.arg1());;

			}
			else if(parser.commandType() == CommandType.C_PUSH || parser.commandType() == CommandType.C_POP){
				cdWr.WritePushPop(parser.commandType(),parser.arg1(),parser.arg2());
			}


		}
		parser.reset();
	}

}
