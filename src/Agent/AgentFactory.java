package Agent;

import Agent.BoSAgent.BoSAgent;
import Agent.BoSAgent.BoSAgentSex;
import ArgsSerializer.GameType;
import Mailer.Mailer;
import Agent.PDAgent.PDAgent;

import java.util.List;

public class AgentFactory {
    public static Agent createAgent(GameType gameType, int id, int numberOfAgents, Mailer mailer, List<Integer> neighbors, BoSAgentSex bosAgentSex) {
        if (gameType == null) {
            throw new IllegalArgumentException("GameType cannot be null");
        }

        return switch (gameType) {
            case PD -> new PDAgent(id, numberOfAgents, mailer, neighbors);
            case BoS -> new BoSAgent(id, numberOfAgents, mailer, neighbors, bosAgentSex);
        };
    }
}