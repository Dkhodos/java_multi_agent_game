public class InvalidGameException extends Exception {
    public InvalidGameException(String game){
        super("Unrecognized game type " + game);
    }
}