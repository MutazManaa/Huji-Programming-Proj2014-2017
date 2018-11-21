import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * The class represent the binary code as string of the parser assembley instructions, and contain
 * the A-instructions and C-intructions binary codes
 */
public class CodeWriter {


	private BufferedWriter out = null;


	CodeWriter()
	{

	
	}

	public void setFileName(String fileName){
		try  
		{
			//String tempName = fileName.substring(fileName.lastIndexOf('/')+1, fileName.lastIndexOf('.'));
		    FileWriter fstream = new FileWriter(fileName +".asm", true); //true tells to append data.
		    out = new BufferedWriter(fstream);
		    out.write("@256\n" + "D=A\n" + "@SP\n" + "M=D\n");
		    
		}
		catch (IOException e)
		{
		    System.err.println("Error: " + e.getMessage());
		}

	}

	public void writeArithmetic(String command){
	    
	    try{
	    	switch(command){
            case "add":
            	out.write("@SP\n"+"AM=M-1\n"+"D=M\n"+"A=A-1\n"+"M=M+D\n");
                break;
            case "sub":
            	out.write("@SP\n"+"AM=M-1\n"+"D=M\n"+"A=A-1\n"+"M=M-D\n");

                break;
            case "neg":
            	out.write("@SP\n"+ "M=M-1\n"+"A=M\n"+"M=-M\n"+"@SP\n" +"M=M+1\n");
                break;
            case "eq"://TODO
            	out.write("");
                break;
            case "gt"://TODO
            	out.write("");
                break;
            case "lt"://TODO
            	out.write("");
                break;
            case "and":
            	out.write("@SP\n"+ "M=M-1\n"+"A=M\n"+"D=M\n"+"@SP\n" +"A=M\n"+"D=M&D\n"+
            			"@SP\n"+"A=M\n"+"M=D\n"+"@SP\n"+"M=M+1\n");
                break;
            case "or":
            	out.write("@SP\n"+"M=M-1\n"+"A=M\n"+"D=M\n"+"@SP\n"+"M=M-1\n"+"A=M\n"+"D=M|D\n"
            			+"@SP\n"+"A=M\n"+"M=D\n"+"@SP\n"+"M=M+1\n");

                break;
            case "not":
            	out.write("@SP\n"+"M=M-1\n"+"A=M\n"+"M=!M\n"+"@SP\n"+"M=M+1\n");
                break;
	    	}
	    }
	    catch (IOException e)
        {
        	System.err.println("Error: " + e.getMessage());
        }
	    		
	       	

	}

	public void WritePushPop(CommandType command, String segment,int index){
		if(command == C_PUSH)
		{
			switch(segment)//TODO ALL
			{
				case "static":
					break;
				case "argument":
					break;
				case "local":
					break;
				case "THIS":
					break;
				case "THAT":
					break;
				case "pointer":
					break;
				case "temp":
					break;
				case "constant":
					break;
			}
		}else if(command == C_POP)//TODO ALL
		{
			switch(segment)
			{
				case "static":
					break;
				case "argument":
					break;
				case "local":
					break;
				case "THIS":
					break;
				case "THAT":
					break;
				case "pointer":
					break;
				case "temp":
					break;
				case "constant":
					break;
			}
		}	
	}

	public void close(){
		try{
			if(out != null) {
				out.close();	
			}
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
}

