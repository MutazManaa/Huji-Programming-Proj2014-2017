package oop.ex6.filescript.orders;

import java.io.*;
/**
 * An order that compares two files based on their type.
 *
 * @author Mutaz
 *
 */

final class TypeOrder extends TextOrder {

	private static final String SUFFIX_INDICATOR = ".";

	/**
	 * Returns the Type of a file, windows style (three last characters of the
	 * file name)
	 *
	 * @param file
	 *            The file to process.
	 * @return The type of file.
	 * @throws FileNotFoundException
	 *             Thrown when f is not properly initialized.
	 */
	private static String getFileType(File file) throws FileNotFoundException {
		String name = file.getName();
		return name.substring(name.lastIndexOf(SUFFIX_INDICATOR));
	}

	@Override
	public int compareFiles(File file1, File file2)
			throws FileNotFoundException {
		return this.ComapereStrings(getFileType(file1), getFileType(file2));
	}

}
