package Agent.BoSAgent;

import Agent.Agent;
import Mailer.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoSAgent extends Agent implements BoSPayoff {
    private final BoSAgentSex agentSex;
    private BoSStrategy strategy;
    private final Map<Integer, BoSNeighborData> neighborsData = new HashMap<>();
    private int payoff = 0;
    private boolean strategyChanged = true;

    public BoSAgent(int id,int numberOfAgents, Mailer mailer, List<Integer> neighbors, BoSAgentSex agentSex) {
        super(id, numberOfAgents, mailer, neighbors);
        this.agentSex = agentSex;
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

    @Override
    public boolean hasStrategyChanged() {
        return strategyChanged;
    }

    @Override
    public int getPersonalGain(){
        int totalGain = 0;
        for (BoSNeighborData neighborData : neighborsData.values()) {
            totalGain += calculatePayoff(agentSex, strategy, neighborData.strategy());
        }
        return totalGain;
    }

    public BoSAgentSex getAgentSex() {
        return agentSex;
    }

    private void readMessages() {
        while (true){
            Message message = mailer.readOne(agentId);
            if(message instanceof PlayMessage){
                return;
            }

            if(message instanceof BoSMessage bosMessage){
                BoSNeighborData neighborData = new BoSNeighborData(bosMessage.getStrategy(), bosMessage.getAgentSex());
                neighborsData.put(message.getSenderId(), neighborData);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void pickStrategy() {
        BoSStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = pickRandomStrategy(random);
        } else {
            bestStrategy = pickBestStrategy(neighborsData, agentSex, strategy);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    private void sendDecisionToNeighbors() {
        // no point in sending data if nothing changed
        if(!strategyChanged) return;

        for (int neighborId : neighbors) {
            mailer.send(neighborId, new BoSMessage(agentId, strategy, agentSex));
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
