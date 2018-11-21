package oop.ex6.filescript;

import java.io.*;
import oop.ex6.filescript.filters.*;
import oop.ex6.filescript.orders.*;

/**
 * helper class for parsing commands file.
 *
 * @author Mutaz
 *
 */
public class Parser {
	private BufferedReader reader;

	private int _lineNum;
	private String _line = "";

	//indicate the last section has no order, in that case the "FILTER" string
	//was already read and we do not expect it
	private boolean noOrder=false;

	private static final String FILTER_HEADER = "FILTER";
	private static final String ORDER_HEADER = "ORDER";

	Parser(String commandFile) throws IOException {
		reader = new BufferedReader(new FileReader(commandFile));
		_line = reader.readLine();
		_lineNum = 1;

	}

	public Command nextSection() throws IOException, CommandFileException {
		FileFilter filter;
		FileOrder order;
		if (_line == null)
			return null;
		try {
			if (_line.equals(FILTER_HEADER)) {
				_line = reader.readLine();
				_lineNum++;
			} else
				if (! noOrder)
					throw new CommandFileException();
			try {
				filter = FileFilterFactory.getFilter(_line, true);
			} catch (FilterException e) {
				filter = null;
				System.out.println(MyFileScript.WARNING_MESSAGE + _lineNum);
			}
			_line = reader.readLine();
			_lineNum++;
			if (_line.equals(ORDER_HEADER)) {
				_line = reader.readLine();
				_lineNum++;
			}
			if (_line == null) {
				order = null;
				noOrder=true;
			} else if (!_line.equals(FILTER_HEADER)) {
				try {
					noOrder=false;
					order = new FileOrder(_line);
				} catch (OrderException e) {
					System.out.println(MyFileScript.WARNING_MESSAGE + _lineNum);
					order = null;
				}
				_line = reader.readLine();
				_lineNum++;
			} else { //line equals FILTER_HEADER - no order in this section
				order = null;
			}
		} catch (NullPointerException e) {
			throw new CommandFileException();
		}
		return new Command(filter, order);

	}
}
