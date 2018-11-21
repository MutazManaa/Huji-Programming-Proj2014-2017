import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static java.lang.Math.max;
import static java.lang.Math.min;


/**
 * The class that extracts the tokens from jack source file
 */
public class JackTokenizer {


	private static String[] symbols = {"{", "}", "[", "]", ".",
			",", ";", "+", "-", "*", ">", "/", "&", "|", "<", "=", "(", ")", "~", "\""};
	private static final String keyWord_pattern = "(class|constructor|function|method|field|static|" +
			"var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)";//\\b";
	private static final Pattern keyWordPattern = Pattern.compile(keyWord_pattern);

	private static final String symbol_pattern = "\\{|\\}|\\(|\\)|\\[|\\]|\\.|\\,|;|\\+|-|\\*|\\/|&|" +
			"\\\\|\\||<|>|=|~";
	private static final Pattern symbolPattern = Pattern.compile(symbol_pattern);

	private static final String identifier_pattern = "^[^\\d\\W]\\w*";
	private static final Pattern identifierPattern = Pattern.compile(identifier_pattern);

	private static final String int_pattern = "\\d+";
	private static final Pattern intPattern = Pattern.compile(int_pattern);

	private static final String string_pattern = "\"[^\"]*\"";
	private static final Pattern stringPattern = Pattern.compile(string_pattern);

	private List<String> lines;
	private int nextLineNo = 0;
	private String nextLine = "";
	private String currentToken = "";

	/**constuct jack tokenizer*/
	JackTokenizer(String path) {
		try {
			lines = Files.readAllLines(Paths.get(path));
			nextLine = lines.get(0);
		} catch (IOException e) {

		}
	}

	/**
	 * return true if there are more tokens
	 * Note: this method also removes comments before the next token
	 */
	public boolean hasMoreTokens() {
		for (int i = nextLineNo; i < lines.size(); ) {

//			lines.set(i,lines.get(i).trim());
			nextLine = nextLine.trim();
			//lines.set(i,lines.get(i).trim().replaceAll("\\s+"," "));

			// catch /* and /** comments and delete them entirely (even if on multiple lines)
			if (nextLine.startsWith("/*")) {
				int commentStart = lines.get(i).lastIndexOf(nextLine);
				nextLine = nextLine.substring(2);
				//lines.set(i,lines.get(i).substring(0,lines.get(i).indexOf("/*")) + nextLine);
				if (nextLine.contains("*/")) {
					nextLine = nextLine.substring(nextLine.indexOf("*/") + 2);
					lines.set(i, lines.get(i).substring(0, commentStart) + nextLine);
				} else {
					lines.set(i, lines.get(i).substring(0, commentStart)); //delete comment start
					for (i++; i < lines.size(); i++) {
						if (lines.get(i).contains("*/")) {
							nextLine = lines.get(i).substring(lines.get(i).indexOf("*/") + 2);
							lines.set(i, nextLine);
							break;
						}
						lines.set(i, "");
					}
					if (i >= lines.size()) break;
				}
			}

			nextLine = nextLine.trim();

			// catch // comments and delete them
			if (nextLine.startsWith("//")) {
				lines.set(i, lines.get(i).substring(0, lines.get(i).lastIndexOf(nextLine)));
				nextLine = "";
			}
			if (!nextLine.equals("")) {
				nextLineNo = i;
				return true;
			}
			if (++i < lines.size()) nextLine = lines.get(i);
		}

		//we didn't find another token
		nextLineNo = lines.size();
		return false;
	}

	/**
	 * gets the next token in the code without altering the current token
	 *
	 * @return the next token in the code
	 */
	public String peek() {
		hasMoreTokens(); //to delete comments in the next segment of code

		//update current token
		Matcher identifierMatcher = identifierPattern.matcher(nextLine);
		Matcher keyWordMatcher = keyWordPattern.matcher(nextLine);
		Matcher symbolMatcher = symbolPattern.matcher(nextLine);
		Matcher intMatcher = intPattern.matcher(nextLine);
		Matcher stringMatcher = stringPattern.matcher(nextLine);

		if (identifierMatcher.lookingAt())
			return identifierMatcher.group();
		else if (keyWordMatcher.lookingAt())
			return keyWordMatcher.group();
		else if (symbolMatcher.lookingAt())
			return symbolMatcher.group();
		else if (intMatcher.lookingAt())
			return intMatcher.group();
		else if (stringMatcher.lookingAt())
			return stringMatcher.group();

		return "";
	}

	/**
	 * advance to the next instruction if there are more commands
	 */
	public void advance() {
		currentToken = peek();
		// remove current token from current string
		nextLine = nextLine.substring(currentToken.length());
	}


	/**
	 * @return command type
	 */
	public TokenType tokenType() {

		if (currentToken.matches(keyWord_pattern))
			return TokenType.KEYWORD;

		if (currentToken.matches(symbol_pattern))
			return TokenType.SYMBOL;

		if (currentToken.matches(identifier_pattern))
			return TokenType.IDENTIFIER;

		if (currentToken.matches(int_pattern))
			return TokenType.INT_CONST;

		if (currentToken.matches(string_pattern))
			return TokenType.STRING_CONST;

		return null;
	}

	/**
	 *
	 * @return keyword type
	 */
	public KeyWord keyWord() {
		switch (currentToken) {
			case "class":
				return KeyWord.CLASS;
			case "constructor":
				return KeyWord.CONSTRUCTOR;
			case "function":
				return KeyWord.FUNCTION;
			case "method":
				return KeyWord.METHOD;
			case "field":
				return KeyWord.FIELD;
			case "static":
				return KeyWord.STATIC;
			case "var":
				return KeyWord.VAR;
			case "int":
				return KeyWord.INT;
			case "char":
				return KeyWord.CHAR;
			case "boolean":
				return KeyWord.BOOLEAN;
			case "void":
				return KeyWord.VOID;
			case "true":
				return KeyWord.TRUE;
			case "false":
				return KeyWord.FALSE;
			case "null":
				return KeyWord.NULL;
			case "this":
				return KeyWord.THIS;
			case "let":
				return KeyWord.LET;
			case "do":
				return KeyWord.DO;
			case "if":
				return KeyWord.IF;
			case "else":
				return KeyWord.ELSE;
			case "while":
				return KeyWord.WHILE;
			case "return":
				return KeyWord.RETURN;
			default:
				return null;
		}
	}

	/**
	 * @return the symbol section of the token
	 */
	public char symbol() {
		return currentToken.charAt(0);
	}

	/**
	 * @return return the token if it's an identifier
	 */
	public String identifier() {
		return currentToken;
	}

	/**
	 * @return the value of the token if it's an integer
	 */
	public int intVal() {
		return Integer.parseInt(currentToken);
	}

	/**
	 * @return the value of the token if it's a string
	 */
	public String stringVal() {
		return currentToken.substring(1, currentToken.length() - 1);
	}

}
