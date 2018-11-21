
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The class write to output file the translation of jack code.
 */
class CompilationEngine {


    private BufferedWriter out = null;
    private JackTokenizer tokenizer;

	/**
	 * initializes the buffered writer according to the given fileName
	 * @param outFileName the fil  translating to.
	 *	Creates a new compilation engine with the
	 *	given input and output. The next routine
	 *	called must be compileClass(). 
	 */
    CompilationEngine(String path, String outFileName) {
        try {
            //String tempName = outFileName.substring(outFileName.lastIndexOf('/')+1, outFileName.lastIndexOf('.'));
            FileWriter fstream = new FileWriter(outFileName + ".xml", false); //true tells to append data.
            out = new BufferedWriter(fstream);

            tokenizer = new JackTokenizer(path);
            if(tokenizer.hasMoreTokens()) tokenizer.advance();
            //writeInit();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }


    /**Compiles a complete class.*/
    public void CompileClass()throws IOException{
    	out.write("<class>\n");

		compileTerminal();
		compileTerminal();
		compileTerminal();
		while(tokenizer.tokenType() == TokenType.KEYWORD &&(tokenizer.keyWord() == KeyWord.STATIC ||
				tokenizer.keyWord() == KeyWord.FIELD ))
			CompileClassVarDec();
		while(tokenizer.tokenType() == TokenType.KEYWORD &&(tokenizer.keyWord() == KeyWord.CONSTRUCTOR ||
				tokenizer.keyWord() ==KeyWord.FUNCTION || tokenizer.keyWord() == KeyWord.METHOD))
			CompileSubroutine();

		compileTerminal();

		out.write("</class>\n");
	}

	/**Compiles a static declaration or a field declaration. */
	public void CompileClassVarDec()throws IOException{
		out.write("<classVarDec>\n");

		compileTerminal();
		compileTerminal();
		compileTerminal();
		while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',') {
			compileTerminal();
			compileTerminal();
		}
		compileTerminal();

		out.write("</classVarDec>\n");
	}

	/**Compiles a complete method, function, or constructor. */
	public void CompileSubroutine()throws IOException{
		out.write("<subroutineDec>\n");

		compileTerminal();
		compileTerminal();
		compileTerminal();
		compileTerminal();
		compileParameterList();
		compileTerminal();
		CompileSubroutineBody();

		out.write("</subroutineDec>\n");
	}

	/**Compiles a (possibly empty) parameter list,not including the enclosing parentheses. */
	public void compileParameterList()throws IOException{
		out.write("<parameterList>\n");

		if((tokenizer.tokenType() == TokenType.KEYWORD && (tokenizer.keyWord() == KeyWord.INT ||
															tokenizer.keyWord() == KeyWord.CHAR ||
															tokenizer.keyWord() == KeyWord.BOOLEAN))
			||tokenizer.tokenType() == TokenType.IDENTIFIER)
		{
			compileTerminal();
			compileTerminal();
			while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',') {
				compileTerminal();
				compileTerminal();
				compileTerminal();
			}
		}

		out.write("</parameterList>\n");
	}


	/**compiles subroutin internal body*/

	private void CompileSubroutineBody()throws IOException{
		out.write("<subroutineBody>\n");

		compileTerminal();
		while(tokenizer.tokenType()==TokenType.KEYWORD && tokenizer.keyWord() == KeyWord.VAR)
			compileVarDec();
		compileStatements();
		compileTerminal();

		out.write("</subroutineBody>\n");
	}


	/**Compiles a var declaration. */
	public void compileVarDec()throws IOException{
		out.write("<varDec>\n");

		compileTerminal();
		compileTerminal();
		compileTerminal();
		while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',') {
			compileTerminal();
			compileTerminal();
		}
		compileTerminal();

