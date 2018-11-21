import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class FractionMathTermTest {

	SimpleMathTerm firstTerm;
	SimpleMathTerm secondTerm;
	
	@Before
	public void initializeTerms() {
		firstTerm = new SimpleMathTerm("a");
		secondTerm = new SimpleMathTerm("b");		
	}
	
	@Test(timeout = 1000)
	public void testSimpleFraction() {
		FractionMathTerm fractionTerm = new FractionMathTerm(firstTerm, secondTerm);
		
		assertEquals("\\frac{ a }{ b }", fractionTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testNestedFraction() {
		FractionMathTerm fractionTerm = new FractionMathTerm(firstTerm, secondTerm);
		FractionMathTerm secondFraction = new FractionMathTerm(fractionTerm, fractionTerm);
		
		assertEquals("\\frac{ \\frac{ a }{ b } }{ \\frac{ a }{ b } }", secondFraction.toLatex());
	}

}
