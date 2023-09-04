package Agent.BoSAgent;

import java.util.Map;
import java.util.Random;


/**
 * Defines the payoff values and strategies for the Battle of the Sexes (BoS) game.
 */
public interface BoSPayoff {

    int WIFE_THEATRE = 3;
    int HUSBAND_THEATRE = 1;
    int WIFE_SOCCER = 1;
    int HUSBAND_SOCCER = 3;
    int BOTH_PICK_DIFFERENTLY = 0;


    /**
     * Picks a random strategy for the BoS game.
     *
     * @param random An instance of the Random class.
     * @return A randomly chosen BoS strategy.
     */
    default BoSStrategy pickRandomStrategy(Random random){
        return random.nextBoolean() ? BoSStrategy.THEATRE : BoSStrategy.SOCCER;
    }


    /**
     * Calculates the payoff based on the agent's gender (sex), strategy, and neighbor's strategy.
     *
     * @param mySex The agent's gender (sex).
     * @param myStrategy The agent's chosen strategy.
     * @param neighborStrategy The neighbor's strategy.
     * @return The payoff value.
     */
    default int calculatePayoff(BoSAgentSex mySex ,BoSStrategy myStrategy, BoSStrategy neighborStrategy){
        if(myStrategy == BoSStrategy.SOCCER && neighborStrategy == BoSStrategy.SOCCER) {
            return mySex == BoSAgentSex.WIFE ? WIFE_SOCCER : HUSBAND_SOCCER;
        } else if(myStrategy == BoSStrategy.SOCCER && neighborStrategy == BoSStrategy.THEATRE) {
            return BOTH_PICK_DIFFERENTLY;
        } else if(myStrategy == BoSStrategy.THEATRE && neighborStrategy == BoSStrategy.SOCCER){
            return BOTH_PICK_DIFFERENTLY;
        } else {
            return mySex == BoSAgentSex.WIFE ? WIFE_THEATRE : HUSBAND_THEATRE;
        }
    }

    /**
     * Picks the best strategy based on neighbors' data.
     *
     * @param neighborsData A map of neighbor IDs and their respective data.
     * @param agentSex The agent's gender (sex).
     * @param strategy The agent's current strategy.
     * @return The best strategy for the agent.
     */
    default BoSStrategy pickBestStrategy(Map<Integer, BoSNeighborData> neighborsData, BoSAgentSex agentSex, BoSStrategy strategy){
        int soccerPayoff = 0;
        int theatrePayoff = 0;

        for (BoSNeighborData neighborData: neighborsData.values()) {
            soccerPayoff += calculatePayoff(agentSex, BoSStrategy.SOCCER, neighborData.strategy());
            theatrePayoff += calculatePayoff(agentSex, BoSStrategy.THEATRE, neighborData.strategy());
        }

        if(soccerPayoff == theatrePayoff) return strategy;
        return soccerPayoff > theatrePayoff ? BoSStrategy.SOCCER : BoSStrategy.THEATRE;
    }
}
