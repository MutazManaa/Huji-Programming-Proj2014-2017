import java.io.*;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import oop.ex6.filescript.MyFileScript;

@RunWith(Parameterized.class)
public class Ex6Tester {
	/**
	 * The location of the test files
	 * TODO change to oop home dir
	 */
	private static final String TEST_INPUT_DIR = "/cs/course/2013/oop/scripts/ex6/tests/"; //Change path!
	//private static final String TEST_INPUT_DIR = "..\\tests\\";
	private static String TEST_TESTERS = "command_line_tests.txt";
	
	//private static final String TEST_OUTPUT_DIR = "..\\school_outputs\\";
	private static final String TEST_OUTPUT_DIR = "/cs/course/2013/oop/scripts/ex6/school_outputs/"; //Change path!

	private String sourceDir;
	private String commandFile;
	private String numOfTest;
	/**
	 * A constructor that receives a test file input and reads its lines to get the  description, source directory and the command file.
	 * @param fileName
	 * @throws FileNotFoundException 
	 */
	public Ex6Tester(String fileName) throws FileNotFoundException {
		
		String[] parts = fileName.split(" ");
		numOfTest = parts[0].substring(0,3);
		sourceDir = TEST_INPUT_DIR+"/"+parts[3];
		commandFile = TEST_INPUT_DIR+parts[4];
		
		File file = new File("outputs"); //creates directory named outputs in order to see your solutions.
		file.mkdir();
	}

	/**
	 * Read test files from test directory
	 * @return a collection of arrays containing a single string - the current test file
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	@Parameterized.Parameters
	public static Collection<String[]> readTests() throws IOException  {
		List<String[]> tests = new LinkedList<>();
		
		FileReader fileReader = null;
		BufferedReader br = null;
		
		try {
			fileReader = new FileReader(new File(TEST_INPUT_DIR+TEST_TESTERS));
			br = new BufferedReader(fileReader);			
		} catch (IOException e) {
				fail("Problem reading "+TEST_INPUT_DIR+TEST_TESTERS);
		}
		 
		 String line = null;
		 // if no more lines the readLine() returns null
		 while ((line = br.readLine()) != null) {
			 tests.add(new String[] {line.toString()});
		 }
		return tests;
	}

	/**
	 * Run the main class of MyFileScript with different args input (source directory and command file).
	 * @param 
	 * @throws IOException 
	 */
	@Test
	public void runTests() throws IOException {
		
	PrintStream default_out = System.out;
		PrintStream default_err = System.err;
		
		File file = new File("outputs/"+numOfTest+".txt");
		 
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		
		System.setErr(ps);		
		System.setOut(ps);		
			
		String[] args = {this.sourceDir,this.commandFile};	
		MyFileScript.main(args);
		ps.close();
		
		System.setOut(default_out);	
		System.setErr(default_err);	
		
		compareTextFiles();		
	}

	private void compareTextFiles() throws IOException {
		
	Integer numLine = 0;
        File student = new File("outputs/"+numOfTest+".txt");// Student output
        File school = new File(TEST_OUTPUT_DIR+numOfTest+".txt");// School solution output

        FileReader fR_student = new FileReader(student);
        FileReader fR_school = new FileReader(school);

        BufferedReader reader1 = new BufferedReader(fR_student);
        BufferedReader reader2 = new BufferedReader(fR_school);

        String line1 = "";
        String line2 = "";
        while (line2 != null && line1 != null)
        {
        	line1 = reader1.readLine();
        	line2 = reader2.readLine();
    		numLine++;
            assertEquals("problem in test number:"+numOfTest+", line number:"+numLine,line2,line1);
            
        }
        reader1.close();
        reader2.close();
	}

}
