package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Filters files whose name ends with given String.
 *
 * @author Mutaz
 *
 */
final class SufixFilter extends NameFilter {

	/**
	 * Creates a new name suffix filter.
	 *
	 * @param name
	 *            the text to match to the end of the file name.
	 */
	SufixFilter(String name) throws IllegalFilterValueException {
		super(name);
	}

	/**
	 * @param file
	 *            The file to test.
	 * @return True if the name of f end with the filter value.
	 */
	@Override
	public boolean accept(File file) {
		return super.accept(file) && file.getName().endsWith(_v1);
	}

}
