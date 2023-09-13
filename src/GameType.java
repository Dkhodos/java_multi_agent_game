/**
 * Enumerates the types of games available.
 */
public enum GameType {
    PD("PD"), BoS("BoS"),;

    // Integer representation of the game type
    private final String value;

    /**
     * Constructs a game type with its integer representation.
     *
     * @param value The string representation of the game type.
     */
    GameType(String value) {
        this.value = value;
    }

    /**
     * Retrieves the integer representation of the game type.
     *
     * @return The integer representation.
     */
    public String getValue() {
        return value;
    }
}