package user.exceptions;


public class IncorrectEmailConfirmationException extends Exception{
	public IncorrectEmailConfirmationException() {
		
	}
	
	public IncorrectEmailConfirmationException(String message) {
		super (message);
	}
	
	public IncorrectEmailConfirmationException(Throwable cause) {
        super (cause);
    }

    public IncorrectEmailConfirmationException(String message, Throwable cause) {
        super (message, cause);
    }

}

