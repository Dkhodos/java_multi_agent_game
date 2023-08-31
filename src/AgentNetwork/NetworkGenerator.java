package AgentNetwork;

import java.util.*;

public class NetworkGenerator {
    private final Random random = new Random();

    private final double probability;
    private final int numberOfAgents;

    public NetworkGenerator(double probability, int numberOfAgents){
        this.probability = probability;
        this.numberOfAgents = numberOfAgents;
    }

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
     * Generates a list of all possible agent pairs.
     *
     * @return A list of all possible agent pairs.
     */
    private List<VarTuple> generateAllPairs() {
        List<VarTuple> allPairs = new ArrayList<>();

        // Iterate over all possible pairs of agents
        for (int i = 1; i < numberOfAgents + 1; i++) {
            for (int j = i + 1; j < numberOfAgents + 1; j++) {
                allPairs.add(new VarTuple(i, j));
            }
        }

        return allPairs;
    }

    /**
     * Determines if two agents are constrained based on the probability.
     *
     * @return true if two agents are constrained, false otherwise.
     */
    private boolean shouldConnectAgents() {
        return random.nextDouble() <= probability;
    }
}
