import ArgsSerializer.*;
import GameExecutor.*;
import Logger.*;

public class Main {
    private static final int NUMBER_OF_GAMES = 10;

    private static final Logger logger = new Logger("Main");

    public static void main(String[] args) throws InterruptedException {
        logger.title("Randomize arguments");
        GameArguments gameArguments = GameArguments.getRandomArguments(10, 0.5);

        logger.title("Executing " + NUMBER_OF_GAMES +" Random Games");
        int totalRawRounds = 0;
        int totalSW = 0;
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            logger.info("Starting Game No." + (i + 1) + ": " + gameArguments.gameType());
            GameExecutor gameExecutor = new GameExecutor(gameArguments);
            GameExecutorResults results = gameExecutor.runGame();

            totalRawRounds += results.totalRounds();
            totalSW += results.totalGain();
        }

        logger.title("Summarizing results");
        logger.info("Average number of rounds: " + (totalRawRounds / NUMBER_OF_GAMES));
        logger.info("Total SW: " + totalSW);
    }
}
