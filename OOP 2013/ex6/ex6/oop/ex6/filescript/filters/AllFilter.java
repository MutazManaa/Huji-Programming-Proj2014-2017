package oop.ex6.filescript.filters;

import java.io.*;

/**
 * This class implements an empty file filter. accepting all the files that
 * exist on the disk.
 *
 * @author Mutaz
 *
 */
public final class AllFilter implements FileFilter {

	/**
	 * @param file
	 *            the file to test.
	 * @return True for all files.
	 *
	 */
	public boolean accept(File file) {
		return file.isFile();
	}

}
