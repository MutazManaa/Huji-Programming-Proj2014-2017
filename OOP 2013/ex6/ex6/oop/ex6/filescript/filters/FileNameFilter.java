package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Filters files by their names.
 *
 * @author Mutaz
 *
 */
class FileNameFilter extends NameFilter {

	/**
	 * Creates a new file name filter.
	 *
	 * @param name
	 *            the name to match.
	 */
	FileNameFilter(String name) throws IllegalFilterValueException {
		super(name);
	}

	/**
	 * @param file
	 *            The file to test.
	 * @return True if the name of f equals the filter value.
	 */
	public boolean accept(File file) {

		return super.accept(file) && file.getName().equals(_v1);
	}

}
