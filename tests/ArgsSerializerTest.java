import Exceptions.InvalidGameException;
import Exceptions.NotEnoughArgumentsException;
import org.junit.jupiter.api.Test;
import ArgsSerializer.ArgsSerializer;
import ArgsSerializer.GameType;

import static org.junit.jupiter.api.Assertions.*;

class ArgsSerializerTest {

    @Test
    void testNoArguments() {
        ArgsSerializer serializer = createSerializer();
        assertThrows(NotEnoughArgumentsException.class, serializer::serialize);
    }

    @Test
    void testTwoArguments() {
        ArgsSerializer serializer = createSerializer("1", "0.5");
        assertThrows(NotEnoughArgumentsException.class, serializer::serialize);
    }

    @Test
    void testValidPDGameWithThreeArguments() {
        ArgsSerializer serializer = createSerializer("1", "0.5", getGameTypeString(GameType.PD));
        assertDoesNotThrow(serializer::serialize);
    }

    @Test
    void testInvalidBoSGameWithThreeArguments() {
        ArgsSerializer serializer = createSerializer("1", "0.5", getGameTypeString(GameType.BoS));
        assertThrows(NotEnoughArgumentsException.class, serializer::serialize);
    }

    @Test
    void testValidBoSGameWithFourArguments() {
        ArgsSerializer serializer = createSerializer("1", "0.5", getGameTypeString(GameType.BoS), "2");
        assertDoesNotThrow(serializer::serialize);
    }

    @Test
    void testInvalidGameType() {
        ArgsSerializer serializer = createSerializer("1", "0.5", "3");
        assertThrows(InvalidGameException.class, serializer::serialize);
    }

    private ArgsSerializer createSerializer(String... args) {
        return new ArgsSerializer(args);
    }

    private String getGameTypeString(GameType gameType){
        return String.valueOf(gameType.getValue());
    }
}
