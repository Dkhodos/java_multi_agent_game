package ArgsSerializer;

import Logger.Logger;

import java.util.Random;

/**
 * Represents the parsed and validated game parameters.
 */
public record GameArguments(int numberOfAgents, double probability, GameType gameType, int fraction) {
    private static final Random random = new Random();

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

    public static GameArguments getRandomArguments(int numberOfAgents, double probability){
        int fraction = 0;
        GameType gameType = random.nextBoolean() ? GameType.PD : GameType.BoS;

        if(gameType == GameType.BoS){
            fraction = random.nextInt(0, numberOfAgents);
        }

        return new GameArguments(numberOfAgents, probability, gameType, fraction);
    }
}

