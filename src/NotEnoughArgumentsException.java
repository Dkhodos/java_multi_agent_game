public class NotEnoughArgumentsException extends Exception {
    public NotEnoughArgumentsException(int expected, int args){
        super("Expected " + expected + " arguments for program but received " + args + ".");
    }
}