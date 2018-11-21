import java.io.*;
import java.util.Currency;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompilationEngineh {
	
	JackTokenizer br;
	vmWriter vm;
	SymbolTable table;
	String classname;
	private final static String statments="(let|if|while|do|return)";
	public static final String keyWord_pattern = "(class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)";
	public static final String symbol_pattern = "(.*)(\\{|\\}|\\(|\\)|\\[|\\]|\\.|\\,|;|\\+|-|\\*|/|&|\\||<|>|=|~)(.*)"; 
	private static String identifier_pattern = "^[^\\d\\W]\\w*\\Z";
	private static String int_pattern = "\\d+";
	private static String boolean_pattern = "(true|false|null|this)";
	int ifcounter = 0;
	int whilecounter = 0;
	
	public CompilationEngineh(String path) throws FileNotFoundException
	{
		br = new JackTokenizer(path);
		vm = new vmWriter(path);
		table = new SymbolTable();
	}
	
	public void compileClass() throws IOException{
		br.advance();//class
		br.advance();//class name
		classname = br.currWord;
		br.advance();// { 
		
		//CLASS VAR DEC : take care of the feilds and static if they are exist
		br.advance();//constructir/function/static/feild/method
		while(br.get_Current_word().equals("field") || br.get_Current_word().equals("static"))
		{
			compileClassVarDec();
			br.advance();// or } or constructor or function or method
		}
		
		while(br.get_Current_word().equals("constructor") || br.get_Current_word().equals("function") ||
				br.get_Current_word().equals("method"))
		{
			compileSubroutine();
			br.advance();// }  of the class or method constructir function
		}
	
		
		vm.close();
		
		
	}
	
	public void compileClassVarDec() throws IOException{
		String kind = br.currWord;//static or field
		br.advance();//type
		String type = br.currWord;
		br.advance();//var name
		String varName = br.currWord;
		table.define(varName, type,kind);//inseret to table
		
		br.advance();//or , or ;
		while(br.currWord.equals(",")){
			br.advance();//var name
			varName = br.currWord;
			table.define(varName, type,kind);
			br.advance();//; or var name
		}
		// ; 	
	}
	
	public void compileSubroutine() throws IOException{
		String subroutine_kind = br.currWord;
		table.startSubroutine();
		if(subroutine_kind.equals("method")){
			table.argument_index++;
		}
		String type;
		String name;
		br.advance();//void or type of func
		br.advance();//name subroutine (function or method name)
		String subroutine_name = br.currWord;
		br.advance();//  function open  ( 
		br.advance();// ) or somthing inside the ( ) | type
		if(br.currWord.equals(")")){
		}else{
			compileParameterList();
		}// end )
		
		br.advance();// { 
		//subroutine body {
		br.advance();// var ,while, let, do
		while(br.currWord.equals("var")){
			br.advance();//type
			type = br.currWord;
			br.advance();//var name
			name = br.currWord;
			table.define(name, type, "var");
			br.advance();//, or ;
			while(br.currWord.equals(",")){
				br.advance();//var name
				name = br.currWord;
				table.define(name, type, "var");
				br.advance();
			}
			br.advance();//another var or func, method ....
		}
		String modifed_name = classname+"."+subroutine_name;
		vm.writeFunction(modifed_name, table.varCount("var"));
		
		if(subroutine_kind.equals("constructor")){
			vm.writePush("constant", table.field_index);
			vm.writeCall("Memory.alloc", 1);
			vm.writePop("pointer", 0);
		}else if(subroutine_kind.equals("method")){
			vm.writePush("argument", 0);
			vm.writePop("pointer", 0);	
	        }

		//statments
		if(!br.currWord.equals("}")){//check if there is no statments
			compileStatements();// we enter and there is a word (method, fun..)
			// } of the sub body  , statment return
			// }
		}else{
			// write the }
		}
	}
	
	public void compileStatements() throws IOException{
		while(br.currWord.matches(statments)){
			if(br.currWord.equals("if")){
				compileIf();// if alwas one step out
			}
			else if(br.currWord.equals("while")){
				compileWhile();
				br.advance();
				
			}else if(br.currWord.equals("do")){
				CompileDo();
				br.advance();
				
			}else if(br.currWord.equals("return")){
				compileReturn();
				br.advance();
				
			}else if(br.currWord.equals("let")){
				compileLet();
				br.advance();
			}
		}
	}

	public void compileWhile() throws IOException{
		whilecounter++;
		int local_count = whilecounter;
		vm.writelabel("l1_label_while"+local_count+"\n");
		br.advance();//( of while
		br.advance();//in exp
		CompileExpression();
		br.advance();// ) of while
		vm.writeArithmetic("not\n");
		vm.writeIf("l2_label_while"+local_count+"\n");
		
		br.advance();//{ of while
		br.advance();// inside state or }
		if(!br.currWord.equals("}")){
			compileStatements();// curr }
		}else{
		}
		
		vm.writeGoto("l1_label_while"+local_count+"\n");
		vm.writelabel("l2_label_while"+local_count+"\n");
		
	}
	
	public void compileReturn() throws IOException{
	
		br.advance();//  ; or inside exp
		if(!br.currWord.equals(";"))
		{
			CompileExpression();
			br.advance();// ;
		}else{// void : push constant 0
			vm.writePush("constant", 0);
		}
		
		vm.writeReturn();
	}
	
	public void CompileDo() throws IOException{
		//do
		br.advance();// inside subroutine
		subRoutineCall();
		br.advance();//;
		vm.writePop("temp", 0);
		
	}
	public void compileIf() throws IOException{
		// there is if
		ifcounter++;
		int local_count = ifcounter;
		br.advance();// ( of the if
		br.advance();//first word in expression 
		CompileExpression();
		br.advance();// )  of the if
		vm.writeArithmetic("not\n");
		vm.writeIf("l1_labelif_"+local_count+"\n");// if-goto l1
		br.advance();// {
		br.advance();//first in stat
		if(!br.currWord.equals("}")){
			compileStatements();
			//statment always get out one more step so the current line is , }
			
		}else{
		}
		
		vm.writeGoto("l2_labelif_"+local_count+"\n");
		vm.writelabel("l1_labelif_"+local_count+"\n");	
		
		br.advance();// else or out if
		if(br.currWord.equals("else")){
			br.advance();//{ of the else
			br.advance();//or } or statment
			if(!br.currWord.equals("}")){
				compileStatements();// the curr line }
			}else{
			}
			
			br.advance();//out if
		}
		
		vm.writelabel("l2_labelif_" + local_count+"\n");
	}
	
	
	public void compileLet() throws IOException{
		br.advance();// var name
		String varname = br.currWord;
		boolean there = false;
		
		br.advance();//or [ or =
		if(br.currWord.equals("[")){
			there = true;
			vm.writePush(table.kindOf(varname), table.indexOf(varname));
			
			br.advance();//enter exp
			CompileExpression();
			br.advance();// ]
			br.advance();// =
			
			vm.writeArithmetic("add");
		}
		br.advance();//enter exp
		CompileExpression();
		br.advance();// ;
		if(there){
			vm.writePop("temp", 0);
			vm.writePop("pointer", 1);
			vm.writePush("temp", 0);
			vm.writePop("that", 0);
		}else{
			vm.writePop(table.kindOf(varname),table.indexOf(varname));
		}
	}
	
	public void CompileExpression() throws IOException{
		compileTerm();
		//op term
		String next;
		if(br.pos==0 && br.parts.length>1)
			next = br.parts[br.pos+1];
		else
			next = br.parts[br.pos];
		//System.out.println(next);
		while(next.equals("+")||next.equals("-")||next.equals("*")||next.equals("/")||
				next.equals("&")||next.equals("|")||next.equals("<")||next.equals(">")||next.equals("=")){
			br.advance();// on of the op
			String symbol = br.currWord;
			br.advance();
			compileTerm();
			
			compileOp(symbol);
			next = br.parts[br.pos];
		}
	}
	
	public void compileTerm() throws IOException{
		// there is a word
		if(br.currWord.matches(int_pattern)){
			vm.writePush("constant",Integer.parseInt(br.currWord));
		}
		else if(br.currWord.matches(boolean_pattern)){
			if(br.currentLine.equals("true")){
				vm.writePush("constant", 1);
				vm.writeArithmetic("neg");
				
			}else if(br.currWord.equals("null") || br.currWord.equals("false")){
				vm.writePush("constant", 0);
			}
			else if(br.currWord.equals("this")){
				
				vm.writePush("pointer", 0 );
			}else if(br.currWord.equals("true")){
				vm.writePush("constant", 1);
				vm.writeArithmetic("neg");
			}
		}
		else if(br.currWord.equals("\"")){
			br.advance();
			String theString="";
			while(!br.currWord.equals("\"")){
				if(br.currWord.equals("_")){
					theString +="\t";
				}
				if(br.currWord.equals("`")){
					theString +=" ";
				}else{
				theString += br.currWord;}
				br.advance();
			}
			
			int lengthofString = theString.length();
			vm.writePush("constant", lengthofString);
			
			vm.writeCall("String.new", 1);
			
			int i;
			for(i = 0;i<lengthofString; i++){
				int charnum_at_acci = (int)theString.charAt(i);
				vm.writePush("constant", charnum_at_acci);
				vm.writeCall("String.appendChar", 2);
			}
		}
		
		else if(br.currWord.matches(identifier_pattern)){
			String next = br.parts[br.pos];// [ or 
			if(next.equals("[")){ 
				vm.writePush(table.kindOf(br.currWord), table.indexOf(br.currWord));
				br.advance();//[
				br.advance();//first thing in exp
				CompileExpression();// expression
				br.advance();//]
				vm.writeArithmetic("add");
				vm.writePop("pointer", 1);
				vm.writePush("that", 0);
				
			}else if(next.equals("(") ||next.equals("."))
				subRoutineCall();
			else{
				// if not of the above then its normal
				String kindofIdentifer = table.kindOf(br.currWord);
				int index = table.indexOf(br.currWord);
				
				vm.writePush(kindofIdentifer, index);
			}
			
		}else if(br.currWord.equals("-") || br.currWord.equals("~")){
			String current = br.currWord;
			br.advance();// first thing in term
			compileTerm();
			if(current.equals("~"))
				vm.writeArithmetic("not");
			else
				vm.writeArithmetic("neg");
			
		}else if(br.currWord.equals("("))
		{
			//write (
			br.advance();// first thing in exp
			CompileExpression();
			br.advance();// ) of the expression
		}
		
	}
	public void compileOp(String symbol) throws IOException{
		if(symbol.equals("&")){
			vm.writeArithmetic("and");
		}else if(symbol.equals("<")){
			vm.writeArithmetic("lt");
		}else if(symbol.equals(">")){
			vm.writeArithmetic("gt");
		}else if(symbol.equals("+")){
			vm.writeArithmetic("add");
		}else if(symbol.equals("-")){
			vm.writeArithmetic("sub");
		}else if(symbol.equals("*")){
			vm.writeCall("Math.multiply", 2);
		}else if(symbol.equals("/")){
			vm.writeCall("Math.divide",2);
		}else if(symbol.equals("=")){
			vm.writeArithmetic("eq");
		}else if(symbol.equals("|")){
			vm.writeArithmetic("or");
		}
	}
	public void subRoutineCall() throws IOException{
		int args=0;
		String nameIdentifier = br.currWord;
		
		br.advance();// ( or .
		if(br.currWord.equals("(")){
			String next = br.parts[br.pos];
			if(next.equals(")")){
				args = 1;
				vm.writePush("pointer", 0);
				vm.writeCall(classname+"."+nameIdentifier, args);
				
				br.advance();
			}
			else{
				br.advance();
				vm.writePush("pointer", 0);
				args = ComplileExpressionList() + 1;// it get out with )
				vm.writeCall(classname+"."+nameIdentifier, args);
			}
			//br.advance();// )
			
			
			
		}else if(br.currWord.equals(".")){
			String objectName = nameIdentifier;
			
			// .
			br.advance();//string after the . -->   .string
			nameIdentifier = br.currWord;
			String type = table.typeOf(objectName);
			
			if(type == null){
				nameIdentifier = objectName+"."+nameIdentifier;
			}else{
				args = 1;
				System.out.println("333");
				System.out.println(table.kindOf(objectName));
				vm.writePush(table.kindOf(objectName), table.indexOf(objectName));
				nameIdentifier = table.typeOf(objectName)+"."+nameIdentifier;
			}
			br.advance();// (

			br.advance();//first thing in exp or )
			if(br.currWord.equals(")")){
				
			}else{
				args += ComplileExpressionList();
				//br.advance();
			}
			vm.writeCall(nameIdentifier, args);
			// write )
			
		}
	}
	

	public int ComplileExpressionList() throws IOException{
		
		int args = 1;
		CompileExpression();
		br.advance();
		while(br.currWord.equals(",")){
			// write ,
			br.advance();
			CompileExpression();
			args++;
			br.advance();
		}
		return args;
	}
	
	public int compileParameterList() throws IOException{
		int count = 0;
		String type = br.currWord;
		br.advance();// varName
		String varname = br.currWord;
		table.define(varname, type, "argument");
		count++;
		br.advance();// , or )
		while(br.currWord.equals(",")){
			br.advance();//type
			type = br.currWord;
			br.advance();//name
			varname = br.currWord;
			table.define(varname, type, "argument");
			br.advance();// ,  or )
			count++;
		}
		return count;
	}
	
	
	/*private void write_type() throws IOException{
		if((br.currWord.equals("int")) || (br.currWord.equals("char")) || (br.currWord.equals("boolean"))){
			writeTo(br.currWord,"keyword");
		}
		else{
			writeTo(br.currWord,"identifier");
		}
	}
	
	private void writeTo(String token,String tag) throws IOException
	{
		bw.write("<"+tag+">"+token+"</"+tag+">\n");
	}*/

	public void advance() throws IOException{
		br.advance();
		br.advance();
		br.advance();
	
		br.advance();
		br.advance();
	}
	
}
