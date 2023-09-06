package Agent.BoSAgent;

import Agent.Agent;
import Audit.Audit;
import Audit.Messages.AgentScoreMessage;
import Logger.Logger;
import Mailer.*;
import Mailer.Messages.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an agent that plays the Battle of the Sexes (BoS) game.
 * Extends the Agent class and implements the BoSPayoff interface.
 */
public class BoSAgent extends Agent implements BoSPayoff {
    private static final Logger logger = new Logger("BoSAgent");

    private final BoSAgentSex agentSex;
    private BoSStrategy strategy;
    private final Map<Integer, BoSNeighborData> neighborsData = new HashMap<>();

    /**
     * Initializes the BoSAgent with specified attributes and dependencies.
     *
     * @param id Unique ID of the agent.
     * @param numberOfAgents Total number of agents.
     * @param mailer Communication interface for the agent.
     * @param audit Audit instance for logging activities.
     * @param neighbors List of neighboring agents.
     * @param agentSex Gender (sex) of the agent.
     */
    public BoSAgent(int id, int numberOfAgents, Mailer mailer, Audit audit,List<Integer> neighbors, BoSAgentSex agentSex) {
        super(id, numberOfAgents, mailer, audit, neighbors);
        this.agentSex = agentSex;
    }

    /**
     * Calculates the agent's personal gain based on its strategy and neighbors' data.
     *
     * @return The total personal gain.
     */
    @Override
    public int getPersonalGain(){
        int totalGain = 0;
        for (BoSNeighborData neighborData : neighborsData.values()) {
            totalGain += calculatePayoff(agentSex, strategy, neighborData.strategy());
        }
        return totalGain;
    }

    /**
     * Retrieves the gender (sex) of the agent.
     *
     * @return The agent's gender (sex).
     */
    public BoSAgentSex getAgentSex() {
        return agentSex;
    }

    public void reportStatus(){
        int personalGain = getPersonalGain();

        audit.recordMessage(agentId, -1, new AgentScoreMessage(agentId, personalGain));
        logger.info("Agent " + agentId + " earned a score of " + personalGain
                + ", with strategy: " + strategy + ", and sex: " + agentSex);
    }

    /**
     * Handles incoming messages related to the Battle of the Sexes game.
     *
     * @param message The incoming message to handle.
     */
    protected void handleMessage(MailerMessage message) {
        if(message instanceof BoSMessage bosMessage){
            BoSNeighborData neighborData = new BoSNeighborData(bosMessage.getStrategy(), bosMessage.getAgentSex());
            neighborsData.put(bosMessage.getSenderId(), neighborData);
        }
    }

    /**
     * Picks the agent's strategy for the current round based on neighbors' data.
     */
    protected void pickStrategy() {
        BoSStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = pickRandomStrategy(random);
        } else {
            bestStrategy = pickBestStrategy(neighborsData, agentSex, strategy);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }


    /**
     * Sends the agent's strategy and gender (sex) to its neighbors.
     */
    protected void sendDecisionToNeighbors() {
        for (int neighborId : neighbors) {
            BoSMessage boSMessage = new BoSMessage(agentId, strategy, agentSex);
            mailer.send(neighborId, boSMessage);
            audit.recordMessage(agentId, neighborId, boSMessage);
        }
    }
}
