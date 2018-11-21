package oop.ex6.filescript.filters;

import java.io.File;

/**
 * Filters files by the ability to execute them.
 *
 * @author Mutaz
 *
 */
final class ExecutableFilter extends AttributeFilter {

	/**
	 * Creates a new Executable filter
	 *
	 * @param v1
	 *            the status indicator string for the filter.
	 * @throws IllegalFilterValueException
	 *             thrown when v1 is not a YES or NO.
	 */
	ExecutableFilter(String v1) throws IllegalFilterValueException {
		super(v1);
	}

	@Override
	public boolean accept(File f) {
		return super.accept(f) && f.canExecute() == _yesno;
	}

}
