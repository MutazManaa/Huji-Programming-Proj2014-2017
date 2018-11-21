package oop.ex6.filescript.orders;

import oop.ex6.filescript.Type1Error;

/**
 * an abstract exception to be inherited by other exceptions in orders package.
 *
 * @author Mutaz
 *
 */
public abstract class OrderException extends Type1Error {
	/**
	 *
	 */
	private static final long serialVersionUID = -5501053869990621044L;

	public OrderException(String message) {
		super(message);
	}
}
