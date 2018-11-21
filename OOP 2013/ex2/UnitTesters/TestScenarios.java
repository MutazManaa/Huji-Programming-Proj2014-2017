import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestScenarios {

	SimpleMathTerm symbolicTerm;
	SimpleMathTerm numericTerm;
	SimpleBinaryOpMathTerm binaryTerm;
	FractionMathTerm fractionTerm;
	
	@Before
	public void initialize() {
		symbolicTerm = new SimpleMathTerm("a"); // a
		numericTerm = new SimpleMathTerm("3.14"); // 3.14
		binaryTerm = new SimpleBinaryOpMathTerm(symbolicTerm, numericTerm, '*'); // a \\cdot 3.14
		fractionTerm = new FractionMathTerm(binaryTerm, symbolicTerm); // \frac{ a \cdot 3.14 }{ a }
	}
	
	@Test(timeout = 1000)
	public void testBasics() {
		assertEquals("a", symbolicTerm.toLatex());
		assertEquals("3.14", numericTerm.toLatex());
		assertEquals("a \\cdot 3.14", binaryTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testBigExpressionAsExponent() {

		SimpleMathTerm secondSymTerm = new SimpleMathTerm("b");
		secondSymTerm.setExponentTerm(fractionTerm);
		
		assertEquals("b^{ \\frac{ a \\cdot 3.14 }{ a } }", secondSymTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testSumOfFractions() {
		SumMathTerm sumTerm = new SumMathTerm(new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"), new SimpleMathTerm("1"), '='), 
				              new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"),new SimpleMathTerm("n"),'<'),
				              fractionTerm);
		
		
		assertEquals("\\sum_{ i=1 }^{ i<n }{ \\frac{ a \\cdot 3.14 }{ a } }", sumTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void sumExponent() {
		SumMathTerm sumTerm = new SumMathTerm(new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"), new SimpleMathTerm("1"), '='), 
				              new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"),new SimpleMathTerm("n"),'<'),
				              fractionTerm);
		
		SimpleMathTerm expBase = new SimpleMathTerm("c");
		expBase.setExponentTerm(sumTerm);
		
		
		assertEquals("c^{ \\sum_{ i=1 }^{ i<n }{ \\frac{ a \\cdot 3.14 }{ a } } }", expBase.toLatex());
	}
	
	@Test(timeout = 1000)
	public void sumExponentWithNegation() {
		SumMathTerm sumTerm = new SumMathTerm(new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"), new SimpleMathTerm("1"), '='), 
				              new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"),new SimpleMathTerm("n"),'<'),
				              fractionTerm);
		
		SimpleMathTerm expBase = new SimpleMathTerm("c");
		expBase.setExponentTerm(sumTerm);
		expBase.setIsNegated(true);
		
		
		assertEquals("\\neg{ c^{ \\sum_{ i=1 }^{ i<n }{ \\frac{ a \\cdot 3.14 }{ a } } } }", expBase.toLatex());
	}

	
	@Test(timeout = 1000)
	public void sumExponentWithPrecision() {
		SumMathTerm sumTerm = new SumMathTerm(new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"), new SimpleMathTerm("1"), '='), 
				              new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"),new SimpleMathTerm("n"),'<'),
				              fractionTerm);
		
		SimpleMathTerm expBase = new SimpleMathTerm("4.99999999");
		expBase.setPrecisionDigits(2);
		expBase.setExponentTerm(sumTerm);
		
		assertEquals("4.99^{ \\sum_{ i=1 }^{ i<n }{ \\frac{ a \\cdot 3.14 }{ a } } }", expBase.toLatex());
	}
	
	@Test(timeout = 1000)
	public void sumExponentWithPrecisionBarred() {
		SumMathTerm sumTerm = new SumMathTerm(new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"), new SimpleMathTerm("1"), '='), 
				              new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"),new SimpleMathTerm("n"),'<'),
				              fractionTerm);
		
		SimpleMathTerm expBase = new SimpleMathTerm("4.99999999");
		expBase.setPrecisionDigits(2);
		expBase.setExponentTerm(sumTerm);
		expBase.setIsBarred(true);
		
		assertEquals("\\overline{ 4.99^{ \\sum_{ i=1 }^{ i<n }{ \\frac{ a \\cdot 3.14 }{ a } } } }", expBase.toLatex());
	}
	
	@Test(timeout = 1000)
	public void sumExponentWithPrecisionBarredAndNegation() {
		SumMathTerm sumTerm = new SumMathTerm(new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"), new SimpleMathTerm("1"), '='), 
				              new SimpleBinaryOpMathTerm(new SimpleMathTerm("i"),new SimpleMathTerm("n"),'<'),
				              fractionTerm);
		
		SimpleMathTerm expBase = new SimpleMathTerm("4.99999999");
		expBase.setPrecisionDigits(2);
		expBase.setExponentTerm(sumTerm);
		expBase.setIsBarred(true);
		expBase.setIsNegated(true);
		
		assertEquals("\\neg{ \\overline{ 4.99^{ \\sum_{ i=1 }^{ i<n }{ \\frac{ a \\cdot 3.14 }{ a } } } } }", expBase.toLatex());
	}



	
	


	

}
