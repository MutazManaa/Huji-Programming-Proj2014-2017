package oop.ex6.filescript.orders;

import java.io.*;

/**
 * An order that compares two files based on their absolute path.
 *
 * @author Mutaz
 *
 */
final class AsbOrder extends TextOrder {

	public int compareFiles(File file1, File file2)
			throws FileNotFoundException {

		return ComapereStrings(file1.getAbsolutePath(), file2.getAbsolutePath());

	}

}
