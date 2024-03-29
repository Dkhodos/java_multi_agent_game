package ArgsSerializer;

import Exceptions.InvalidGameException;
import Exceptions.NotEnoughArgumentsException;

public class ArgsSerializer {

    // Min number of args for a game
    static final int MIN_NUMBER_ARGS = 3;
    private final String[] arguments;

    /**
     * Initializes the ArgsSerializer with the provided command-line arguments.
     *
     * @param arguments Command-line arguments to be serialized.
     */
    public ArgsSerializer(String[] arguments) {
        this.arguments = arguments;
    }


    /**
     * Serializes and validates the command-line arguments to construct game parameters.
     *
     * @return A GameArguments object containing the parsed and validated game parameters.
     * @throws NotEnoughArgumentsException If there are insufficient arguments provided.
     * @throws InvalidGameException If an invalid game type is provided.
     */
    public GameArguments serialize() throws NotEnoughArgumentsException, InvalidGameException {
        if(arguments.length < MIN_NUMBER_ARGS){
            throw new NotEnoughArgumentsException(MIN_NUMBER_ARGS, arguments.length);
        }

        int numberOfAgents = Integer.parseInt(arguments[0]);
        String game = arguments[1];
        double probability = Double.parseDouble(arguments[2]);


        if(game.equals(GameType.PD.getValue())){
            return new GameArguments(numberOfAgents, probability, GameType.PD);
        }

        if(game.equals(GameType.BoS.getValue())){
            if(arguments.length < MIN_NUMBER_ARGS + 1){
                throw new NotEnoughArgumentsException(MIN_NUMBER_ARGS + 1, arguments.length);
            }

            float fraction = Float.parseFloat(arguments[3]);
            return new GameArguments(numberOfAgents, probability, GameType.BoS, fraction);
        }

        throw new InvalidGameException(game);
    }
}
