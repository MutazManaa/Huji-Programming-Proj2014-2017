/**
 * This class represents a math term which is either a single letter variable (x,y,a,b,etc..) 
 * or a number (may be a floating point number). The latex representation is straight
 *  forward the name of the variable, or the number itself. However, in case this term represents a number, 
 * the class will allow to user to control the precision of its latex representation,
 *  that is - the number of digits to the right of the floating dot.
 * @author mutazmanaa
 *
 */
public class SimpleMathTerm extends MathTerm {
	private String termName;
	
/**
 * Constructs a new instance given a simple term "name" (which can be a variable or a number).	
 * @param termName A string of either a single letter variable (x,y,z,a,b..) or a number 
 * (may be a floating point number).
 */
	public SimpleMathTerm(String termName){
		super.exponentStr = termName;
		this.termName = termName;
	}
	

/**
 * Checks the given name. And determines whether it's numeric.
 * @return true if this term represents a number.
 */
	public boolean isNumeric(){
		return this.termName.matches("[-+]?\\d*\\.?\\d+");
	 
		
	}
	
	/**
	 * Sets the number of digits of precision in case this term represents a number.
	 * @param precisionDigits Number of digits right of the floating point on the latex representation.
	 */
	
	public void setPrecisionDigits(int precisionDigits){
		if(this.isNumeric() == true){
			
			double precision = Math.pow(10, precisionDigits);
			double numericTermName = Double.parseDouble(super.exponentStr);//return the numeric of the string value
			numericTermName = (int)(numericTermName*precision);
			numericTermName = numericTermName/precision;	
			 super.exponentStr = String.valueOf(numericTermName);//transfer the number to a String
		}
		
		
	}
	
/**
 * the Latex representation. If this term represents a variable, this method returns the variable name. Otherwise, 
 * if the term represents a number it should be trimmed to according to the precision parameter.
 */
	public java.lang.String toLatex(){
		return super.exponentStr;
	
		
	}

}	
	

	