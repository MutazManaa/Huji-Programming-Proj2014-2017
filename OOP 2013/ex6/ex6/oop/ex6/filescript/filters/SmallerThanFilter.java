package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Matches files whose size in bytes is less then a given value.
 *
 * @author Mutaz
 *
 */
final class SmallerThanFilter extends NumericFilter {

	/**
	 * Creates a new SmallerThanFilter object.
	 *
	 * @param val
	 *            the size to compare to.
	 * @throws IllegalFilterValueException
	 *             Thrown when val is not a positive number.
	 */
	SmallerThanFilter(String val) throws IllegalFilterValueException {
		super(val);
	}

	@Override
	public boolean accept(File file) {
		return super.accept(file) && file.length() / BYTE_SIZE < _val1;
	}

}
