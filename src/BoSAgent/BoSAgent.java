package BoSAgent;

import Agent.Agent;
import Mailer.*;
import PDAgent.PDStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoSAgent extends Agent {
    static final int WIFE_THEATRE = 3;
    static final int HUSBAND_THEATRE = 1;
    static final int WIFE_SOCCER = 1;
    static final int HUSBAND_SOCCER = 3;
    static final int BOTH_PICK_DIFFERENTLY = 0;

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
        sendDecisionToNeighbors(strategy);

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
            totalGain += calculatePayoff(strategy, neighborData.strategy());
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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void pickStrategy() {
        BoSStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = random.nextBoolean() ? BoSStrategy.THEATRE : BoSStrategy.SOCCER;
        } else {
            int newRoundPayoff = 0;

            for (BoSNeighborData neighborData: neighborsData.values()) {
                newRoundPayoff += calculatePayoff(strategy, neighborData.strategy());
            }

            bestStrategy = calculateBestStrategy(newRoundPayoff);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    private int calculatePayoff(BoSStrategy myStrategy, BoSStrategy neighborStrategy){
        if(myStrategy == BoSStrategy.SOCCER && neighborStrategy == BoSStrategy.SOCCER) {
            return agentSex == BoSAgentSex.WIFE ? WIFE_SOCCER : HUSBAND_SOCCER;
        } else if(myStrategy == BoSStrategy.SOCCER && neighborStrategy == BoSStrategy.THEATRE) {
            return BOTH_PICK_DIFFERENTLY;
        } else if(myStrategy == BoSStrategy.THEATRE && neighborStrategy == BoSStrategy.SOCCER){
            return BOTH_PICK_DIFFERENTLY;
        } else {
            return agentSex == BoSAgentSex.WIFE ? WIFE_THEATRE : HUSBAND_THEATRE;
        }
    }

    private BoSStrategy calculateBestStrategy(int newRoundPayoff) {
        if (newRoundPayoff > payoff) {
            payoff = newRoundPayoff;
            return strategy == BoSStrategy.SOCCER ? BoSStrategy.THEATRE : BoSStrategy.SOCCER;
        }

        return strategy;
    }

    private void sendDecisionToNeighbors(BoSStrategy action) {
        for (int neighborId : neighbors) {
            mailer.send(neighborId, new BoSMessage(agentId, action, agentSex));
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
