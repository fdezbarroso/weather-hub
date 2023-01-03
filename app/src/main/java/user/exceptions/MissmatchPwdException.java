package user.exceptions;

public class MissmatchPwdException extends Exception{
	public MissmatchPwdException() {
		
	}
	
	public MissmatchPwdException(String message) {
		super (message);
	}
	
	public MissmatchPwdException(Throwable cause) {
        super (cause);
    }

    public MissmatchPwdException(String message, Throwable cause) {
        super (message, cause);
    }
}
