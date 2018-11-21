package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Filters files who's size is between two given values.
 *
 * @author Mutaz
 *
 */
final class BetweenFilter extends NumericFilter {

	/**
	 * Creates a new between filter.
	 *
	 * @param v1
	 *            Boundary 1
	 * @param v2
	 *            Boundary 2
	 * @throws IllegalFilterValueException
	 *             thrown when v1 or v2 are not positive numbers.
	 */
	public BetweenFilter(String v1, String v2)
			throws IllegalFilterValueException {
		super(v1, v2);
	}

	/**
	 *
	 * checks if the size of a file is between two values.
	 *
	 * @param file
	 *            The file to check.
	 * @return True if the size of file in bytes is between the two filter's
	 *         values. parsed to a number.
	 */
	public boolean accept(File file) {
		double length = file.length();
		return super.accept(file) && (length / BYTE_SIZE > _val1)
				&& (length / BYTE_SIZE < _val2);
	}

}
