package Agent.AgentNetwork;

import java.util.*;

/**
 * Responsible for generating a network of agents based on a given probability.
 */
public class NetworkGenerator {

    // Random instance to generate random numbers
    private final Random random = new Random();

    // Probability of forming a connection between two agents
    private final double probability;

    // The total number of agents to consider for the network
    private final int numberOfAgents;

    /**
     * Initializes a new NetworkGenerator with the specified probability and number of agents.
     *
     * @param probability The probability of forming a connection between any two agents.
     * @param numberOfAgents The total number of agents to consider for the network.
     */
    public NetworkGenerator(double probability, int numberOfAgents){
        this.probability = probability;
        this.numberOfAgents = numberOfAgents;
    }

    /**
     * Generates an AgentNetwork based on the specified probability.
     *
     * @return An AgentNetwork representing the generated network.
     */
    public AgentNetwork generateNetwork(){
        AgentNetwork network = new AgentNetwork(numberOfAgents);

        List<VarTuple> allPairs = generateAllPairs();

        // Iterate over all pairs of agents
        for (VarTuple pair : allPairs) {
            // If agents are constrained, create a connection
            if (shouldConnectAgents()) {
                network.connect(pair.i(), pair.j());
            }
        }

        // lock so no modification can be done
        network.lock();

        return network;
    }

    /**
     * Generates a list of all possible pairs of agents.
     *
     * @return A list of VarTuple objects, each representing a unique pair of agent IDs.
     */
    private List<VarTuple> generateAllPairs() {
        List<VarTuple> allPairs = new ArrayList<>();

        // Iterate over all possible pairs of agents
        for (int i = 0; i < numberOfAgents; i++) {
            for (int j = i + 1; j < numberOfAgents; j++) {
                allPairs.add(new VarTuple(i, j));
            }
        }

        return allPairs;
    }

    /**
     * Determines if two agents should be connected based on the given probability.
     *
     * @return true if two agents should be connected, false otherwise.
     */
    private boolean shouldConnectAgents() {
        return random.nextDouble() <= probability;
    }
}
