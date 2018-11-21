import static org.junit.Assert.*;

import org.junit.Test;


public class SimpleBinaryOpMathTermTest {

	@Test(timeout=1000)
	public void testAddition() {
		SimpleMathTerm firstNumericTerm = new SimpleMathTerm("1");
		SimpleMathTerm secondNumericTerm = new SimpleMathTerm("1");
		
		SimpleBinaryOpMathTerm binaryTerm = new SimpleBinaryOpMathTerm(firstNumericTerm, secondNumericTerm,'+');
		
		assertEquals("1+1", binaryTerm.toLatex());
	}
	
	@Test(timeout=1000)
	public void testSubtraction() {
		SimpleMathTerm firstNumericTerm = new SimpleMathTerm("1");
		SimpleMathTerm secondNumericTerm = new SimpleMathTerm("1");
		
		SimpleBinaryOpMathTerm binaryTerm = new SimpleBinaryOpMathTerm(firstNumericTerm, secondNumericTerm,'-');
		
		assertEquals("1-1", binaryTerm.toLatex());
	}
	
	@Test(timeout=1000)
	public void testMultiplication() {
		SimpleMathTerm firstNumericTerm = new SimpleMathTerm("1");
		SimpleMathTerm secondNumericTerm = new SimpleMathTerm("1");
		
		SimpleBinaryOpMathTerm binaryTerm = new SimpleBinaryOpMathTerm(firstNumericTerm, secondNumericTerm,'*');
		
		assertEquals("1 \\cdot 1", binaryTerm.toLatex());
	}
	
	@Test(timeout=1000)
	public void testCompositeBinaryArguments1() {
		SimpleMathTerm firstNumericTerm = new SimpleMathTerm("1");	
		SimpleBinaryOpMathTerm secondTerm = 
			new SimpleBinaryOpMathTerm(new SimpleMathTerm("2"), new SimpleMathTerm("1"), '-');
		
		SimpleBinaryOpMathTerm binaryTerm = new SimpleBinaryOpMathTerm(firstNumericTerm, secondTerm,'=');
		
		assertEquals("1=2-1", binaryTerm.toLatex());
	}
	
	@Test(timeout=1000)
	public void testCompositeBinaryArguments2() {
		SimpleMathTerm firstTerm = new SimpleMathTerm("c");
		firstTerm.setExponentTerm(new SimpleMathTerm("2"));
		
		SimpleMathTerm secondTerm = new SimpleMathTerm("a");
		secondTerm.setExponentTerm(new SimpleMathTerm("2"));

		SimpleMathTerm thirdTerm = new SimpleMathTerm("b");
		thirdTerm.setExponentTerm(new SimpleMathTerm("2"));
		
		SimpleBinaryOpMathTerm binaryTerm = new SimpleBinaryOpMathTerm(secondTerm, thirdTerm, '+');
		SimpleBinaryOpMathTerm secondBinaryTherem = new SimpleBinaryOpMathTerm(firstTerm, binaryTerm, '=');
		
		assertEquals("c^{ 2 }=a^{ 2 }+b^{ 2 }", secondBinaryTherem.toLatex());
	}
	
	@Test(timeout=1000)
	public void testCompositeBinaryArguments3() {
		SimpleMathTerm firstTerm = new SimpleMathTerm("c");
		firstTerm.setExponentTerm(new SimpleMathTerm("2"));
		
		SimpleMathTerm secondTerm = new SimpleMathTerm("a");
		secondTerm.setExponentTerm(new SimpleMathTerm("2"));

		SimpleMathTerm thirdTerm = new SimpleMathTerm("b");
		thirdTerm.setExponentTerm(new SimpleMathTerm("2"));
		
		SimpleBinaryOpMathTerm binaryTerm = new SimpleBinaryOpMathTerm(secondTerm, thirdTerm, '+');
		SimpleBinaryOpMathTerm secondBinaryTherem = new SimpleBinaryOpMathTerm(firstTerm, binaryTerm, '=');
		
		assertEquals("c^{ 2 }=a^{ 2 }+b^{ 2 }", secondBinaryTherem.toLatex());
	}
	
	@Test(timeout=1000)
	public void testCompositeBinaryArguments4() {
		SimpleMathTerm firstTerm = new SimpleMathTerm("c");
		firstTerm.setExponentTerm(new SimpleMathTerm("2"));
		
		SimpleMathTerm secondTerm = new SimpleMathTerm("a");
		secondTerm.setExponentTerm(new SimpleMathTerm("2"));

		SimpleMathTerm thirdTerm = new SimpleMathTerm("b");
		thirdTerm.setExponentTerm(new SimpleMathTerm("2"));
		
		SimpleBinaryOpMathTerm binaryTerm = new SimpleBinaryOpMathTerm(secondTerm, thirdTerm, '+');
		SimpleBinaryOpMathTerm secondBinaryTherem = new SimpleBinaryOpMathTerm(firstTerm, binaryTerm, '=');
		
		assertEquals("c^{ 2 }=a^{ 2 }+b^{ 2 }", secondBinaryTherem.toLatex());
	}






}
