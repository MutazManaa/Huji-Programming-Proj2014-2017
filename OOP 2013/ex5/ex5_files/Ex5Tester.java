import java.io.*;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import oop.ex5.data_structures.AvlTree;

@RunWith(Parameterized.class)
public class Ex5Tester {
	/**
	 * The location of the test files
	 */
	private static final String TEST_INPUT_DIR = 
			"/cs/course/2013/oop/scripts/ex5/tests"; //You can change this path
			
	private List<String> lines;
	private String fileName;

	/**
	 * A constructor that receives a test file input and reads its lines.
	 * @param fileName
	 */
	public Ex5Tester(String fileName) {
		this.fileName = fileName;
		
		try {
			this.lines = this.readLines(fileName);
		} catch (IOException e) {
			fail("Problem reading "+fileName);
		}
	}

	/**
	 * Read test files from test directory
	 * @return a collection of arrays containing a single string - the current test file
	 */
	@Parameterized.Parameters
	public static Collection<String[]> readTests()  {
		File dir = new File(TEST_INPUT_DIR);
		
		List<String[]> tests = new LinkedList<>();
		
		for (File f : dir.listFiles()) {
			tests.add(new String[] {f.toString()});
		}
		
		return tests;
	}

	
	/**
	 * Read all lines from file
	 * @param f file to read lines from
	 * @throws IOException problem with reading the file lines
	 */
	private List<String> readLines(String f) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		List<String> lines = new LinkedList<>();
		String line = null; 
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}

		return lines;
	}
	
	/**
	 * Run a set of tests on the AvlTree class
	 * @param lines
	 * @throws TestException 
	 */
	@Test
	public void runTests() {
		Iterator<String> linesIterator = this.lines.iterator();

		// First line is the test description
		String description = linesIterator.next();
		
		// Second line is tree creation line.
		String creationLine = linesIterator.next();
		
		// Creating tree using creation line.
		//AvlTree tree = createTree(creationLine);
		AvlTree tree = createTree(creationLine);

		// Line counter (start from 2, since we already read 2 lines).
		int lineNum = 2;
		
		// handle other lines.
		while (linesIterator.hasNext()) {
			handleCommand(linesIterator.next(), ++lineNum, description, tree);
		}
	}
	
	/**
	 * Create AVL tree according to creation line 
	 * @param firstLine
	 * @return
	 */
	private static AvlTree createTree(String firstLine){
		// empty line -- call default constructor.
		if (firstLine.length() == 0) {
			return new AvlTree();
			// otherwise -- data constructor
		} else {
			String[] input = firstLine.split(" ");

			int data[] = new int[input.length];
			for (int i = 0; i < data.length; i++) {
				data[i] = new Integer(input[i]);
			}
			return new AvlTree(data);
		}	
	}
	
private void handleCommand(String line, int lineNum, String description, AvlTree tree) {
		String[] elements = line.split(" ");

		// First element is the command.
		String command = elements[0];
		
		// Second argument is the value (either command argument or expected value).
		int value = 0;
		if(!command.equals("copy"))
			value = new Integer(elements[1]);
		
		String msg = buildMsg(description, lineNum);
		
		switch (command) {
		case "add":
			assertEquals(msg, new Boolean(elements[2]), tree.add(value) );
			break;
		case "delete":
			assertEquals(msg, new Boolean(elements[2]), tree.delete(value));
			break;
		case "size":
			assertEquals(msg, value, tree.size());
			break;
		case "contains":
			assertEquals(msg, new Integer(elements[2]).intValue(), tree.contains(value));
			break;
		case "copy":
			AvlTree tree2 = new AvlTree(tree);
			Iterator<Integer> it1 = tree.iterator();
			Iterator<Integer> it2 = tree2.iterator();
			
			while(it1.hasNext())
				assertEquals(msg, it1.next(), it2.next());
			break;
		case "minNodes":
			assertEquals(msg, new Integer(elements[2]).intValue(), AvlTree.findMinNodes(value));
			
			break;
		default:
			fail("Invaild command "+command);
		}
	}

	

	/**
	 * Build error message
	 * @param description Test description
	 * @param lineNum current line number
	 * @return
	 */
	private String buildMsg(String description, int lineNum) {
		StringBuilder msg = new StringBuilder();

		msg.append(this.fileName);
		msg.append(" (");
		msg.append(description);
		msg.append("): line number ");
		msg.append(lineNum);
		msg.append(": ");
		
		return msg.toString();
	}


}
