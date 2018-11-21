package oop.ex6.filescript.filters;

import java.io.File;

/**
 * A filter based on a file hidden property.
 * 
 * @author Mutaz
 * 
 */
final class HiddenFilter extends AttributeFilter {

	/**
	 * Creates a new Hidden filter.
	 * 
	 * @param v1
	 *            the status indicator string for the filter.
	 * @throws IllegalFilterValueException
	 */
	HiddenFilter(String v1) throws IllegalFilterValueException {
		super(v1);
	}

	@Override
	public boolean accept(File file) {
		return super.accept(file) && file.isHidden() == _yesno;
	}

}
