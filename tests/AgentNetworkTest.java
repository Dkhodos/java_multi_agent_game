import Agent.AgentNetwork.AgentNetwork;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgentNetworkTest {

    @Test
    void testConnect() {
        AgentNetwork network = new AgentNetwork(5);
        network.connect(1, 2);

        assertTrue(network.getNeighbors(1).contains(2));
        assertTrue(network.getNeighbors(2).contains(1));
    }

    @Test
    void testLock() {
        AgentNetwork network = new AgentNetwork(5);
        network.lock();
        network.connect(1, 2);

        assertFalse(network.getNeighbors(1).contains(2));
        assertFalse(network.getNeighbors(2).contains(1));
    }
}