package exceptions;

public class CriticalStatusException extends Exception{
	private static final long serialVersionUID = 1L;

	public CriticalStatusException(String message) {
        super(message);
    }
}
