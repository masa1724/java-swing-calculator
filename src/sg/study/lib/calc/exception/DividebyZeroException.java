package sg.study.lib.calc.exception;

public class DividebyZeroException extends RuntimeException {
	private static final long serialVersionUID = 5690447968906067688L;

	public DividebyZeroException() {
		super("0èúéZÇÕÇ≈Ç´Ç‹ÇπÇÒÅB");
	}

	public DividebyZeroException(String message) {
		super(message);
	}

	public DividebyZeroException(String message, Throwable cause) {
		super(message, cause);
	}

	public DividebyZeroException(Throwable cause) {
		super(cause);
	}
}
