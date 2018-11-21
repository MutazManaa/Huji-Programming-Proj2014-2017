/**
 * This class represents a mathematical sum. It comprised of 3 MathTerms: - Term beneath the sigma sign.
 * - Term above the sigma sign. - Term being summed. Use the \sum latex command, 
 *to generate the latex representation of this MathTerm.
 * @author mutazmanaa
 *
 */
public class SumMathTerm extends MathTerm {
	
	private MathTerm subTerm, superTerm, sumTerm;
	
	/**
	 * The constructor receives the 3 MathTerm that comprises the sum math term.
	 * @param subTerm The term beneath the sigma.
	 * @param superTerm The term above the sigma
	 * @param sumTerm The summed term.
	 */
	
	public SumMathTerm(MathTerm subTerm, MathTerm superTerm, MathTerm sumTerm){
		this.subTerm = subTerm;
		this.superTerm = superTerm;
		this.sumTerm = sumTerm;
	}
	
	/**
	 * Generates the latex representation of this sum math term
	 *@return latex representation using the \sum command.
	 */
	public java.lang.String toLatex(){
		super.exponentStr = "\\sum_{ " + this.subTerm.toLatex() + " }^{ "+ this.superTerm.toLatex() + " }{ " 
			+ this.sumTerm.toLatex() + " }";
			return super.exponentStr;

	}
			
	
}
