package user.exceptions;

public class MissingArgumentsException extends Exception{
	public MissingArgumentsException() {
		
	}
	
	public MissingArgumentsException(String message) {
		super (message);
	}
	
	public MissingArgumentsException(Throwable cause) {
        super (cause);
    }

    public MissingArgumentsException(String message, Throwable cause) {
        super (message, cause);
    }
}
