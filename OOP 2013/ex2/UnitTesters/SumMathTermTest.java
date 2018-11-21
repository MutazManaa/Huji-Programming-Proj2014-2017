import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class SumMathTermTest {

	SimpleBinaryOpMathTerm binTerm;
	SimpleMathTerm firstSimpleTerm;
	SimpleMathTerm secondSimpleTerm;
	
	@Before
	public void initialize() { 
		binTerm = new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"), new SimpleMathTerm("1"), '=');
		firstSimpleTerm = new SimpleMathTerm("n");
		secondSimpleTerm = new SimpleMathTerm("x");
		
		secondSimpleTerm.setExponentTerm(new SimpleMathTerm("i"));
	}
	
	@Test(timeout=1000)
	public void testSimpleSum() {
		SumMathTerm sumTerm = new SumMathTerm(binTerm, firstSimpleTerm, secondSimpleTerm);
		
		assertEquals("\\sum_{ i=1 }^{ n }{ x^{ i } }", sumTerm.toLatex());
	}
	
	@Test(timeout=1000)
	public void testNestedSum() {
		SumMathTerm sumTerm = new SumMathTerm(binTerm, firstSimpleTerm, secondSimpleTerm);
		SumMathTerm secondSum = new SumMathTerm(binTerm, firstSimpleTerm, sumTerm);
				
		assertEquals("\\sum_{ i=1 }^{ n }{ \\sum_{ i=1 }^{ n }{ x^{ i } } }", secondSum.toLatex());
	}


}
