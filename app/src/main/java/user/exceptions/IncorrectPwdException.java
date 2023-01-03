package user.exceptions;

public class IncorrectPwdException extends Exception{ 
	public IncorrectPwdException() {
		
	}
	
	public IncorrectPwdException(String message) {
		super (message);
	}
	
	public IncorrectPwdException(Throwable cause) {
        super (cause);
    }

    public IncorrectPwdException(String message, Throwable cause) {
        super (message, cause);
    }

}
