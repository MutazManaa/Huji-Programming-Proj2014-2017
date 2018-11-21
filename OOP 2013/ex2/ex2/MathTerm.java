/**
 * 
 * @author mutazmanaa
 * 
 *
 */
public class MathTerm {

	protected MathTerm exponentTerm;
	protected String exponentStr = "";
	private boolean isBarred = false;
	private boolean isNegated = false;
	

	public MathTerm(){};
	
	/**
	 * This method gets a math term to be placed as an exponent for the 
	 * current math term. 
	 *For example. If our current MathTerm is "a" and the user passes "b". 
	 * Then our Mathterm  will be rendered as "a^{ b }".
	 * @param exponentTerm-The MathTerm to be placed as an exponent of the current term.
	 */
	public void setExponentTerm(MathTerm exponentTerm){
		this.exponentTerm = exponentTerm;
		this.exponentStr = this.exponentStr+"^{ "+ this.exponentTerm.toLatex() +" }";
		
	}
	
	/**
	 * Returns the exponent math term.
	 * @return The exponent MathTerm of this term.
	 */
	public MathTerm getExponentTerm(){
		
		return this.exponentTerm;	
	}
	
	/**
	 * Setting whether this MathTerm should be barred or not 
	 * (a straight line on top of the term: see Latex's \overline{}).
	 * @param isBarred- true if we want this term to be barred.
	 */
	public void setIsBarred(boolean isBarred){
		this.isBarred = isBarred;
		if(this.isBarred == true){
			this.exponentStr = "\\overline{ "+ this.exponentStr + " }";
		}
		
	}
	
	/**
	 * isBarred getter.
	 * @return returns whether this math term was set to be barred.
	 */
	public boolean getIsBarred(){
		return this.isBarred;
		
	}
	
	/**
	 * Sets whether this math term should be negated (see Latex's \neg{}).
	 * @param isNegated
	 */
	public void setIsNegated(boolean isNegated){
		this.isNegated = isNegated;
		if (this.isNegated == true){
			this.exponentStr = "\\neg{ "+ this.exponentStr + " }";//represent the string of the mathterm
		}
	}	
	
	/**
	 * isNegated getter.
	 * @return True if this term should be negated.
	 */
	public boolean getIsNegated(){
		return this.isNegated;
		
	}
	

	/**
	 * This method should be implemented in any of MathTerm derivatives (inheriting classes).
	 *  However, we will leave it unimplemented (returns empty string).
	 * @return 
	 */
	public java.lang.String toLatex(){
		return "";
	}
		
}

