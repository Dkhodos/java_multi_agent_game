package ArgsSerializer;

import Logger.Logger;

/**
 * Represents the parsed and validated game parameters.
 */
public record GameArguments(int numberOfAgents, double probability, GameType gameType, float fraction) {
    private static final String TO_STRING_PD_TEMPLATE = "%d %s %,.1f";
    private static final String TO_STRING_BoS_TEMPLATE = "%d %s %,.1f %,.1f";

    // Format string for logging the parsed arguments
    private final static String FORMATTED_ARGUMENTS_STRING = "Game arguments: numberOfAgents: %d, probability: %,.1f, gameType: %s, fraction: %s";

    // Logger instance for logging messages related to GameArguments
    private static final Logger logger = new Logger("GameArguments");

    /**
     * Constructor for cases where the fraction is not specified.
     *
     * @param numberOfAgents The total number of agents.
     * @param probability The probability of forming a connection between agents.
     * @param gameType The type of game to be played.
     */
    public GameArguments(int numberOfAgents, double probability, GameType gameType) {
        this(numberOfAgents, probability, gameType, 0);
    }

    /**
     * Logs the parsed and validated game parameters.
     */
    public void print(){
        logger.info(String.format(FORMATTED_ARGUMENTS_STRING, numberOfAgents, probability, gameType, fraction));
    }

    @Override
    public String toString() {
        if(this.gameType() == GameType.PD){
            return String.format(TO_STRING_PD_TEMPLATE, numberOfAgents, gameType.getValue(), probability);
        }
        return String.format(TO_STRING_BoS_TEMPLATE, numberOfAgents, gameType.getValue(), probability, fraction);
    }
}

