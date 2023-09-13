/**
 * Represents the results of a game execution.
 */
public record GameExecutorResults(double avgGain, int totalRounds, AgentNetwork network, Audit audit) {
    private static final Logger logger = new Logger("GameExecutorResults");

    /**
     * Logs the game results.
     */
    public void print(){
        logger.info("Average Social Welfare (SW): " + avgGain);
        logger.info("Total number of rounds: " + totalRounds);
    }
}
