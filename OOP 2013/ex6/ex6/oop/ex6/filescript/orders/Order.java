package oop.ex6.filescript.orders;

import java.io.*;

/**
 * This interface represent an order between files
 *
 * @author Mutaz
 *
 */
interface Order {
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
	 *             Thrown if either file1 or file 2 do not exist.
	 */
	public int compareFiles(File file1, File file2)
			throws FileNotFoundException;

}
