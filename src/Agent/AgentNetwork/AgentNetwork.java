package Agent.AgentNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentNetwork {
    static String OBJECT_TEMPLATE = "%s: [%s],";

    private final int numberOfAgents;
    private final Map<Integer, List<Integer>> network;

    private boolean locked = false;

    public AgentNetwork(int numberOfAgents){
        this.numberOfAgents = numberOfAgents;
        this.network = getEmptyNetwork();
    }

    public void connect(int agentId1, int agentId2){
        if(locked) return;

        this.network.get(agentId1).add(agentId2);
        this.network.get(agentId2).add(agentId1);
    }

    public void lock(){
        this.locked = true;
    }

    public List<Integer> getNeighbors(int agentId){
        return this.network.get(agentId);
    }

    public void print(){
        for (Map.Entry<Integer, List<Integer>> entry: network.entrySet()){
            System.out.println("Agent " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private Map<Integer, List<Integer>> getEmptyNetwork() {
        Map<Integer, List<Integer>> network= new HashMap<>();

        // Iterate over all possible pairs of agents
        for (int i = 0; i < numberOfAgents; i++) {
            network.put(i, new ArrayList<>());
        }

        return network;
    }

    public String toJavaScriptJsonString(){
        StringBuilder string = new StringBuilder();
        string.append("{");

        for (Map.Entry<Integer, List<Integer>> entry: network.entrySet()){
            int agentId = entry.getKey();
            List<Integer> neighbors = entry.getValue();

            StringBuilder neighborsString = new StringBuilder();
            for(int neighbor : neighbors){
                neighborsString.append(neighbor).append(", ");
            }

            string.append(String.format(OBJECT_TEMPLATE, agentId, neighborsString));
        }

        string.append("}");

        return string.toString();
    }
}