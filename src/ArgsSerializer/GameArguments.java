package ArgsSerializer;

public record GameArguments(int numberOfAgents, double probability, GameType gameType, int fraction) {
    public GameArguments(int numberOfAgents, double probability, GameType gameType) {
        this(numberOfAgents, probability, gameType, 0);
    }
}