		out.write("</varDec>\n");
	}


	/**Compiles a sequence of statements, not including the enclosing curled parentheses. */
	public void compileStatements()throws IOException{
		out.write("<statements>\n");

		while(tokenizer.tokenType()==TokenType.KEYWORD && (tokenizer.keyWord() == KeyWord.LET ||
															tokenizer.keyWord() == KeyWord.IF ||
															tokenizer.keyWord() == KeyWord.WHILE ||
															tokenizer.keyWord() == KeyWord.DO ||
															tokenizer.keyWord() == KeyWord.RETURN))
		{
			switch (tokenizer.keyWord())
			{
				case LET:
					compileLet();
					break;
				case IF:
					compileIf();
					break;
				case WHILE:
					compileWhile();
					break;
				case DO:
					compileDo();
					break;
				case RETURN:
					compileReturn();
					break;
			}
		}

		out.write("</statements>\n");
	}


	/**Compiles a let statement*/
	public void compileLet()throws IOException{
		out.write("<letStatement>\n");

		compileTerminal();
		compileTerminal();
		if(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol() == '['){
			compileTerminal();
			CompileExpression();
			compileTerminal();
		}
		compileTerminal();
		CompileExpression();
		compileTerminal();

		out.write("</letStatement>\n");
	}


	/**Compiles an if statement, possibly with a trailing else clause. */
	public void compileIf()throws IOException{
		out.write("<ifStatement>\n");

		compileTerminal();
		compileTerminal();
		CompileExpression();
		compileTerminal();
		compileTerminal();
		compileStatements();
		compileTerminal();
		if(tokenizer.tokenType()==TokenType.KEYWORD && tokenizer.keyWord()==KeyWord.ELSE)
		{
			compileTerminal();
			compileTerminal();
			compileStatements();
			compileTerminal();
		}

		out.write("</ifStatement>\n");
	}


	/**Compiles a while statement. */
	public void compileWhile()throws IOException{
		out.write("<whileStatement>\n");

		compileTerminal();
		compileTerminal();
		CompileExpression();
		compileTerminal();
		compileTerminal();
		compileStatements();
		compileTerminal();

		out.write("</whileStatement>\n");
	}


	/**Compiles a do statement. */
	public void compileDo()throws IOException{
		out.write("<doStatement>\n");

		compileTerminal();
		compileSubroutineCall();
		compileTerminal();

		out.write("</doStatement>\n");
	}


	/**Compiles a return statement. */

	public void compileReturn()throws IOException{
		out.write("<returnStatement>\n");

		compileTerminal();
		if(!(tokenizer.tokenType()==TokenType.SYMBOL && tokenizer.symbol()==';'))
			CompileExpression();
		compileTerminal();

		out.write("</returnStatement>\n");
	}


	/**Compiles an expression.*/

	public void CompileExpression()throws IOException{
		out.write("<expression>\n");

		CompileTerm();
		while(tokenizer.tokenType()== TokenType.SYMBOL &&(tokenizer.symbol() == '+' ||
															tokenizer.symbol() == '-'||
															tokenizer.symbol() == '*'||
															tokenizer.symbol() == '/'||
															tokenizer.symbol() == '&'||
															tokenizer.symbol() == '|'||
															tokenizer.symbol() == '<'||
															tokenizer.symbol() == '>'||
															tokenizer.symbol() == '='))
		{
			compileTerminal();
			CompileTerm();
		}

		out.write("</expression>\n");
	}


	/**Compiles a term. */
	public void CompileTerm()throws IOException{
		out.write("<term>\n");

		if(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol() == '(') {
			compileTerminal();
			CompileExpression();
			compileTerminal();
		}else if (tokenizer.tokenType() == TokenType.SYMBOL && (tokenizer.symbol() == '-' ||
																tokenizer.symbol() == '~' )) {
			compileTerminal();
			CompileTerm();
		}else if (tokenizer.tokenType() == TokenType.IDENTIFIER && (tokenizer.peek().equals("(")||
					tokenizer.peek().equals("."))) {
			compileSubroutineCall();
		}else if (tokenizer.tokenType() == TokenType.IDENTIFIER && tokenizer.peek().equals("[")) {
			compileTerminal();
			compileTerminal();
			CompileExpression();
			compileTerminal();
		}else{
			compileTerminal();
		}

		out.write("</term>\n");
	}


	/**Compiles a (possibly empty) commaseparated list of expressions.*/
	public void CompileExpressionList()throws IOException{
		out.write("<expressionList>\n");
		if((tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol() == '(' )||
				(tokenizer.tokenType() == TokenType.SYMBOL && (tokenizer.symbol() == '-' ||
						tokenizer.symbol() == '~' ))||
				(tokenizer.tokenType() == TokenType.IDENTIFIER && (tokenizer.peek().equals("(")||
																	tokenizer.peek().equals(".")))||
				(tokenizer.tokenType() == TokenType.IDENTIFIER && tokenizer.peek().equals("["))||
				(tokenizer.tokenType()== TokenType.IDENTIFIER)||
				(tokenizer.tokenType()== TokenType.KEYWORD && (tokenizer.keyWord() == KeyWord.TRUE ||
																tokenizer.keyWord() == KeyWord.FALSE||
																tokenizer.keyWord() == KeyWord.NULL||
																tokenizer.keyWord() == KeyWord.THIS))||
				tokenizer.tokenType()== TokenType.INT_CONST ||
				tokenizer.tokenType() == TokenType.STRING_CONST)
		{
			CompileExpression();
			while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',')
			{
				compileTerminal();
				CompileExpression();
			}

		}

		out.write("</expressionList>\n");
	}


	/**compile subroutin caller in a routine*/
	private void compileSubroutineCall() throws  IOException{
		compileTerminal();
		if(tokenizer.tokenType()==TokenType.SYMBOL && tokenizer.symbol() =='.')
		{
			compileTerminal();
			compileTerminal();
		}
		compileTerminal();
		CompileExpressionList();
		compileTerminal();
	}


	/**method to write compilation process*/
	private void compileTerminal()throws IOException{
    	String type ="";
    	switch (tokenizer.tokenType())
		{
			case KEYWORD:
				type ="keyword";
				out.write("<"+type+"> "+tokenizer.keyWord().toString().toLowerCase()+" </"+type+">\n");
				break;
			case IDENTIFIER:
				type ="identifier";
				out.write("<"+type+"> "+tokenizer.identifier()+" </"+type+">\n");
				break;
			case SYMBOL:
				type ="symbol";
				String symbol = tokenizer.symbol()+"";
				symbol = symbol.replace("&","&amp;");
				symbol = symbol.replace("<","&lt;");
				symbol = symbol.replace(">","&gt;");
				symbol = symbol.replace("\"","&quot;");
				out.write("<"+type+"> "+symbol+" </"+type+">\n");
				break;
			case INT_CONST:
				type = "integerConstant";
				out.write("<"+type+"> "+tokenizer.intVal()+" </"+type+">\n");
				break;
			case STRING_CONST:
				type = "stringConstant";
				out.write("<"+type+"> "+tokenizer.stringVal()+" </"+type+">\n");
				break;
		}
    	if (tokenizer.hasMoreTokens()) tokenizer.advance();
	}


//    void writeInit() throws IOException { //TODO
//
//        //initalizing the stack pointer
//        out.write("@256\n" +
//                "D=A\n" +
//                "@SP\n" +
//                "M=D\n");
//       // writeCall("Sys.init",0);
//
//    }



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

