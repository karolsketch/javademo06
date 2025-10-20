package exception;

public class AppException extends RuntimeException {
	public AppException(String m) {
		super(m);
	}

	public AppException(String m, Throwable t) {
		super(m, t);
	}
}
