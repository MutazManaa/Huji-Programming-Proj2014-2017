package oop.ex6.filescript;

import java.io.*;

import oop.ex6.filescript.filters.FileFilterFactory;
import oop.ex6.filescript.orders.FileOrder;

/**
 * this class contains the main function of the project
 *
 * @author Mutaz
 *
 */
public abstract class MyFileScript {

	public static final char DELIMITER = '#';
	public static final String WARNING_MESSAGE = "Warning in line ";

	private static final String ERROR_MESSAGE = "ERROR";

	private static final String DEFAULT_FILTER = "all";
	private static final String DEFAULT_ORDER = "abs";

	public static void main(String[] args) {
		try {
			File source = new File(args[0]);
			Parser parser = new Parser(args[1]);
			Command section;
			do {
				section = parser.nextSection();

				if (section.getFilter() == null) {
					section.setFilter(FileFilterFactory.getFilter(
							DEFAULT_FILTER, true));
				}
				if (section.getOrder() == null) {
					section.setOrder(new FileOrder(DEFAULT_ORDER));
				}
				File[] files = source.listFiles(section.getFilter());

				FileOrder DefaultOrder = new FileOrder(DEFAULT_ORDER);
				DefaultOrder.setReverse(section.getOrder().getReverse());
				FileSort.mergeSort(files, DefaultOrder);
				if (!DefaultOrder.getOrderName().equals(
						section.getOrder().getOrderName())) {
					FileSort.mergeSort(files, section.getOrder());
				}
				for (int i = 0; i < files.length; i++) {
					System.out.println(files[i].getName());
				}
				section = parser.nextSection();
			}while (section != null);

		} catch (IOException | Type2Error e) {
			System.out.println(ERROR_MESSAGE);
		} catch (Type1Error e) {
			//the computer should never get to this line, as the only calls
			//for FileOrder and FileFilterFactory.getFilters are with default,
			//legal values
		}
	}

}
