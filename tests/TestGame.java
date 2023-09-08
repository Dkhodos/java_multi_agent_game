import ArgsSerializer.*;
import GameExecutor.*;
import Logger.Logger;
import Settings.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestGame {
    private record Range(double min, double max){}

    private static final int NUMBER_OF_GAMES = 100;

    private static final Logger logger = new Logger("TestGame");

    @BeforeEach
    public void setup() {
        Settings.AGENT_SLEEP_TIME = 1;
        Settings.DEBUG = false;
    }

    @Disabled
    @Test
    public void testFullPDGame() throws InterruptedException {
        GameArguments gameArguments  = new GameArguments(500, 0.5f, GameType.PD);

        runGame(gameArguments, new Range(2,3), new Range(400, 700));
    }

    @Disabled
    @Test
    public void testFullBoSGame() throws InterruptedException {
        GameArguments gameArguments  = new GameArguments(500, 0.5f, GameType.BoS, 0.3f);

        runGame(gameArguments, new Range(3,5), new Range(200, 600));
    }

    private void runGame(GameArguments gameArguments, Range roundRange, Range SWRange) throws InterruptedException {
        double totalRawRounds = 0;
        double totalSW = 0;

        logger.info("Test started for: " + gameArguments.gameType().getValue() + "\n");

        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            logger.info("Running Game (" + i + " / " + NUMBER_OF_GAMES + ")");

            GameExecutor gameExecutor = new GameExecutor(gameArguments);
            GameExecutorResults results = gameExecutor.runGame();

            // accumulate game reports
            totalRawRounds += results.totalRounds();
            totalSW += results.avgGain();
        }

        double averageRound = totalRawRounds / NUMBER_OF_GAMES;
        double averageSW = totalSW / NUMBER_OF_GAMES;

        validateInRange(roundRange.min(),roundRange.max(), averageRound, "averageRound");
        validateInRange(SWRange.min(),SWRange.max(), averageSW, "averageSW");
    }

    private void validateInRange(double min, double max, double score, String name){
        String errorMessage = name + " exceeded range of " + min + "-" + max + ", value: " + score;
        assertTrue(score >= min && score <= max, errorMessage);
    }
}
