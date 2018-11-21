import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class BracketsMathTermTest {

	SimpleMathTerm simpleTerm;
	
	@Before
	public void initializeTerm() {
		simpleTerm = new SimpleMathTerm("a");
	}
	

	@Test(timeout = 1000)
	public void simpleBracketsTest() {
		BracketsMathTerm bracketsTerm = new BracketsMathTerm(simpleTerm);
		
		assertEquals("\\left( a \\right)", bracketsTerm.toLatex()); 
		
	}
	
	@Test(timeout = 1000)
	public void doubleBracketsTest() {
		BracketsMathTerm bracketsTerm = new BracketsMathTerm(simpleTerm);
		BracketsMathTerm secondBrackets = new BracketsMathTerm(bracketsTerm);
		
		assertEquals("\\left( \\left( a \\right) \\right)", secondBrackets.toLatex()); 
		
	}

}
