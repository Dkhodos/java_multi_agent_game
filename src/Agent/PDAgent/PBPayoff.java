package Agent.PDAgent;

import java.util.Map;
import java.util.Random;

public interface PBPayoff {
    int BOTH_COOPERATE = 8;
    int BOTH_DEFECT = 5;
    int I_COOPERATE_HE_DEFECT = 0;
    int I_DEFECT_HE_COOPERATE = 10;

    default PDStrategy pickRandomStrategy(Random random){
        return random.nextBoolean() ? PDStrategy.COOPERATE : PDStrategy.DEFECT;
    }

    default PDStrategy pickBestStrategy(Map<Integer, PDStrategy> neighborsStrategies){
        int cooperatePayoff = 0;
        int defectPayoff = 0;

        for (PDStrategy neighborStrategy : neighborsStrategies.values()) {
            cooperatePayoff += (neighborStrategy == PDStrategy.COOPERATE) ? BOTH_COOPERATE : I_COOPERATE_HE_DEFECT;
            defectPayoff += (neighborStrategy == PDStrategy.COOPERATE) ? I_DEFECT_HE_COOPERATE : BOTH_DEFECT;
        }

        return  (cooperatePayoff > defectPayoff) ? PDStrategy.COOPERATE : PDStrategy.DEFECT;
    }

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
