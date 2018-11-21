package oop.ex6.filescript.filters;

import java.io.*;

/**
 * an abstract class tpo be inherited by text based filters
 *
 * @author Mutaz
 *
 */
abstract class NameFilter implements FileFilter {

	protected String _v1;

	/**
	 * creates a new name filter with the given filter value.
	 *
	 * @param v1
	 *            The value for the filter.
	 * @throws IllegalFilterValueException
	 *             Thrown when v1 is empty or null
	 */
	NameFilter(String v1) throws IllegalFilterValueException {
		if (v1 == null || v1.equals(""))
			throw new IllegalFilterValueException("");
		_v1 = v1;
	}

	public boolean accept(File f) {
		return f.isFile();
	}

}
