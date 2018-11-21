package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Filters files who's name contains a given string.
 *
 * @author Mutaz
 *
 */
final class ContainsFilter extends NameFilter {

	/**
	 * Creates a new Contains filter.
	 *
	 * @param v1
	 *            the String value to test.
	 */
	ContainsFilter(String v1) throws IllegalFilterValueException {
		super(v1);
	}

	/**
	 * Checks if the file's name contains the string.
	 *
	 * @param file
	 *            The file to test
	 * @return True in value1 is a substring of f's name.
	 *
	 */
	@Override
	public boolean accept(File file) {
		return super.accept(file) && file.getName().indexOf(_v1) > -1;

	}

}
