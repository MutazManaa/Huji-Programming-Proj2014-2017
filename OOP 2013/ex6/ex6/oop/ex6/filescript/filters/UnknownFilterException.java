package oop.ex6.filescript.filters;

/**
 * This exception is raised when an unknown filter name is used.
 *
 * @author Mutaz
 *
 */
public class UnknownFilterException extends FilterException {
	private static final long serialVersionUID = 16489842071062047L;

	public UnknownFilterException(String message) {
		super(message);
	}
}
