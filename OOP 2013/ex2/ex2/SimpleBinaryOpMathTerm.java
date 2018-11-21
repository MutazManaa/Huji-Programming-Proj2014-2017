/**
 * This class represents a simple operation between two other terms. For example, "a+b", "c*d" or "c=d","x
 * @author mutazmanaa
 *
 */
public class SimpleBinaryOpMathTerm extends BinaryMathTerm {
	private char sign;
	private String firstTerm;
	private String secondTerm;
	
	/**
	 * Instantiate a new SimpleBinaryOpMathTerm.
	 * @param firstTerm  The first term of the binary operation. 
	 * @param secondTerm  The second term of the binary operation.
	 * @param sign The operation sign. Can be any of the following: "+,-,*,<,>,=". If sign == '*', 
	 * you should use the \cdot latex command (a\cdotb). Otherwise you can use sign itself.
	 */
	public SimpleBinaryOpMathTerm (MathTerm firstTerm,MathTerm secondTerm,char sign){
		super(firstTerm,secondTerm);
		this.firstTerm = firstTerm.toLatex();
		this.secondTerm = secondTerm.toLatex();
		this.sign = sign;
		}
		
	
	/**
	 * Generates the latex representation of this arithmetic operation math term.
	 *@return The latex representation of the operation: "firstTerm operationSign secondTerm".
	 */
	public java.lang.String toLatex(){
		if (this.sign == '*'){
			super.exponentStr = this.firstTerm + " \\cdot " +this.secondTerm;
		}else {
			
		super.exponentStr = this.firstTerm + "" + sign + "" +this.secondTerm;
		}
		
		return super.exponentStr;
		
	}
	
	
	
	

}
