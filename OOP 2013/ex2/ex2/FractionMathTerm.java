/**
 * This class represents a special case of binary math term. 
 * It should be rendered as a fraction ("kav shever" in hebrew).
 * Implement the toLatex method using Latex \frac command
 * @author mutazmanaa
 *
 */
public class FractionMathTerm extends BinaryMathTerm {
	private String firstTerm, secondTerm;
	
/**
 * Constructs a new Fraction term. Keep in mind that the base class, BinaryMathTerm, 
 * should be be instantiated explicitly
 * (see: SimpleBinaryOpMathTerm.SimpleBinaryOpMathTerm(MathTerm, MathTerm, char)).	
 * @param firstTerm Term on the numerator ("Mone").
 * @param secondTerm  Term on the denominator ("Mechane").
 */
	public FractionMathTerm(MathTerm firstTerm, MathTerm secondTerm){
		super(firstTerm,secondTerm);
		this.firstTerm = firstTerm.toLatex();
		this.secondTerm = secondTerm.toLatex();
		
	}
	
	/**
	 * Generates the latex representation of this fraction math term.
	 * @return latex representation of this fraction math term using the \frac latex command.
	 */
	public java.lang.String toLatex(){
		super.exponentStr = "\\frac{ "+this.firstTerm+" }"+"{ "+this.secondTerm+" }";
		return super.exponentStr;
	}	
}
