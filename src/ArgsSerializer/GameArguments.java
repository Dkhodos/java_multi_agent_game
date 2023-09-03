package ArgsSerializer;

import Logger.Logger;

public record GameArguments(int numberOfAgents, double probability, GameType gameType, int fraction) {
    private final static String FORMATTED_ARGUMENTS_STRING = "Parsed arguments: numberOfAgents: %d, probability: %,.1f, gameType: %s, fraction: %s";
    private static final Logger logger = new Logger("GameArguments");
    public GameArguments(int numberOfAgents, double probability, GameType gameType) {
        this(numberOfAgents, probability, gameType, 0);
    }

    public void print(){
        logger.info(String.format(FORMATTED_ARGUMENTS_STRING, numberOfAgents, probability, gameType, fraction));
    }
}
