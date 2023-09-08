import ArgsSerializer.*;
import Exceptions.InvalidGameException;
import Exceptions.NotEnoughArgumentsException;
import GameExecutor.*;
import Logger.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final String OUTPUTS_FOLDER  = System.getProperty("user.dir") + "/outputs/";
    private static final int NUMBER_OF_GAMES = 1;

    private static final Logger logger = new Logger("Main");

    public static void main(String[] args) throws InterruptedException {
        logger.title("Parse arguments");
        ArgsSerializer argsSerializer = new ArgsSerializer(args);
        GameArguments gameArguments;
        try {
            gameArguments = argsSerializer.serialize();
        } catch (InvalidGameException | NotEnoughArgumentsException e) {
            logger.error(e.toString());
            printUsage();
            return;
        }
        gameArguments.print();

        logger.title("Executing " + NUMBER_OF_GAMES +" Random Games");
        double totalRawRounds = 0;
        double totalSW = 0;

        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            logger.info("Starting Game No." + (i + 1));

            // execute game i with the random gameArguments
            GameExecutor gameExecutor = new GameExecutor(gameArguments);
            GameExecutorResults results = gameExecutor.runGame();

            // accumulate game reports
            totalRawRounds += results.totalRounds();
            totalSW += results.totalGain();

            // print single game results (debug)
            printSingleGameResults(results, i+1);
        }

        logger.title("Summarizing results");
        reportResults(gameArguments, totalRawRounds, totalSW);
    }

    static private void printSingleGameResults(GameExecutorResults results, int gameNumber){
        String SINGE_GAME_REPORT_TEMPLATE = "Starting Game No.%d Summary: rounds: %d, SW: %d\n";
        String game_message = String.format(SINGE_GAME_REPORT_TEMPLATE, gameNumber, results.totalRounds(), results.totalGain());
        logger.debug(game_message);
    }

    /**
     * Displays the correct usage of the application along with expected command-line arguments.
     */
    public static void printUsage(){
        logger.info("### For Prisoner’s Dilemma (PD-" + GameType.PD.getValue() +") ###");
        logger.info("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.PD.getValue());

        logger.info("### For Battle of the Sexes (BoS-"+GameType.BoS.getValue()+") ###");
        logger.info("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.BoS.getValue() + " <fraction:int>");
    }

    private static void reportResults(GameArguments gameArguments, double totalRawRounds, double totalSW){
        String numIterations = "Num_Iterations - " + (totalRawRounds / NUMBER_OF_GAMES);
        String SW = "SW - " + (totalSW / NUMBER_OF_GAMES);

        logger.info(numIterations);
        logger.info(SW);

        String output = "// input parameters - " + gameArguments.toString() + "\n\n" + numIterations + "\n" + SW + "\n";

        try {
            Files.write(Paths.get(OUTPUTS_FOLDER + gameArguments.gameType() + ".txt"), output.getBytes());
        } catch (IOException e) {
            logger.error("File to write to output file");
            e.printStackTrace();
        }
    }
}
