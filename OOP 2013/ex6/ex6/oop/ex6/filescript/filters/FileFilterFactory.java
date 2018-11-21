package oop.ex6.filescript.filters;

import java.io.FileFilter;

import static oop.ex6.filescript.MyFileScript.DELIMITER;

public class FileFilterFactory {
	// incorrect filter name exception text.
	private static final String BAD_NAME = "Incorrect Filter Name";

	private static final String NEGATION_STRING = "NOT";

	/**
	 *
	 * @param filterText
	 * @throws FilterException
	 */
	public static FileFilter getFilter(String filterText, boolean checkNegative)
			throws FilterException {

		if (checkNegative && filterText.endsWith(DELIMITER + NEGATION_STRING)) {
			filterText = filterText.substring(0, filterText.length()
					- (NEGATION_STRING.length() + 1));
			return new NegationFilter(filterText);
		}

		// get filter name
		int index = filterText.indexOf(DELIMITER);
		String filterName = "";
		if (index < 0) {
			filterName = filterText;
			filterText = "";
		} else {
			filterName = filterText.substring(0, index);
			filterText = filterText.substring(index + 1);
		}

		// get values
		String val1 = "";
		String val2 = "";
		index = filterText.indexOf(DELIMITER);
		if (index < 0) // one value
		{
			val1 = filterText;
		} else {
			val1 = filterText.substring(0, index);
			val2 = filterText.substring(index + 1).replace("" + DELIMITER, "");

		}

		switch (filterName) {
		case "greater_than":
			return new GreaterThanFilter(val1);

		case "between":
			return new BetweenFilter(val1, val2);

		case "smaller_than":
			return new SmallerThanFilter(val1);

		case "file":
			return new FileNameFilter(val1);

		case "contains":
			return new ContainsFilter(val1);

		case "prefix":
			return new PrefixFilter(val1);

		case "suffix":
			return new SufixFilter(val1);

		case "writable":
			return new WriteableFilter(val1);

		case "executable":
			return new ExecutableFilter(val1);

		case "hidden":
			return new HiddenFilter(val1);

		case "all":
			return new AllFilter();

		default:
			throw new UnknownFilterException(BAD_NAME);
		}

	}

}
