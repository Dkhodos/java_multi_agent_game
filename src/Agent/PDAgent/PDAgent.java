package Agent.PDAgent;

import Agent.Agent;
import Mailer.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDAgent extends Agent implements PBPayoff {
    private final Map<Integer, PDStrategy> neighborsStrategies = new HashMap<>();
    private PDStrategy strategy;
    private boolean strategyChanged = true; // To track if the strategy changed in the last round

    public PDAgent(int id,int numberOfAgents, Mailer mailer, List<Integer> neighbors) {
        super(id, numberOfAgents, mailer, neighbors);
    }

    @Override
    public void play() {
        /* 1. Wait for play message, and read messages for neighbors */
        readMessages();

        /* 2. Pick the best strategy based on neighbors' decisions */
        pickStrategy();

        /* 3. Send my decision to my neighbors */
        sendDecisionToNeighbors();

        /* 4. trigger next agent */
        triggerNextAgent();
    }


    public boolean hasStrategyChanged() {
        return strategyChanged;
    }

    public int getPersonalGain() {
        int totalGain = 0;
        for (PDStrategy neighborStrategy : neighborsStrategies.values()) {
            totalGain += calculatePayoff(strategy, neighborStrategy);
        }
        return totalGain;
    }

    private void readMessages(){
        while (true){
            Message message = mailer.readOne(agentId);
            if(message instanceof PlayMessage){
                return;
            }

            if(message instanceof PDMessage pdMessage){
                neighborsStrategies.put(message.getSenderId(), pdMessage.getStrategy());
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void pickStrategy() {
        PDStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = pickRandomStrategy(random);
        } else {
            bestStrategy = pickBestStrategy(neighborsStrategies);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    private void sendDecisionToNeighbors() {
        // no point in sending data if nothing changed
        if(!strategyChanged) return;

        for (int neighborId : neighbors) {
            mailer.send(neighborId, new PDMessage(getId(), strategy));
        }
    }

    private void triggerNextAgent(){
        int nextAgentId = agentId + 1;
        if(nextAgentId == numberOfAgents){
            return;
        }

        mailer.send(nextAgentId, new PlayMessage(nextAgentId));
    }
}