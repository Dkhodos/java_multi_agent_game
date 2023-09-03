package Agent.BoSAgent;

import Agent.PDAgent.PDStrategy;

import java.util.Map;
import java.util.Random;

public interface BoSPayoff {

    int WIFE_THEATRE = 3;
    int HUSBAND_THEATRE = 1;
    int WIFE_SOCCER = 1;
    int HUSBAND_SOCCER = 3;
    int BOTH_PICK_DIFFERENTLY = 0;

    default BoSStrategy pickRandomStrategy(Random random){
        return random.nextBoolean() ? BoSStrategy.THEATRE : BoSStrategy.SOCCER;
    }

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
