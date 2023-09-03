import static org.junit.jupiter.api.Assertions.assertEquals;

import Agent.PDAgent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class PBPayoffTest {

    private PBPayoff testClass;

    @BeforeEach
    public void setup() {
        testClass = new PBPayoff() {}; // This is an anonymous class to create an instance of the interface
    }

    // Test cases for pickBestStrategy
    @Test
    public void testPickBestStrategyAllCooperate() {
        Map<Integer, PDStrategy> strategies = new HashMap<>();
        strategies.put(1, PDStrategy.COOPERATE);
        strategies.put(2, PDStrategy.COOPERATE);

        assertEquals(PDStrategy.DEFECT, testClass.pickBestStrategy(strategies));
    }

    @Test
    public void testPickBestStrategyAllDefect() {
        Map<Integer, PDStrategy> strategies = new HashMap<>();
        strategies.put(1, PDStrategy.DEFECT);
        strategies.put(2, PDStrategy.DEFECT);

        assertEquals(PDStrategy.DEFECT, testClass.pickBestStrategy(strategies));
    }

    @Test
    public void testPickBestStrategyMixed() {
        Map<Integer, PDStrategy> strategies = new HashMap<>();
        strategies.put(1, PDStrategy.COOPERATE);
        strategies.put(2, PDStrategy.DEFECT);

        assertEquals(PDStrategy.DEFECT, testClass.pickBestStrategy(strategies)); // Based on the provided payoffs, defect is the best strategy in this case.
    }

    // Test cases for calculatePayoff
    @Test
    public void testCalculatePayoffBothCooperate() {
        assertEquals(PBPayoff.BOTH_COOPERATE, testClass.calculatePayoff(PDStrategy.COOPERATE, PDStrategy.COOPERATE));
    }

    @Test
    public void testCalculatePayoffICooperateHeDefect() {
        assertEquals(PBPayoff.I_COOPERATE_HE_DEFECT, testClass.calculatePayoff(PDStrategy.COOPERATE, PDStrategy.DEFECT));
    }

    @Test
    public void testCalculatePayoffIDefectHeCooperates() {
        assertEquals(PBPayoff.I_DEFECT_HE_COOPERATE, testClass.calculatePayoff(PDStrategy.DEFECT, PDStrategy.COOPERATE));
    }

    @Test
    public void testCalculatePayoffBothDefect() {
        assertEquals(PBPayoff.BOTH_DEFECT, testClass.calculatePayoff(PDStrategy.DEFECT, PDStrategy.DEFECT));
    }

    @Test
    public void testPickBestStrategyNineCooperateOneDefect() {
        Map<Integer, PDStrategy> strategies = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            strategies.put(i, PDStrategy.COOPERATE);
        }
        strategies.put(9, PDStrategy.DEFECT);

        assertEquals(PDStrategy.DEFECT, testClass.pickBestStrategy(strategies));
    }

    @Test
    public void testPickBestStrategyFiveCooperateFiveDefect() {
        Map<Integer, PDStrategy> strategies = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            strategies.put(i, PDStrategy.COOPERATE);
        }
        for (int i = 5; i < 10; i++) {
            strategies.put(i, PDStrategy.DEFECT);
        }

        assertEquals(PDStrategy.DEFECT, testClass.pickBestStrategy(strategies));
    }

    @Test
    public void testPickBestStrategyOneCooperateNineDefect() {
        Map<Integer, PDStrategy> strategies = new HashMap<>();
        strategies.put(0, PDStrategy.COOPERATE);
        for (int i = 1; i < 10; i++) {
            strategies.put(i, PDStrategy.DEFECT);
        }

        assertEquals(PDStrategy.DEFECT, testClass.pickBestStrategy(strategies));
    }

    @Test
    public void testPickBestStrategyEmptyMap() {
        Map<Integer, PDStrategy> strategies = new HashMap<>();

        assertEquals(PDStrategy.DEFECT, testClass.pickBestStrategy(strategies));  // In case of a tie or empty map, the method defaults to DEFECT.
    }

}
