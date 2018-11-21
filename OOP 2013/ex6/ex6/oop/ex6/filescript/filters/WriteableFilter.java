package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Filters files by the ability of the user to write to them.
 *
 * @author Mutaz
 *
 */
final class WriteableFilter extends AttributeFilter {

	/**
	 * Creates a new Writable state filter.
	 *
	 * @param yesno
	 *            the status indicator string for the filter.
	 * @throws IllegalFilterValueException
	 *             thrown when v1 is not a YES or NO.
	 */
	WriteableFilter(String yesno) throws IllegalFilterValueException {
		super(yesno);
	}

	@Override
	public boolean accept(File file) {

		return super.accept(file) && file.canWrite() == _yesno;
	}

}
