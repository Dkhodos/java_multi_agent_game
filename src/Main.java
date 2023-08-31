import ArgsSerializer.*;
import Exceptions.*;
import GameExecutor.*;
import Logger.Logger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = new Logger("Main");

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
        GameExecutor gameExecutor = new GameExecutor(gameArguments);
        GameExecutorResults gameExecutorResults = gameExecutor.runGame();

        gameExecutorResults.print();
    }

    public static void printUsage(){
        System.out.println("### For Prisoner’s Dilemma (PD-" + GameType.PD.getValue() +") ###");
        System.out.println("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.PD.getValue());

        System.out.println("### For Battle of the Sexes (BoS-"+GameType.BoS.getValue()+") ###");
        System.out.println("Usage: java Main <number_of_agents:int> <probability_of_connection:double> "
                + GameType.BoS.getValue() + " <friction:int>");
    }
}
