import ArgsSerializer.*;
import Exceptions.*;
import GameExecutor.*;
import Logger.Logger;

public class Main {
    private static final Logger logger = new Logger("Main");

    public static void main(String[] args) throws InterruptedException {
        // extract parameters
        ArgsSerializer argsSerializer = new ArgsSerializer(args);
        GameArguments gameArguments;
        try {
            gameArguments = argsSerializer.serialize();
        } catch (InvalidGameException | NotEnoughArgumentsException e) {
            logger.error(e.toString());
            Main.printUsage();
            return;
        }

        // run game
        logger.title("Game");
        GameExecutor gameExecutor = new GameExecutor(gameArguments);
        GameExecutorResults gameExecutorResults = gameExecutor.runGame();

        // conclude results
        logger.title("Results");
        gameExecutorResults.print();
    }

    public static void printUsage(){
        logger.info("### For Prisoner’s Dilemma (PD-" + GameType.PD.getValue() +") ###");
        logger.info("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.PD.getValue());

        logger.info("### For Battle of the Sexes (BoS-"+GameType.BoS.getValue()+") ###");
        logger.info("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.BoS.getValue() + " <friction:int>");
    }
}
