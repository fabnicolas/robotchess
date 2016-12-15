package exceptions;

public class InsufficientEnergyException extends Exception {
	private static final long serialVersionUID = 1L;

	public InsufficientEnergyException(String message) {
        super(message);
    }
}
