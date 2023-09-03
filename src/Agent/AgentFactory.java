package Agent;

import Agent.AgentNetwork.AgentNetwork;
import Agent.BoSAgent.*;
import ArgsSerializer.GameType;
import Audit.Audit;
import Mailer.Mailer;
import Agent.PDAgent.*;

import java.util.List;

public class AgentFactory {

    public static Agent createAgent(GameType gameType, int id, int numberOfAgents, Mailer mailer, Audit audit, List<Integer> neighbors, BoSAgentSex bosAgentSex) {
        if (gameType == null) {
            throw new IllegalArgumentException("GameType cannot be null");
        }

        return switch (gameType) {
            case PD -> new PDAgent(id, numberOfAgents, mailer, audit, neighbors);
            case BoS -> {
                if (bosAgentSex == null) {
                    throw new IllegalArgumentException("BoSAgentSex cannot be null for BoS game type");
                }
                yield new BoSAgent(id, numberOfAgents, mailer, audit, neighbors, bosAgentSex);
            }
        };
    }

    public static Agent[] createAgents(GameType gameType, int numberOfAgents, Mailer mailer, Audit audit, AgentNetwork network, int fraction) {
        Agent[] agents = new Agent[numberOfAgents];
        int createdAgents = 0;

        for (int i = 0; i < numberOfAgents; i++) {
            List<Integer> neighbors = network.getNeighbors(i);

            agents[i] = switch (gameType) {
                case PD -> createAgent(GameType.PD, i, numberOfAgents, mailer, audit,neighbors, null);
                case BoS -> {
                    BoSAgentSex bosAgentSex = createdAgents < fraction ? BoSAgentSex.WIFE : BoSAgentSex.HUSBAND;
                    createdAgents++;
                    yield createAgent(GameType.BoS, i, numberOfAgents, mailer, audit, neighbors, bosAgentSex);
                }
            };

            registerWithMailer(mailer, i);
        }

        return agents;
    }

    private static void registerWithMailer(Mailer mailer, int agentId) {
        mailer.register(agentId);
    }
}
