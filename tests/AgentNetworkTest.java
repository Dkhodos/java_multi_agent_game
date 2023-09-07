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

    @Test
    void testAgentWithoutConnection() {
        AgentNetwork network = new AgentNetwork(5);
        assertFalse(network.hasConnection(1));
    }

    @Test
    void testAgentWithConnection() {
        AgentNetwork network = new AgentNetwork(5);
        network.connect(1, 2);

        assertTrue(network.hasConnection(1));
        assertTrue(network.hasConnection(2));
    }

    @Test
    void testMultipleAgentsSomeWithoutConnection() {
        AgentNetwork network = new AgentNetwork(5);
        network.connect(1, 2);
        network.connect(3, 4);

        assertTrue(network.hasConnection(1));
        assertTrue(network.hasConnection(3));
        assertFalse(network.hasConnection(0));
    }
}