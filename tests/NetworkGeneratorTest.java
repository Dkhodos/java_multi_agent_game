
import Agent.AgentNetwork.AgentNetwork;
import Agent.AgentNetwork.NetworkGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NetworkGeneratorTest {
    @Test
    void testGenerateNetwork() {
        NetworkGenerator generator = new NetworkGenerator(0.5, 10);
        AgentNetwork network = generator.generateNetwork();

        // Check if the network has the expected number of agents
        for (int i = 0; i < 10; i++) {
            assertNotNull(network.getNeighbors(i));
        }
    }

    @Test
    void testGenerateNetworkWithZeroProbability() {
        NetworkGenerator generator = new NetworkGenerator(0, 10);
        AgentNetwork network = generator.generateNetwork();

        // Check if the network has the expected number of agents and at least a single connection
        for (int i = 0; i < 10; i++) {
            assertFalse(network.getNeighbors(i).isEmpty());
        }
    }

    @Test
    void testGenerateNetworkWithFullProbability() {
        NetworkGenerator generator = new NetworkGenerator(1, 10);
        AgentNetwork network = generator.generateNetwork();

        // Check if the network has the expected number of agents and all possible connections
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i != j) {
                    assertTrue(network.getNeighbors(i).contains(j));
                }
            }
        }
    }

    @Test
    void testGenerateNetworkWithLowProbabilityNoHoles() {
        NetworkGenerator generator = new NetworkGenerator(0.1, 10);
        AgentNetwork network = generator.generateNetwork();

        // Check if the network has the expected number of agents and no agent is without a connection
        for (int i = 0; i < 10; i++) {
            assertTrue(network.hasConnection(i));
        }
    }
}
