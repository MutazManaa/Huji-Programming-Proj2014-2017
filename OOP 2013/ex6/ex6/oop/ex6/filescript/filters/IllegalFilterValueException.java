package oop.ex6.filescript.filters;

/**
 * This exception occurs when an illegal value is passed to the filter
 * 
 * @author Mutaz
 * 
 */
public class IllegalFilterValueException extends FilterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2379867179341762359L;

	public IllegalFilterValueException(String message) {
		super(message);
	}
}
