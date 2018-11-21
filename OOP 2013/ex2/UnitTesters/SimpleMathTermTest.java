import static org.junit.Assert.*;

import org.junit.Test;


public class SimpleMathTermTest {

	@Test(timeout = 1000)
	public void testToLatexVariable() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("a");
		
		assertEquals(simpleTerm.toLatex(), "a");
	}

	@Test(timeout = 1000)
	public void testNumeric() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("3.14");
		
		assertEquals("3.14", simpleTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testNumericNegative() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("-3.14");
				
		assertEquals("-3.14", simpleTerm.toLatex());
	}

	
	@Test(timeout = 1000)
	public void testExponent1() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("a");
		SimpleMathTerm secondSimpleTerm = new SimpleMathTerm("b");
		simpleTerm.setExponentTerm(secondSimpleTerm);
		
		assertEquals("a^{ b }", simpleTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testExponent2() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("a");
		SimpleMathTerm secondSimpleTerm = new SimpleMathTerm("b");
		simpleTerm.setExponentTerm(secondSimpleTerm);
		
		SimpleMathTerm thirdTerm = new SimpleMathTerm("2");
		thirdTerm.setExponentTerm(simpleTerm);
		
		assertEquals("2^{ a^{ b } }", thirdTerm.toLatex());
	}


	
	@Test(timeout = 1000)
	public void testPrecision() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("3.14");
		simpleTerm.setPrecisionDigits(1);
		
		assertEquals("3.1", simpleTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testPrecisionNegative() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("-3.14");
		simpleTerm.setPrecisionDigits(1);
		
		assertEquals("-3.1", simpleTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testNegation() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("3.14");
		
		simpleTerm.setIsNegated(true);
		
		assertEquals("\\neg{ 3.14 }", simpleTerm.toLatex());
	}
	
	@Test(timeout = 1000)
	public void testBarred() {
		SimpleMathTerm simpleTerm = new SimpleMathTerm("3.14");
		
		simpleTerm.setIsBarred(true);
		
		assertEquals("\\overline{ 3.14 }", simpleTerm.toLatex());
	}



	
	@Test(timeout = 1000)
	public void testTrimming() {
		SimpleMathTerm numericTerm = new SimpleMathTerm("3.14");
		numericTerm.setPrecisionDigits(0);
		
		assertTrue(Double.parseDouble(numericTerm.toLatex()) == 3.0);
	}
	
	@Test(timeout = 1000)
	public void testGetter() {
		SimpleMathTerm term1 = new SimpleMathTerm("a");
		SimpleMathTerm term2 = new SimpleMathTerm("b");
		
		term1.setExponentTerm(term2);
		
		assertTrue(term1.getExponentTerm() == term2);
	}



}
