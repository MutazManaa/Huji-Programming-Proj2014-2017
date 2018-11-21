
import java.io.IOException;


/**
 * The class write to output file the translation of jack code.
 */
class CompilationEngine {


//    private BufferedWriter out = null;
    private JackTokenizer tokenizer;
    private SymbolTable symbolTable;
	private VMWriter vw;
	private int labelNo=0;

	/**
	 * initializes the buffered writer according to the given fileName
	 * @param outFileName the fil  translating to.
	 *	Creates a new compilation engine with the
	 *	given input and output. The next routine
	 *	called must be compileClass(). 
	 */
    CompilationEngine(String path, String outFileName) {
            //String tempName = outFileName.substring(outFileName.lastIndexOf('/')+1, outFileName.lastIndexOf('.'));
//            FileWriter fstream = new FileWriter(outFileName + ".xml", false); //true tells to append data.
//            out = new BufferedWriter(fstream);

			vw = new VMWriter(outFileName);

            tokenizer = new JackTokenizer(path);
            if(tokenizer.hasMoreTokens()) tokenizer.advance();
            //writeInit();

    }


    /**Compiles a complete class.*/
    public void CompileClass()throws IOException{

		next();

		symbolTable = new SymbolTable(tokenizer.identifier());
		next();
		next();

		while(tokenizer.tokenType() == TokenType.KEYWORD &&(tokenizer.keyWord() == KeyWord.STATIC ||
				tokenizer.keyWord() == KeyWord.FIELD ))
			CompileClassVarDec();
		while(tokenizer.tokenType() == TokenType.KEYWORD &&(tokenizer.keyWord() == KeyWord.CONSTRUCTOR ||
				tokenizer.keyWord() ==KeyWord.FUNCTION || tokenizer.keyWord() == KeyWord.METHOD))
			CompileSubroutine();

		next();

	}

