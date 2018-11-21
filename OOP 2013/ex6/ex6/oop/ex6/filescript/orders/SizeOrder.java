package oop.ex6.filescript.orders;

import java.io.*;
/**
 * An order that compares two files based on their size.
 *
 * @author Mutaz
 *
 */
final class SizeOrder implements Order {

	@Override
	public int compareFiles(File file1, File file2)
			throws FileNotFoundException {
		if (file1.length() > file2.length()) {
			return 1;
		} else if (file1.length() == file2.length()) {
			return 0;
		} else {
			return -1;
		}
	}

}
