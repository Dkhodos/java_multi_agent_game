package GameExecutor;

import Agent.AgentNetwork.AgentNetwork;
import Audit.Audit;
import Logger.Logger;

public record GameExecutorResults(int totalGain, int totalRounds, AgentNetwork network, Audit audit) {
    private static final Logger logger = new Logger("GameExecutorResults");

    public void print(){
        logger.info("Total Social Welfare (SW): " + totalGain);
        logger.info("Total number of rounds: " + totalRounds);
    }
}
