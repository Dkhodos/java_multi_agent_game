package ArgsSerializer;

public enum GameType {
    PD(1), BoS(2),;

    private final int value;
    GameType(int i) {
        this.value = i;
    }

    public int getValue() {
         return value;
    }
}