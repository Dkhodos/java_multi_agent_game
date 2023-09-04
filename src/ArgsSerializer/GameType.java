package ArgsSerializer;

/**
 * Enumerates the types of games available.
 */
public enum GameType {
    PD(1), BoS(2),;

    // Integer representation of the game type
    private final int value;

    /**
     * Constructs a game type with its integer representation.
     *
     * @param i The integer representation of the game type.
     */
    GameType(int i) {
        this.value = i;
    }

    /**
     * Retrieves the integer representation of the game type.
     *
     * @return The integer representation.
     */
    public int getValue() {
        return value;
    }
}