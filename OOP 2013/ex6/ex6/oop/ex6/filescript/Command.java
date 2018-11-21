package oop.ex6.filescript;

import java.io.FileFilter;

import oop.ex6.filescript.orders.*;

/**
 * A section in the commands file.
 * 
 * @author Mutaz
 * 
 */
class Command {
	// this section order
	private FileOrder _order;
	// this section filter.
	private FileFilter _filter;

	/**
	 * create new Section Object
	 * 
	 * @param filter
	 *            The Filter to use for this section
	 * @param order
	 *            The order of files to be executed
	 */
	Command(FileFilter filter, FileOrder order) {
		setOrder(order);
		setFilter(filter);
	}

	public FileOrder getOrder() {
		return _order;
	}

	public void setOrder(FileOrder _order) {
		this._order = _order;
	}

	public FileFilter getFilter() {
		return _filter;
	}

	public void setFilter(FileFilter _filter) {
		this._filter = _filter;
	}

}
