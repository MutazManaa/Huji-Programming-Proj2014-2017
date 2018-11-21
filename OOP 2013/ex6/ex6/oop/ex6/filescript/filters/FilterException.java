package oop.ex6.filescript.filters;

import oop.ex6.filescript.Type1Error;

/**
 * This is an abstract filter exception object to be inherited by other
 * exceptions.
 *
 * @author Mutaz
 *
 */
public abstract class FilterException extends Type1Error {
	/**
	 *
	 */
	private static final long serialVersionUID = 2682189124490957354L;

	public FilterException(String message) {
		super(message);
	}
}
