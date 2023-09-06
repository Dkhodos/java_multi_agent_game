import ArgsSerializer.*;
import Exceptions.*;
import GameExecutor.*;
import Logger.*;
import ReportMaker.ReportMaker;

/**
 * Main entry point for the game simulation.
 */
public class Main {
    private static final Logger logger = new Logger("Main");

    /**
     * Entry point for the application.
     *
     * @param args Command-line arguments.
     * @throws InterruptedException If the game execution is interrupted.
     */
    public static void main(String[] args) throws InterruptedException {
        /* extract parameters */
        logger.debug("Parse arguments", LogType.Title);
        ArgsSerializer argsSerializer = new ArgsSerializer(args);
        GameArguments gameArguments;
        try {
            gameArguments = argsSerializer.serialize();
        } catch (InvalidGameException | NotEnoughArgumentsException e) {
            logger.error(e.toString());
            Main.printUsage();
            return;
        }
        gameArguments.print();

        /* run game */
        logger.debug("Execute Game", LogType.Title);
        GameExecutor gameExecutor = new GameExecutor(gameArguments);
        GameExecutorResults results = gameExecutor.runGame();

        /* create reports */
        ReportMaker reportMaker = new ReportMaker();
        reportMaker.generateReport(gameArguments.numberOfAgents(), gameArguments.gameType(), gameArguments.fraction(),
                gameArguments.probability(), results.network(), results.audit());

        /* conclude results */
        logger.debug("Game Results", LogType.Title);
        results.print();
    }

    /**
     * Displays the correct usage of the application along with expected command-line arguments.
     */
    public static void printUsage(){
        logger.debug("### For Prisoner’s Dilemma (PD-" + GameType.PD.getValue() +") ###");
        logger.debug("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.PD.getValue());

        logger.debug("### For Battle of the Sexes (BoS-"+GameType.BoS.getValue()+") ###");
        logger.debug("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.BoS.getValue() + " <friction:int>");
    }
}
