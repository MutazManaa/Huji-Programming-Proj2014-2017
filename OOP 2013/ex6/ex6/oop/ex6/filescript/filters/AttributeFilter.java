package oop.ex6.filescript.filters;

import java.io.*;

/**
 * This abstract class is inherited by filters that use yes/no attribute of a
 * file.
 *
 * @author Mutaz
 *
 */
abstract class AttributeFilter implements FileFilter {

	/** The Yes indicator string */
	protected static final String YES_STRING = "YES";
	/** The No indicator string */
	protected static final String NO_STRING = "NO";
	/** the value of the filter */
	protected boolean _yesno;

	/**
	 * Interprets the value string to a boolean.
	 *
	 * @param val
	 *            The value to test.
	 * @return True if v1 equals "YES" and false if v1 equals "NO".
	 * @throws IllegalFilterValueException
	 *             Thrown if if v1 is nether YES or NO.
	 */
	AttributeFilter(String val) throws IllegalFilterValueException {
		if (val.equals(YES_STRING)) {
			_yesno = true;
		} else if (val.equals(NO_STRING)) {
			_yesno = false;
		} else {
			throw new IllegalFilterValueException(getClass().getName() + "-"
					+ val);
		}
	}

	public boolean accept(File file) {
		return file.isFile();
	}

}
