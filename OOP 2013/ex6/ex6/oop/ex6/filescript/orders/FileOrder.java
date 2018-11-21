package oop.ex6.filescript.orders;

import java.io.*;

import static oop.ex6.filescript.MyFileScript.DELIMITER;

/**
 * This class implements an order between files.
 *
 * @author Mutaz
 *
 */
public class FileOrder {
	/** an exception String when the given name is not a valid order */
	private static final String BAD_NAME_MESSGE = "The given Order does not exist";

	private static final String REVERSE_STRING = "REVERSE";

	// This is the actual order
	private Order _order;
	//This flag indicates weather the order should be reversed
	private boolean _reverse = false;
	//the name of the order
	private String _orderName;

	/**
	 * Creates a new FileOrder Object based on a given order string.
	 *
	 * @param orderString
	 */
	public FileOrder(String orderString) throws OrderException {

		//check reverse
		if (orderString.endsWith(DELIMITER + REVERSE_STRING)) {
			_reverse = true;
			orderString = orderString.substring(0, orderString.length()
					- (REVERSE_STRING.length() + 1));
		}
		int index = orderString.indexOf(DELIMITER);
		String orderName = "";
		if (index < 0) {
			orderName = orderString;
		} else {
			orderName = orderString.substring(0, index - 1);
		}
		_orderName = orderName;

		switch (orderName) {
		case "abs":
			_order = new AsbOrder();
			break;
		case "type":
			_order = new TypeOrder();
			break;
		case "size":
			_order = new SizeOrder();
			break;
		default:
			throw new UnknownOrderException(BAD_NAME_MESSGE);
		}
	}

	/**
	 * Compares two files and returns the order between them.
	 *
	 * @param file1
	 *            First file to compare.
	 * @param file2
	 *            Second file to compare.
	 * @return A positive number if file1 should be placed after file2.<br>
	 *         0 if file1 and file2 have the same order value.<br>
	 *         A negative number if file1 should be placed before file2.
	 * @throws FileNotFoundException
	 *             Thrown if either file1 or file 2 do not exist
	 * @throws OrderException
	 *             Thrown if an exception was encountered. while determining the
	 *             order.
	 */
	public int compareFiles(File file1, File file2)
			throws FileNotFoundException {
		return _order.compareFiles(file1, file2) * (_reverse ? -1 : 1);
	}

	/**
	 *
	 * @return the name of the order.
	 */
	public String getOrderName() {
		return _orderName;
	}

	/**
	 *
	 * @return the order reverse flag state.
	 */
	public boolean getReverse() {
		return _reverse;
	}

	/**
	 * Sets the reverse flag state for the order
	 *
	 * @param reverse
	 *            the value set reverse flag for.
	 */
	public void setReverse(boolean reverse) {
		_reverse = reverse;
	}
}
