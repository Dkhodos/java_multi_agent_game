package Agent;

import Mailer.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDAgent extends Agent {
    static final int BOTH_COOPERATE = 8;
    static final int BOTH_DEFECT = 5;
    static final int I_COOPERATE_HE_DEFECT = 0;
    static final int I_DEFECT_HE_COOPERATE = 10;

    private PDMessage.Strategy strategy = PDMessage.Strategy.COOPERATE;
    private final Map<Integer, PDMessage.Strategy> neighborsStrategies = new HashMap<>();
    private boolean strategyChanged = true; // To track if the strategy changed in the last round


    public PDAgent(int id,int numberOfAgents, Mailer mailer, List<Integer> neighbors) {
        super(id, numberOfAgents, mailer, neighbors);
    }

    @Override
    public void play() {
        /* 0. Wait for play message*/
        waitForPlayMessage();

        /* 1. Read decisions from neighbors */
        readDecisionsFromNeighbors();

        /* 2. Pick the best strategy based on neighbors' decisions */
        pickStrategy();

        /* 3. Send my decision to my neighbors */
        sendDecisionToNeighbors(strategy);

        /* 4. trigger next agent */
        triggerNextAgent();
    }


    public boolean hasStrategyChanged() {
        return strategyChanged;
    }

    public int getPersonalGain() {
        int totalGain = 0;
        for (PDMessage.Strategy neighborStrategy : neighborsStrategies.values()) {
            totalGain += calculatePayoff(strategy, neighborStrategy);
        }
        return totalGain;
    }

    private void waitForPlayMessage(){
        while (true){
            Message message = mailer.readOne(agentId);
            if(message instanceof PlayMessage){
                return;
            }

            if(message instanceof PDMessage pdMessage){
                neighborsStrategies.put(message.getSenderId(), pdMessage.getStrategy());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void readDecisionsFromNeighbors() {
        PDMessage message = (PDMessage) mailer.readOne(getId());
        while (message != null){
            neighborsStrategies.put(message.getSenderId(), message.getStrategy());
            message = (PDMessage) mailer.readOne(getId());
        }
    }

    private void pickStrategy() {
        PDMessage.Strategy bestStrategy = PDMessage.Strategy.DEFECT; // Default to DEFECT as it's the dominant strategy for PD

        for (PDMessage.Strategy neighborStrategy : neighborsStrategies.values()) {
            if (neighborStrategy == PDMessage.Strategy.COOPERATE) {
                break;
            }
        }

        if (bestStrategy != strategy) {
            strategyChanged = true;
            strategy = bestStrategy;
        } else {
            strategyChanged = false;
        }
    }

    private void sendDecisionToNeighbors(PDMessage.Strategy action) {
        for (int neighborId : neighbors) {
            mailer.send(neighborId, new PDMessage(getId(), action));
        }
    }

    private void triggerNextAgent(){
        int nextAgentId = agentId + 1;
        if(nextAgentId == numberOfAgents){
            return;
        }

        mailer.send(nextAgentId, new PlayMessage(nextAgentId));
    }


    private int calculatePayoff(PDMessage.Strategy myAction, PDMessage.Strategy theirAction) {
        if (myAction == PDMessage.Strategy.COOPERATE && theirAction == PDMessage.Strategy.COOPERATE) {
            return BOTH_COOPERATE;
        } else if (myAction == PDMessage.Strategy.COOPERATE && theirAction == PDMessage.Strategy.DEFECT) {
            return I_COOPERATE_HE_DEFECT;
        } else if (myAction == PDMessage.Strategy.DEFECT && theirAction == PDMessage.Strategy.COOPERATE) {
            return I_DEFECT_HE_COOPERATE;
        } else { // Both defect
            return BOTH_DEFECT;
        }
    }
}