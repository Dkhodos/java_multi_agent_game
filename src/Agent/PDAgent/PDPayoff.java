package Agent.PDAgent;

import java.util.Map;
import java.util.Random;

/**
 * Defines the payoff values and strategies for the Prisoner's Dilemma (PD) game.
 */
public interface PDPayoff {
    // Payoff values for different combinations of strategies

    int BOTH_COOPERATE = 8;
    int BOTH_DEFECT = 5;
    int I_COOPERATE_HE_DEFECT = 0;
    int I_DEFECT_HE_COOPERATE = 10;


    /**
     * Picks a random strategy for the PD game.
     *
     * @param random An instance of the Random class.
     * @return A randomly chosen PD strategy.
     */
    default PDStrategy pickRandomStrategy(Random random){
        return random.nextBoolean() ? PDStrategy.COOPERATE : PDStrategy.DEFECT;
    }


    /**
     * Picks the best strategy based on neighbors' strategies.
     *
     * @param neighborsStrategies A map of neighbor IDs and their respective strategies.
     * @return The best strategy considering neighbors' strategies.
     */
    default PDStrategy pickBestStrategy(Map<Integer, PDStrategy> neighborsStrategies){
        int cooperatePayoff = 0;
        int defectPayoff = 0;

        for (PDStrategy neighborStrategy : neighborsStrategies.values()) {
            cooperatePayoff += (neighborStrategy == PDStrategy.COOPERATE) ? BOTH_COOPERATE : I_COOPERATE_HE_DEFECT;
            defectPayoff += (neighborStrategy == PDStrategy.COOPERATE) ? I_DEFECT_HE_COOPERATE : BOTH_DEFECT;
        }

        return  (cooperatePayoff > defectPayoff) ? PDStrategy.COOPERATE : PDStrategy.DEFECT;
    }

    /**
     * Calculates the payoff based on the agent's action and their neighbor's action.
     *
     * @param myAction The agent's strategy.
     * @param theirAction The neighbor's strategy.
     * @return The payoff value.
     */
    default int calculatePayoff(PDStrategy myAction, PDStrategy theirAction) {
        if (myAction == PDStrategy.COOPERATE && theirAction == PDStrategy.COOPERATE) {
            return BOTH_COOPERATE;
        } else if (myAction == PDStrategy.COOPERATE && theirAction == PDStrategy.DEFECT) {
            return I_COOPERATE_HE_DEFECT;
        } else if (myAction == PDStrategy.DEFECT && theirAction == PDStrategy.COOPERATE) {
            return I_DEFECT_HE_COOPERATE;
        } else { // Both defect
            return BOTH_DEFECT;
        }
    }
}
