package Exceptions;

public class InvalidGameException extends Exception {
    public InvalidGameException(int game){
        super("Unrecognized game type " + game);
    }
}