package oop.ex6.filescript.orders;

/**
 * An abstract class to be inherited by orders who uses text comparison.
 *
 * @author Mutaz
 *
 */
abstract class TextOrder implements Order

{

	/**
	 * Compares Two Strings. by lexicographic order.
	 *
	 * @param str1
	 *            First string to compare.
	 * @param str2
	 *            Second string to compare
	 * @return A positive number if the file represented by str1 should be
	 *         placed after the file represented by str2.<br>
	 *         0 if the file represented by str1 and the file represented by
	 *         str1 have the same order value.<br>
	 *         A negative number if the file represented by str1 should be
	 *         placed before the file represented by str1.
	 */
	protected int ComapereStrings(String str1, String str2) {
		return str1.compareTo(str2);
	}
}
