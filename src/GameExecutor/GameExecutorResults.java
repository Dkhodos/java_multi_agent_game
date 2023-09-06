package GameExecutor;

import Agent.AgentNetwork.AgentNetwork;
import Audit.Audit;
import Logger.Logger;

/**
 * Represents the results of a game execution.
 */
public record GameExecutorResults(int totalGain, int totalRounds, AgentNetwork network, Audit audit) {
    private static final Logger logger = new Logger("GameExecutorResults");

    /**
     * Logs the game results.
     */
    public void print(){
        logger.debug("Total Social Welfare (SW): " + totalGain);
        logger.debug("Total number of rounds: " + totalRounds);
    }
}
