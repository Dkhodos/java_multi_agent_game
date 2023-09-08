
import Agent.BoSAgent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BoSPayoffTest {

    private BoSPayoff boSPayoff;

    @BeforeEach
    public void setup() {
        boSPayoff = new BoSPayoff() {};  // anonymous implementation as the interface methods are default
    }

    @Test
    public void testCalculatePayoff_Wife_BothSoccer() {
        int result = boSPayoff.calculatePayoff(BoSAgentSex.WIFE, BoSStrategy.SOCCER, BoSStrategy.SOCCER);
        assertEquals(BoSPayoff.WIFE_SOCCER, result);
    }

    @Test
    public void testCalculatePayoff_Husband_BothSoccer() {
        int result = boSPayoff.calculatePayoff(BoSAgentSex.HUSBAND, BoSStrategy.SOCCER, BoSStrategy.SOCCER);
        assertEquals(BoSPayoff.HUSBAND_SOCCER, result);
    }

    @Test
    public void testCalculatePayoff_Wife_DifferentChoices() {
        int result = boSPayoff.calculatePayoff(BoSAgentSex.WIFE, BoSStrategy.SOCCER, BoSStrategy.THEATRE);
        assertEquals(BoSPayoff.BOTH_PICK_DIFFERENTLY, result);
    }

    @Test
    public void testCalculatePayoff_Husband_DifferentChoices() {
        int result = boSPayoff.calculatePayoff(BoSAgentSex.HUSBAND, BoSStrategy.THEATRE, BoSStrategy.SOCCER);
        assertEquals(BoSPayoff.BOTH_PICK_DIFFERENTLY, result);
    }

    @Test
    public void testPickBestStrategy_AllWives_AllTheatre() {
        Map<Integer, BoSNeighborData> neighborsData = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            neighborsData.put(i, new BoSNeighborData(BoSStrategy.THEATRE, BoSAgentSex.WIFE));
        }

        BoSStrategy result = boSPayoff.pickBestStrategy(neighborsData, BoSAgentSex.WIFE, BoSStrategy.SOCCER);
        assertEquals(BoSStrategy.THEATRE, result);
    }

    @Test
    public void testPickBestStrategy_AllHusbands_AllSoccer() {
        Map<Integer, BoSNeighborData> neighborsData = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            neighborsData.put(i, new BoSNeighborData(BoSStrategy.SOCCER, BoSAgentSex.HUSBAND));
        }

        BoSStrategy result = boSPayoff.pickBestStrategy(neighborsData, BoSAgentSex.HUSBAND, BoSStrategy.THEATRE);
        assertEquals(BoSStrategy.SOCCER, result);
    }

    @Test
    public void testPickBestStrategy_HalfAndHalf_Wife_StartsTheatre() {
        Map<Integer, BoSNeighborData> neighborsData = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            neighborsData.put(i, new BoSNeighborData(BoSStrategy.THEATRE, BoSAgentSex.WIFE));
        }
        for (int i = 5; i < 10; i++) {
            neighborsData.put(i, new BoSNeighborData(BoSStrategy.SOCCER, BoSAgentSex.HUSBAND));
        }

        BoSStrategy result = boSPayoff.pickBestStrategy(neighborsData, BoSAgentSex.WIFE, BoSStrategy.THEATRE);
        assertEquals(BoSStrategy.THEATRE, result);  // It should remain theatre due to equal payoff
    }
}
