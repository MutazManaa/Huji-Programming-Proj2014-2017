import javax.swing.text.Segment;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The class wirte to output file the translation of jack code to vm instrructions.
 */
class VMWriter {


	public static final String CONST = "constant";
	public static final String ARG = "argument";
	public static final String LOCAL = "local";
	public static final String STATIC = "static";
	public static final String THIS = "this";
	public static final String THAT = "that";
	public static final String POINTER = "pointer";
	public static final String TEMP = "temp";

    private BufferedWriter out = null;
//    private int labelNo;
//    private String fileName;
	private static final String SPACE = " ";
	private static final String ENTER = "\n";
	private static final String PUSH = "push";
	private static final String POP = "pop";
	private static final String LABEL = "label";
	private static final String GOTO = "goto";
	private static final String IF_GOTO = "if-goto";
	private static final String CALL = "call";
	private static final String FUNCTION = "function";
	private static final String RETURN = "return";
	/**
	 * initializes the buffered writer according to the given fileName
	 * @param outFileName the fil  translating to.
	 */
    VMWriter(String outFileName) {
        int labelNo = 0;
        try {
            //String tempName = outFileName.substring(outFileName.lastIndexOf('/')+1, outFileName.lastIndexOf('.'));
            FileWriter fstream = new FileWriter(outFileName + ".vm", false); //true tells to append data.
            out = new BufferedWriter(fstream);
           

            out.flush();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
    
//    /**setting the output file name*/
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }

	/**Writes a VM push command */
	public void writePush(String segment, int index) throws IOException{
			
		out.write(PUSH+SPACE+segment+SPACE+Integer.toString(index)+ENTER);
	}
	
	/**Writes a VM pop command*/
	public void writePop(String segment, int index) throws IOException{
		out.write(POP+SPACE+segment+SPACE+Integer.toString(index)+ENTER);
	}
	
	/**Writes a VM arithmetic command */
	public void writeArithmetic(VMArithmetic command) throws IOException{
		if(command == VMArithmetic.MULT)
			writeCall("Math.multiply",2);
		else if(command == VMArithmetic.DIVIDE)
			writeCall("Math.divide",2);
		else
			out.write(command.toString().toLowerCase() +ENTER);
	}
	
	/**Writes a VM label command */
	public void writelabel(String label) throws IOException
	{
		out.write(LABEL+SPACE+label+ENTER);
	}
	
	/**Writes a VM go-to command */
	public void  writeGoto(String label) throws IOException{
		out.write(GOTO+SPACE+label+ENTER);
	}
	
	/**Writes a VM if command */
	public void writeIf(String label) throws IOException{
		out.write(IF_GOTO+SPACE+label+ENTER);
	}
	
	/**Writes a VM call command*/
	public void writeCall(String name, int nArgs) throws IOException{
		out.write(CALL+SPACE+name+SPACE+Integer.toString(nArgs)+ENTER);
	}
	
	/**Writes a VM function command */
	public void writeFunction(String name, int nLocals) throws IOException{
		out.write(FUNCTION+SPACE+name+SPACE+Integer.toString(nLocals)+ENTER);
	}
	
	/**Writes a VM return command */
	public void writeReturn() throws IOException{
		out.write(RETURN+SPACE+ENTER);
	
	}

    
    /**close the output file*/
    public void close() {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

