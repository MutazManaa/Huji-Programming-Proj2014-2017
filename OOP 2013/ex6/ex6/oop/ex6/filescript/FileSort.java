package oop.ex6.filescript;

import java.io.*;
import oop.ex6.filescript.orders.*;

/**
 * This class sorts a File array using quick sort algorithm and Order for
 * comparing.
 *
 * @author Mutaz
 *
 */
public abstract class FileSort {

	/**
	 * sort the file array using a given file order
	 *
	 * @param fileList
	 *            the array to sort.
	 * @param order
	 *            the order to sort by.
	 * @throws FileNotFoundException
	 *             Thrown if any member of A is not properly Initialized.
	 */
	public static void mergeSort(File[] fileList, FileOrder order)
			throws FileNotFoundException {
		mergeSort(fileList, 0, fileList.length - 1, order);
	}

	private static void mergeSort(File[] A, int bottom, int top, FileOrder order)
			throws FileNotFoundException {
		if (top > bottom) {
			int middle = (top + bottom) / 2;
			mergeSort(A, bottom, middle, order);
			mergeSort(A, middle + 1, top, order);
			merge(A, bottom, middle, top, order);
		}
	}

	/**
	 * merges two parts in an array by an order.
	 *
	 * @param A
	 *            The working array
	 * @param start
	 *            the bottom index to merge.
	 * @param middle
	 *            the middle index to merge.
	 * @param end
	 *            the top index to merge
	 * @param order
	 *            the order to merge by.
	 * @throws FileNotFoundException
	 */
	private static void merge(File[] A, int start, int middle, int end,
			FileOrder order) throws FileNotFoundException {
		int leftSize = middle - start + 1;
		int rightSize = end - middle;

		File[] left = new File[leftSize + 1];
		File[] right = new File[rightSize + 1];

		for (int i = 0; i < leftSize; i++) {
			left[i] = A[start + i];
		}

		for (int i = 0; i < rightSize; i++) {
			right[i] = A[middle + i + 1];
		}

		int rIndex = 0;
		int lIndex = 0;

		for (int k = start; k <= end; k++) {
			if (left[lIndex] == null) {
				A[k] = right[rIndex];
				rIndex++;
			} else if (right[rIndex] == null) {
				A[k] = left[lIndex];
				lIndex++;
			} else if (order.compareFiles(left[lIndex], right[rIndex]) <= 0) {
				A[k] = left[lIndex];
				lIndex++;
			} else {
				A[k] = right[rIndex];
				rIndex++;
			}

		}
	}
}
