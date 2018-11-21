package oop.ex6.filescript.filters;

import java.io.*;

/**
 * an abstract class for number based filters.
 *
 * @author Mutaz
 *
 */
abstract class NumericFilter implements FileFilter {

	protected final int BYTE_SIZE = 1024;

	protected double _val1;
	protected double _val2;

	/**
	 * Creates a new numerical filter and rests values.
	 *
	 * @param val
	 *            first value to test.
	 * @throws FilterException
	 *             Thrown when v1 is not a positive number are not valid for
	 *             this filter.
	 */
	public NumericFilter(String val) throws IllegalFilterValueException {
		this(val, "0");
	}

	/**
	 * Creates a new numerical filter and rests values.
	 *
	 * @param val1
	 *            first value to test.
	 * @param val2
	 *            second value to test.
	 * @throws FilterException
	 *             Thrown when v1 or v2 are not positive numbers are not valid
	 *             for this filter.
	 */
	public NumericFilter(String val1, String val2)
			throws IllegalFilterValueException {
		try {
			_val1 = Double.valueOf(val1);
			_val2 = Double.valueOf(val2);
			if (_val1 < 0 || _val2 < 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {
			throw new IllegalFilterValueException(this.getClass().getName()
					+ "-" + val1 + "-" + val2);
		}
	}

	/**
	 * accept all files
	 */
	@Override
	public boolean accept(File file) {
		return file.isFile();
	}

}
