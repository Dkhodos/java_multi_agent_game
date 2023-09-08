package Agent;

import Agent.AgentNetwork.AgentNetwork;
import Agent.BoSAgent.*;
import ArgsSerializer.GameType;
import Audit.Audit;
import Mailer.Mailer;
import Agent.PDAgent.*;

import java.util.List;

/**
 * Factory class responsible for creating Agent instances based on game type and other parameters.
 */
public class AgentFactory {

    /**
     * Creates an agent based on the provided game type and parameters.
     *
     * @param gameType The type of game the agent will participate in.
     * @param id The unique ID of the agent.
     * @param numberOfAgents The total number of agents.
     * @param mailer Communication interface for the agent.
     * @param audit Audit instance for logging activities.
     * @param neighbors List of neighboring agents.
     * @return An instance of an Agent based on the game type.
     * @throws IllegalArgumentException If the game type is null or if gender is null for BoS game type.
     */
    public static Agent createAgent(GameType gameType, int id, int numberOfAgents, Mailer mailer, Audit audit, List<Integer> neighbors, float fraction) {
        if (gameType == null) {
            throw new IllegalArgumentException("GameType cannot be null");
        }

        return switch (gameType) {
            case PD -> new PDAgent(id, numberOfAgents, mailer, audit, neighbors);
            case BoS -> {
                yield new BoSAgent(id, numberOfAgents, mailer, audit, neighbors, fraction);
            }
        };
    }

    /**
     * Creates an array of agents based on the provided game type and parameters.
     *
     * @param gameType The type of game the agents will participate in.
     * @param numberOfAgents The total number of agents.
     * @param mailer Communication interface for the agents.
     * @param audit Audit instance for logging activities.
     * @param network Network of agent connections.
     * @param fraction Fraction of agents to be of a specific gender (Women, only applicable for BoS game type).
     * @return An array of Agent instances.
     */
    public static Agent[] createAgents(GameType gameType, int numberOfAgents, Mailer mailer, Audit audit, AgentNetwork network, float fraction) {
        Agent[] agents = new Agent[numberOfAgents];
        int createdAgents = 0;

        for (int i = 0; i < numberOfAgents; i++) {
            List<Integer> neighbors = network.getNeighbors(i);

            agents[i] = switch (gameType) {
                case PD -> createAgent(GameType.PD, i, numberOfAgents, mailer, audit,neighbors, 0);
                case BoS -> {
                    createdAgents++;
                    yield createAgent(GameType.BoS, i, numberOfAgents, mailer, audit, neighbors, fraction);
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
