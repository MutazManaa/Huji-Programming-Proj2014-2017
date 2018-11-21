package oop.ex6.filescript.orders;

public class UnknownOrderException extends OrderException {
	private static final long serialVersionUID = 2834674260708117339L;

	public UnknownOrderException(String message) {
		super(message);
	}
}
