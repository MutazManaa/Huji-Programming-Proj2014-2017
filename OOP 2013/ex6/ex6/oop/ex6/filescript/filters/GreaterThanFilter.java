package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Filters files whose size in bytes exceeds a given value.
 *
 * @author Mutaz
 *
 */
class GreaterThanFilter extends NumericFilter {

	/**
	 * Creates a new Greater size filter
	 *
	 * @param v1
	 *            the value to compare.
	 * @throws IllegalFilterValueException
	 *             Thrown when v1 is not a positive number.
	 */
	GreaterThanFilter(String v1) throws IllegalFilterValueException {
		super(v1);
	}

	/**
	 * @param file
	 *            The file to test
	 * @return True if the size of f is greater then the filter value.
	 */
	public boolean accept(File file) {
		return super.accept(file) && file.length() / BYTE_SIZE > _val1;
	}

}
