package oop.ex6.filescript.filters;

import java.io.*;

/**
 * this class implements a filter that negate another filters
 *
 * @author Mutaz
 *
 */
public class NegationFilter implements FileFilter {

	private FileFilter _filter;

	public NegationFilter(String filterText) throws FilterException {
		_filter = FileFilterFactory.getFilter(filterText, false);
	}

	public boolean accept(File file) {
		return !_filter.accept(file);
	}

}
