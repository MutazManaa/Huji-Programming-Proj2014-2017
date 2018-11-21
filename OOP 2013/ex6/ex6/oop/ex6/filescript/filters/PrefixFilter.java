package oop.ex6.filescript.filters;

import java.io.File;

/**
 * A filter that accepts fils that starts with a given string
 *
 * @author Mutaz
 *
 */
class PrefixFilter extends NameFilter {

	/**
	 * Creates a new prefix filter
	 *
	 * @param val
	 *            the text to match to start of the file name.
	 */
	PrefixFilter(String val) throws IllegalFilterValueException {
		super(val);
	}

	/**
	 * @param file
	 *            The file to test.
	 * @return True if the name of f begins with the filter value.
	 */
	@Override
	public boolean accept(File file) {
		return super.accept(file) && file.getName().startsWith(_v1);

	}

}