	/**Compiles a static declaration or a field declaration. */
	public void CompileClassVarDec()throws IOException{

		IdentifierKind kind;
		if(tokenizer.keyWord().toString().toLowerCase().equals("static"))
			kind = IdentifierKind.STATIC;
		else
			kind = IdentifierKind.FIELD;
		next();

		String type = tokenizer.identifier();
		next();

		symbolTable.define(tokenizer.identifier(),type,kind);
		next();

		while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',') {
			next();

			symbolTable.define(tokenizer.identifier(),type,kind);
			next();
		}
		next();

	}

	/**Compiles a complete method, function, or constructor. */
	public void CompileSubroutine()throws IOException{

		KeyWord subroutineType = tokenizer.keyWord();
		next();

//		boolean isVoid = tokenizer.tokenType() == TokenType.KEYWORD && tokenizer.keyWord() == KeyWord.VOID;
		next();

		symbolTable.startSubroutine(tokenizer.identifier());
		if(subroutineType == KeyWord.METHOD)
			symbolTable.define("this",tokenizer.identifier(),IdentifierKind.ARG);


		next();

		next();
		compileParameterList();
		next();



		CompileSubroutineBody(subroutineType);

	}

	/**Compiles a (possibly empty) parameter list,not including the enclosing parentheses. */
	public void compileParameterList()throws IOException{


		if((tokenizer.tokenType() == TokenType.KEYWORD && (tokenizer.keyWord() == KeyWord.INT ||
															tokenizer.keyWord() == KeyWord.CHAR ||
															tokenizer.keyWord() == KeyWord.BOOLEAN))
			||tokenizer.tokenType() == TokenType.IDENTIFIER)
		{
			String type;
			if(tokenizer.tokenType() == TokenType.KEYWORD)
				type = tokenizer.keyWord().toString().toLowerCase();
			else
				type = tokenizer.identifier();

			next();

			symbolTable.define(tokenizer.identifier(),type,IdentifierKind.ARG);
			next();

			while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',') {
				next();

				if(tokenizer.tokenType() == TokenType.KEYWORD)
					type = tokenizer.keyWord().toString().toLowerCase();
				else
					type = tokenizer.identifier();
				next();

				symbolTable.define(tokenizer.identifier(),type,IdentifierKind.ARG);
				next();
			}
		}
	}


	/**compiles subroutine internal body*/

	private void CompileSubroutineBody(KeyWord subroutineType)throws IOException{

		next();

		while(tokenizer.tokenType()==TokenType.KEYWORD && tokenizer.keyWord() == KeyWord.VAR)
			compileVarDec();

		vw.writeFunction(symbolTable.getClassName() +"." +symbolTable.getSubroutineName()
				,symbolTable.VarCount(IdentifierKind.VAR));

		if(subroutineType == KeyWord.METHOD)
		{
			vw.writePush(VMWriter.ARG,0);
			vw.writePop(VMWriter.POINTER,0);
		}else if(subroutineType == KeyWord.CONSTRUCTOR)
		{
			vw.writePush(VMWriter.CONST,symbolTable.VarCount(IdentifierKind.FIELD));
			vw.writeCall("Memory.alloc",1);
			vw.writePop(VMWriter.POINTER,0);
		}


		compileStatements();
		next();

	}


	/**Compiles a var declaration. */
	public void compileVarDec()throws IOException{

		next();
		String type;
		if(tokenizer.tokenType() == TokenType.KEYWORD)
			type = tokenizer.keyWord().toString().toLowerCase();
		else
			type = tokenizer.identifier();

		next();

		symbolTable.define(tokenizer.identifier(),type,IdentifierKind.VAR);
		next();

		while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',') {
			next();

			symbolTable.define(tokenizer.identifier(),type,IdentifierKind.VAR);
			next();
		}
		next();
	}


	/**Compiles a sequence of statements, not including the enclosing curled parentheses. */
	public void compileStatements()throws IOException{

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

	}


	/**Compiles a let statement*/
	public void compileLet()throws IOException{

		next();

		String varName = tokenizer.identifier();
		next();

		boolean isArray = tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol() == '[';
		if(isArray){
			pushFromSymbol(varName);
			next();
			CompileExpression();
			next();
			vw.writeArithmetic(VMArithmetic.ADD);
			vw.writePop(VMWriter.TEMP,1);
		}


		next();
		CompileExpression();
		next();

		if(isArray){
			vw.writePush(VMWriter.TEMP,1);
			vw.writePop(VMWriter.POINTER,1);
			vw.writePop(VMWriter.THAT,0);
		}else{
			popToSymbol(varName);
		}

	}


	/**Compiles an if statement, possibly with a trailing else clause. */
	public void compileIf()throws IOException{

		String L1 = "IF"+(labelNo++);
		String L2 = "IF"+(labelNo++);

		next();
		next();
		CompileExpression();
		vw.writeArithmetic(VMArithmetic.NOT);
		vw.writeIf(L1);

		next();
		next();
		compileStatements();
		next();

		vw.writeGoto(L2);
		vw.writelabel(L1);
		if(tokenizer.tokenType()==TokenType.KEYWORD && tokenizer.keyWord()==KeyWord.ELSE)
		{
			next();
			next();
			compileStatements();
			next();
		}
		vw.writelabel(L2);

	}


	/**Compiles a while statement. */
	public void compileWhile()throws IOException{
		String L1 = "WHILE"+(labelNo++);
		String L2 = "WHILE"+(labelNo++);

		vw.writelabel(L1);
		next();
		next();
		CompileExpression();
		vw.writeArithmetic(VMArithmetic.NOT);
		vw.writeIf(L2);

		next();
		next();
		compileStatements();
		next();

		vw.writeGoto(L1);
		vw.writelabel(L2);

	}


	/**Compiles a do statement. */
	public void compileDo()throws IOException{

		next();
		compileSubroutineCall();
		next();

		vw.writePop(VMWriter.TEMP,0);
	}


	/**Compiles a return statement. */

	public void compileReturn()throws IOException{

		next();
		if(!(tokenizer.tokenType()==TokenType.SYMBOL && tokenizer.symbol()==';'))
			CompileExpression();
		else
			vw.writePush(VMWriter.CONST,0);

		next();

		vw.writeReturn();
	}


	/**Compiles an expression.*/

	public void CompileExpression()throws IOException{

		CompileTerm();
		while(tokenizer.tokenType()== TokenType.SYMBOL) {
			VMArithmetic op = null;
			switch (tokenizer.symbol()) {
				case '+':
					op = VMArithmetic.ADD;
					break;
				case '-':
					op = VMArithmetic.SUB;
					break;
				case '*':
					op = VMArithmetic.MULT;
					break;
				case '/':
					op = VMArithmetic.DIVIDE;
					break;
				case '&':
					op =VMArithmetic.AND;
					break;
				case '|':
					op = VMArithmetic.OR;
					break;
				case '<':
					op = VMArithmetic.LT;
					break;
				case '>':
					op = VMArithmetic.GT;
					break;
				case '=':
					op = VMArithmetic.EQ;
					break;
				default:
					return;
			}
			next();
			CompileTerm();
			vw.writeArithmetic(op);
		}
	}


	/**Compiles a term. */
	public void CompileTerm()throws IOException{

		if(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol() == '(') {
			next();
			CompileExpression();
			next();
			}

		else if (tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol() == '~'){
			next();
			CompileTerm();
			vw.writeArithmetic(VMArithmetic.NOT);
		} else if (tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol() == '-') {
			next();
			CompileTerm();
			vw.writeArithmetic(VMArithmetic.NEG);
		}

		else if (tokenizer.tokenType() == TokenType.IDENTIFIER && (tokenizer.peek().equals("(")||
					tokenizer.peek().equals("."))) {
			compileSubroutineCall();
		}else if (tokenizer.tokenType() == TokenType.IDENTIFIER && tokenizer.peek().equals("[")) {
			pushFromSymbol(tokenizer.identifier());
			next();
			next();
			CompileExpression();
			next();

			vw.writeArithmetic(VMArithmetic.ADD);
			vw.writePop(VMWriter.POINTER, 1);
			vw.writePush(VMWriter.THAT, 0);

		}else if(tokenizer.tokenType()==TokenType.IDENTIFIER){
			pushFromSymbol(tokenizer.identifier());
			next();

		}else if(tokenizer.tokenType()==TokenType.KEYWORD){
			switch (tokenizer.keyWord())
			{
				case TRUE:
				vw.writePush(VMWriter.CONST,1);
				vw.writeArithmetic(VMArithmetic.NEG);
				break;

				case FALSE:
				case NULL:
				vw.writePush(VMWriter.CONST,0);
				break;

				case THIS:
				vw.writePush(VMWriter.POINTER,0);
				break;
			}
			next();

		}else if(tokenizer.tokenType()==TokenType.STRING_CONST){
			String string = tokenizer.stringVal();
			string = string.replaceAll("\t","\\\\t");
			string = string.replaceAll("\n","\\\\n");
			string = string.replaceAll("\r","\\\\r");
			string = string.replaceAll("\b","\\\\b");
			vw.writePush(VMWriter.CONST,string.length());
			vw.writeCall("String.new",1);
			for(char c : string.toCharArray())
			{
				vw.writePush(VMWriter.CONST,c);
				vw.writeCall("String.appendChar",2);
			}
			next();

		}else if(tokenizer.tokenType()==TokenType.INT_CONST) {
			vw.writePush(VMWriter.CONST,tokenizer.intVal());
			next();

		}

	}


	/**Compiles a (possibly empty) comma separated list of expressions.*/
	public int CompileExpressionList()throws IOException{

		int count =0;
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
			count++;
			while(tokenizer.tokenType() == TokenType.SYMBOL && tokenizer.symbol()==',')
			{
				next();
				CompileExpression();
				count++;
			}

		}
		return count;
	}


	/**compile subroutine caller in a routine*/
	private void compileSubroutineCall() throws  IOException{
		//TODO check if called method within a method
		int argsCount =0;
		String name = tokenizer.identifier();
		next();
		if(tokenizer.tokenType()==TokenType.SYMBOL && tokenizer.symbol() =='.')
		{
			next();
			if(symbolTable.KindOf(name) != IdentifierKind.NONE) { // method
				pushFromSymbol(name);
				name = symbolTable.TypeOf(name) + "." + tokenizer.identifier();
				argsCount++;
			}else{ 			// constructor/function
				name += "." +tokenizer.identifier();
			}
			next();
		}else 				// method on this
		{
			name = symbolTable.getClassName()+ "." + name;
			vw.writePush(VMWriter.POINTER,0);
			argsCount++;
		}
		next();
		argsCount += CompileExpressionList();
		next();
		vw.writeCall(name,argsCount);
	}


	private void popToSymbol(String name) throws  IOException{
		switch (symbolTable.KindOf(name))
		{
			case ARG:
			vw.writePop("argument",symbolTable.IndexOf(name));
			break;

			case VAR:
			vw.writePop("local",symbolTable.IndexOf(name));
			break;

			case STATIC:
			vw.writePop("static",symbolTable.IndexOf(name));
			break;

			case FIELD:
			vw.writePop("this",symbolTable.IndexOf(name));
			break;
		}
	}

	private void pushFromSymbol(String name) throws  IOException{
		switch (symbolTable.KindOf(name))
		{
			case ARG:
				vw.writePush("argument",symbolTable.IndexOf(name));
				break;

			case VAR:
				vw.writePush("local",symbolTable.IndexOf(name));
				break;

			case STATIC:
				vw.writePush("static",symbolTable.IndexOf(name));
				break;

			case FIELD:
				vw.writePush("this",symbolTable.IndexOf(name));
				break;
		}
	}


	/**method to get next command*/
	private void next(){
    	if (tokenizer.hasMoreTokens()) tokenizer.advance();
	}

	/**close the output file*/
	public void close() {

		vw.close();
	}
}

