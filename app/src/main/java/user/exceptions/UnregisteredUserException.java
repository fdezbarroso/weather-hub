package user.exceptions;

public class UnregisteredUserException extends Exception { 
	public UnregisteredUserException() {
		
	}
	
	public UnregisteredUserException(String message) {
		super (message);
	}
	
	public UnregisteredUserException(Throwable cause) {
        super (cause);
    }

    public UnregisteredUserException(String message, Throwable cause) {
        super (message, cause);
    }
	
}

