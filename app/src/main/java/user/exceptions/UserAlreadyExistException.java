package user.exceptions;

public class UserAlreadyExistException extends Exception{ 
	public UserAlreadyExistException() {
		
	}
	
	public UserAlreadyExistException(String message) {
		super (message);
	}
	
	public UserAlreadyExistException(Throwable cause) {
        super (cause);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super (message, cause);
    }

}