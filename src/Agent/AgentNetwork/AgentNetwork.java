package Agent.AgentNetwork;

import Logger.Logger;
import java.util.*;

/**
 * Represents a network of agents, where each agent can form connections with other agents.
 */
public class AgentNetwork {

    // Logger instance for logging messages related to the AgentNetwork class
    private static final Logger logger = new Logger("AgentNetwork");

    // The total number of agents in the network
    private final int numberOfAgents;

    // A mapping of agent IDs to the list of agent IDs they're connected to
    private final Map<Integer, List<Integer>> network;

    // A flag to indicate if modifications to the network are allowed
    private boolean locked = false;

    /**
     * Initializes a new AgentNetwork with the specified number of agents.
     *
     * @param numberOfAgents The total number of agents in the network.
     */
    public AgentNetwork(int numberOfAgents){
        this.numberOfAgents = numberOfAgents;
        this.network = getEmptyNetwork();
    }

    /**
     * Establishes a connection between two agents identified by their agent IDs.
     * The connection is bidirectional.
     *
     * @param agentId1 The ID of the first agent.
     * @param agentId2 The ID of the second agent.
     */
    public void connect(int agentId1, int agentId2){
        if(locked) return;

        this.network.get(agentId1).add(agentId2);
        this.network.get(agentId2).add(agentId1);
    }

    /**
     * Locks the network, preventing further modifications.
     */
    public void lock(){
        this.locked = true;
    }

    /**
     * Retrieves a list of neighbors connected to a given agent.
     *
     * @param agentId The ID of the agent.
     * @return A list of agent IDs representing the neighbors of the given agent.
     */
    public List<Integer> getNeighbors(int agentId){
        return this.network.get(agentId);
    }

    /**
     * Prints the connections of all agents in the network.
     */
    public void print(){
        for (Map.Entry<Integer, List<Integer>> entry: network.entrySet()){
            logger.debug("Agent " + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Retrieves the entire network mapping.
     *
     * @return A map representing the entire network, with keys being agent IDs and values being lists of connected agent IDs.
     */
    public Map<Integer, List<Integer>> getNetwork(){
        return network;
    }

    /**
     * Checks if the agent identified by the given ID has any connections.
     *
     * @param agentId The ID of the agent.
     * @return true if the agent has at least one connection, false otherwise.
     */
    public boolean hasConnection(int agentId) {
        return !(this.network.get(agentId).isEmpty());
    }

    /**
     *
     * Initializes an empty network for the agents.
     *
     * @return A map representing the network, with keys being agent IDs and values being empty lists.
     */
    private Map<Integer, List<Integer>> getEmptyNetwork() {
        Map<Integer, List<Integer>> network= new HashMap<>();

        // Iterate over all possible pairs of agents
        for (int i = 0; i < numberOfAgents; i++) {
            network.put(i, new ArrayList<>());
        }

        return network;
    }
}
