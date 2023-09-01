package GameExecutor;

import Logger.Logger;

public record GameExecutorResults(int totalGain, int totalRounds) {
    private static final Logger logger = new Logger("GameExecutorResults");

    public void print(){
        logger.info("Total Social Welfare (SW): " + totalGain);
        logger.info("Total number of rounds: " + totalRounds);
    }
}
