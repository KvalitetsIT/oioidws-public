package dk.sds.samlh.model;

public class ValidationException extends Exception {
	private static final long serialVersionUID = -3164660649036367824L;

	public ValidationException(String message) {
		super(message);
	}
	
	public ValidationException(String message, Exception e) {
		super(message, e);
	}
}
