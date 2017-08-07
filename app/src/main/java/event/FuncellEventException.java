package event;


public class FuncellEventException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FuncellEventException(String detailMessage) {
        super(detailMessage);
    }

    public FuncellEventException(Throwable throwable) {
        super(throwable);
    }

    public FuncellEventException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}