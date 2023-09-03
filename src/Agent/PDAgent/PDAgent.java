package Agent.PDAgent;

import Agent.Agent;
import Mailer.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDAgent extends Agent implements PBPayoff {
    private final Map<Integer, PDStrategy> neighborsStrategies = new HashMap<>();
    private PDStrategy strategy;

    public PDAgent(int id,int numberOfAgents, Mailer mailer, List<Integer> neighbors) {
        super(id, numberOfAgents, mailer, neighbors);
    }

    public int getPersonalGain() {
        int totalGain = 0;
        for (PDStrategy neighborStrategy : neighborsStrategies.values()) {
            totalGain += calculatePayoff(strategy, neighborStrategy);
        }
        return totalGain;
    }

    @Override
    protected void handleMessage(Message message) {
        if(message instanceof PDMessage pdMessage){
            neighborsStrategies.put(message.getSenderId(), pdMessage.getStrategy());
        }
    }

    protected void pickStrategy() {
        PDStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = pickRandomStrategy(random);
        } else {
            bestStrategy = pickBestStrategy(neighborsStrategies);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    protected void sendDecisionToNeighbors() {
        for (int neighborId : neighbors) {
            mailer.send(neighborId, new PDMessage(getId(), strategy));
        }
    }
}