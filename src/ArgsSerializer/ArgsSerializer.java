package ArgsSerializer;

import Exceptions.InvalidGameException;
import Exceptions.NotEnoughArgumentsException;

public class ArgsSerializer {
    static final int MIN_NUMBER_ARGS = 3;
    private final String[] arguments;

    public ArgsSerializer(String[] arguments) {
        this.arguments = arguments;
    }

    public GameArguments serialize() throws NotEnoughArgumentsException, InvalidGameException {
        if(arguments.length < MIN_NUMBER_ARGS){
            throw new NotEnoughArgumentsException(MIN_NUMBER_ARGS, arguments.length);
        }

        int numberOfAgents = Integer.valueOf(arguments[0]).intValue();
        double probability = Double.valueOf(arguments[1]).doubleValue();
        int game = Double.valueOf(arguments[2]).intValue();

        if(game == (GameType.PD).getValue()){
            return new GameArguments(numberOfAgents, probability, GameType.PD);
        }

        if(game == (GameType.BoS).getValue()){
            if(arguments.length < MIN_NUMBER_ARGS + 1){
                throw new NotEnoughArgumentsException(MIN_NUMBER_ARGS + 1, arguments.length);
            }

            int friction = Integer.valueOf(arguments[3]).intValue();

            return new GameArguments(numberOfAgents, probability, GameType.BoS, friction);
        }

        throw new InvalidGameException(game);
    }
}
