import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The class wirte to output file the translation of VM instrruction.
 */
class CodeWriter {


    private BufferedWriter out = null;
    private int labelNo;
    private String fileName;

	/**
	 * initializes the buffered writer according to the given fileName
	 * @param outFileName the fil  translating to.
	 */
    CodeWriter(String outFileName) {
        labelNo = 0;
        try {
            //String tempName = outFileName.substring(outFileName.lastIndexOf('/')+1, outFileName.lastIndexOf('.'));
            FileWriter fstream = new FileWriter(outFileName + ".asm", false); //true tells to append data.
            out = new BufferedWriter(fstream);
            //initalizing the stack pointer
            out.write("@256\n" +
                    "D=A\n" +
                    "@SP\n" +
                    "M=D\n");
            out.flush();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
    
    /**setting the output file name*/
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * get Arithmetic VM command and write it's translation to
     * assembley version to the output file
     */
    public void writeArithmetic(String command) {
        String jmp = "";
        String op = "";
        try {
            switch (command) {

                case "add":
                    op = "+";
                case "sub":
                    if (op.equals("")) op = "-";
                case "and":
                    if (op.equals("")) op = "&";
                case "or":
                    if (op.equals("")) op = "|";

                    out.write("@SP\n" +
                            "AM=M-1\n" +
                            "D=M\n" +
                            "A=A-1\n" +
                            "M=M" + op + "D\n");
                    break;


                case "not":
                    op = "!";
                case "neg":
                    if (op.equals("")) op = "-";

                    out.write("@SP\n" +
                            "A=M-1\n" +
                            "M=" + op + "M\n");
                    break;


                case "eq":
                    compare("JEQ");
                    break;
                case "gt":
                    compare("JGT");
                    break;
                case "lt":
                    compare("JLT");
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }


    }

    /**
     * a function that writes the compare arithmetic
     * @param jmp the type of the comparison
     * @throws IOException if there is an error
     */
    private void compare(String jmp) throws IOException
    {
        //save a and b to ram (variables)
        out.write("@SP\n"+
                "A=M-1\n"+
                "D=M\n"+
                "@b\n"+
                "M=D\n"+
                "@SP\n"+
                "A=M-1\n"+
                "A=A-1\n"+
                "D=M\n"+
                "@a\n"+
                "M=D\n");

        //calculate amod and bmod
        out.write("@a\n" +
                "D=M>>\n" +
                "@ashift\n" +
                "M=D\n" +
                "M=M<<\n" +
                "D=M\n" +
                "@a\n" +
                "D=M-D\n" +
                "@amod\n" +
                "M=D\n" +
                "@b\n" +
                "D=M>>\n"+
                "@bshift\n" +
                "M=D\n" +
                "MD=M<<\n" +
                "@b\n" +
                "D=M-D\n" +
                "@bmod\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M-1\n" +
                "M=M-1\n");

        //push amod-bmod
        out.write("@amod\n" +
                "D=M\n" +
                "@bmod\n" +
                "D=D-M\n");
        pushD();

        //push a/2 -b/2
        out.write("@a\n" +
                "M=M>>\n" +
                "D=M\n" +
                "@b\n" +
                "M=M>>\n" +
                "D=D-M\n");
        pushD();

        //compare a/2, b/2
        out.write("@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@END"+labelNo +"\n" +
                "D;JEQ\n" +
                "@IF" + labelNo + "\n" +
                "D;" + jmp + "\n" +

                "@0\n" +
                "D=A\n" +
                "@SP\n" +
                //check
                "A=M-1\n" +
                "M=D\n" +
                "@ENDIF"+labelNo+"\n"+
                "0;JMP\n"+

                "(IF" + labelNo + ")\n" +
                "D=-1\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=D\n" +

                "(ENDIF" + labelNo + ")\n" +
                "@END"+(labelNo+1)+"\n" +
                "0;JMP\n"+
                "(END"+labelNo+")\n");

        labelNo++;

        //compare amod, bmod
        out.write("@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@IF" + labelNo + "\n" +
                "D;" + jmp + "\n" +

                "@0\n" +
                "D=A\n" +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@ENDIF"+labelNo+"\n"+
                "0;JMP\n"+

                "(IF" + labelNo + ")\n" +
                "D=-1\n" +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +

                "(ENDIF" + labelNo + ")\n" +
                "@SP\n" +
                "M=M+1\n" +
                "(END"+labelNo+")\n");
        labelNo++;
    }


    /**
     * helper function to reduce duplicate code
     * writes the code that pushes the value in D to the stack
     */
    private void pushD () throws IOException
    {
        out.write("@SP\n"+
                "M=M+1\n"+
                "A=M-1\n"+
                "M=D\n");
    }

    /**
     * helper function to reduce duplicate code
     * writes the code that pops the value the top of the stack to D
     */
    private void popD()  throws IOException
    {
        out.write("@SP\n"+
                "M=M-1\n"+
                "A=M\n"+
                "D=M\n");
    }

    /**get push or pop VM command and write it's translation to
    assembley version to the output file*/
    public void WritePushPop(CommandType command, String segment, int index) {
        String base="";
        try {
            if (command == CommandType.C_POP) {
                switch (segment)
                {
                    case "static":
                        popD();
                        out.write("@"+fileName+"."+ index+"\n"+
                                "M=D\n");
                        break;

                    case "argument":
                        base = "ARG";
                    case "local":
                        if(base.equals("")) base = "LCL";
                    case "this":
                        if(base.equals("")) base = "THIS";
                    case "that":
                        if(base.equals("")) base = "THAT";

                        out.write("@"+base+"\n"+
                                "D=M\n"+
                                "@"+index+"\n"+
                                "D=D+A\n"+
                                "@address\n"+
                                "M=D\n");
                        popD();
                        out.write("@address\n"+
                                "A=M\n"+
                                "M=D\n");
                        break;


                    case "pointer":
                        base= "THIS";
                    case "temp":
                        if(base.equals("")) base ="R5";

                        out.write("@"+base+"\n"+
                                "D=A\n"+
                                "@"+index+"\n"+
                                "D=D+A\n"+
                                "@address\n"+
                                "M=D\n");
                        popD();
                        out.write("@address\n"+
                                "A=M\n"+
                                "M=D\n");
                        break;
                }
            } else if (command == CommandType.C_PUSH)
            {
                switch (segment) {
                    case "static":
                        out.write("@"+fileName+"."+ index+"\n"+
                                "D=M\n");
                        pushD();
                        break;

                    case "argument":
                        base = "ARG";
                    case "local":
                        if(base.equals("")) base = "LCL";
                    case "this":
                        if(base.equals("")) base = "THIS";
                    case "that":
                        if(base.equals("")) base = "THAT";

                        out.write("@"+base+"\n"+
                                "D=M\n"+
                                "@"+index+"\n"+
                                "A=D+A\n"+
                                "D=M\n");
                        pushD();
                        break;


                    case "pointer":
                        base= "THIS";
                    case "temp":
                        if(base.equals("")) base ="R5";

                        out.write("@"+base+"\n"+
                                "D=A\n"+
                                "@"+index+"\n"+
                                "A=D+A\n"+
                                "D=M\n");
                        pushD();
                        break;


                    case "constant":
                        out.write("@"+index+"\n"+
                                "D=A\n");
                        pushD();
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
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

