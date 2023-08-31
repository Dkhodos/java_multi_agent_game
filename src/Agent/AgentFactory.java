package Agent;

import ArgsSerializer.GameType;
import Mailer.Mailer;
import java.util.List;

public class AgentFactory {
    public static Agent createAgent(GameType gameType, int id, Mailer mailer, List<Integer> neighbors, int fraction) {
        if (gameType == null) {
            throw new IllegalArgumentException("GameType cannot be null");
        }

        return switch (gameType) {
            case PD -> new PDAgent(id, mailer, neighbors);
            case BoS -> new BoSAgent(id, mailer, neighbors, fraction);
        };
    }
}