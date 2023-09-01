package PDAgent;

import Agent.Agent;
import BoSAgent.BosStrategy;
import Mailer.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PDAgent extends Agent {
    static final int BOTH_COOPERATE = 8;
    static final int BOTH_DEFECT = 5;
    static final int I_COOPERATE_HE_DEFECT = 0;
    static final int I_DEFECT_HE_COOPERATE = 10;

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
        sendDecisionToNeighbors(strategy);

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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void pickStrategy() {
        PDStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = random.nextBoolean() ? PDStrategy.COOPERATE : PDStrategy.DEFECT;
        } else {
            int cooperatePayoff = 0;
            int defectPayoff = 0;

            for (PDStrategy neighborStrategy : neighborsStrategies.values()) {
                cooperatePayoff += (neighborStrategy == PDStrategy.COOPERATE) ? BOTH_COOPERATE : I_COOPERATE_HE_DEFECT;
                defectPayoff += (neighborStrategy == PDStrategy.COOPERATE) ? I_DEFECT_HE_COOPERATE : BOTH_DEFECT;
            }

            bestStrategy = (cooperatePayoff > defectPayoff) ? PDStrategy.COOPERATE : PDStrategy.DEFECT;
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    private void sendDecisionToNeighbors(PDStrategy action) {
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


    private int calculatePayoff(PDStrategy myAction, PDStrategy theirAction) {
        if (myAction == PDStrategy.COOPERATE && theirAction == PDStrategy.COOPERATE) {
            return BOTH_COOPERATE;
        } else if (myAction == PDStrategy.COOPERATE && theirAction == PDStrategy.DEFECT) {
            return I_COOPERATE_HE_DEFECT;
        } else if (myAction == PDStrategy.DEFECT && theirAction == PDStrategy.COOPERATE) {
            return I_DEFECT_HE_COOPERATE;
        } else { // Both defect
            return BOTH_DEFECT;
        }
    }
}