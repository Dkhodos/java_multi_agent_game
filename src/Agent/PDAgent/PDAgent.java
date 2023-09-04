package Agent.PDAgent;

import Agent.Agent;
import Audit.Audit;
import Mailer.*;
import Mailer.Messages.MailerMessage;
import Mailer.Messages.PDMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an agent that plays the Prisoner's Dilemma game.
 * Extends the Agent class and implements the PBPayoff interface.
 */
public class PDAgent extends Agent implements PBPayoff {
    private final Map<Integer, PDStrategy> neighborsStrategies = new HashMap<>();
    private PDStrategy strategy;


    /**
     * Initializes the PDAgent with specified attributes and dependencies.
     *
     * @param id Unique ID of the agent.
     * @param numberOfAgents Total number of agents.
     * @param mailer Communication interface for the agent.
     * @param audit Audit instance for logging activities.
     * @param neighbors List of neighboring agents.
     */
    public PDAgent(int id, int numberOfAgents, Mailer mailer, Audit audit,List<Integer> neighbors) {
        super(id, numberOfAgents, mailer, audit, neighbors);
    }

    /**
     * Calculates the agent's personal gain based on its strategy and neighbors' strategies.
     *
     * @return The total personal gain.
     */
    public int getPersonalGain() {
        int totalGain = 0;
        for (PDStrategy neighborStrategy : neighborsStrategies.values()) {
            totalGain += calculatePayoff(strategy, neighborStrategy);
        }
        return totalGain;
    }

    /**
     * Handles incoming messages related to the Prisoner's Dilemma game.
     *
     * @param message The incoming message to handle.
     */
    @Override
    protected void handleMessage(MailerMessage message) {
        if(message instanceof PDMessage pdMessage){
            neighborsStrategies.put(pdMessage.getSenderId(), pdMessage.getStrategy());
        }
    }

    /**
     * Picks the best strategy for the current round based on neighbors' strategies.
     */
    protected void pickStrategy() {
        PDStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = pickRandomStrategy(random);
        } else {
            bestStrategy = pickBestStrategy(neighborsStrategies);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    /**
     * Sends the agent's strategy to its neighbors.
     */
    protected void sendDecisionToNeighbors() {
        for (int neighborId : neighbors) {
            PDMessage pdMessage = new PDMessage(getId(), strategy);
            mailer.send(neighborId, pdMessage);
            audit.recordMessage(agentId, neighborId, pdMessage);
        }
    }
}