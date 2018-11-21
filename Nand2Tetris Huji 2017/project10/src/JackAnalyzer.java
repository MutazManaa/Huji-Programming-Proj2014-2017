
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;

/**
 * JackAnalyzer: the main class of the project get vm file and parse it, then translate it
 * to assembley file
 */
public class JackAnalyzer {

	/**
	 * the main function that runs the  program
	 * @param args the source files (file/ directory)
	 */
	public static void main(String[] args) {

		//check args
		if (args.length != 1) {
			System.out.println("Bad Input!, Usage: JackAnalyzer file.jack/directory");
			System.exit(1);
		}
		CompilationEngine engine;

		Path file = new File(args[0]).toPath();

		boolean isDirectory = Files.isDirectory(file);   // Check if it's a directory
		boolean isFile = Files.isRegularFile(file); // Check if it's a regular file

		try {
			//Translator.engine = new CompilationEngine(file);
			if (isFile) {
				//if single file we assume that it is valid one

				//String fileName = file.getFileName().toString().split("\\.")[0];
				engine = new CompilationEngine(file.toString(),file.toString().substring(0, file.toString().lastIndexOf(".jack")));
				engine.CompileClass();
				engine.close();

				//if directory, iterate over all the jack files.
			} else if (isDirectory) {
				File dir = new File(args[0]);
				File[] directoryListing = dir.listFiles();

				if (directoryListing != null) {
					for (File child : directoryListing) {

						String absPath = child.getAbsolutePath();
						if (child.toString().substring(child.toString().lastIndexOf(".") + 1).equals("jack")) {
							engine = new CompilationEngine(absPath,absPath.substring(0,absPath.lastIndexOf(".jack")));
							engine.CompileClass();
							engine.close();
						}
					}
				}



			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}


	}
}



