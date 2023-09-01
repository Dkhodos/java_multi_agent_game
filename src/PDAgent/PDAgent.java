package PDAgent;

import Agent.Agent;
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

    private Strategy strategy = Strategy.COOPERATE;
    private final Map<Integer, Strategy> neighborsStrategies = new HashMap<>();
    private boolean strategyChanged = true; // To track if the strategy changed in the last round

    private  final Random random = new Random();


    public PDAgent(int id,int numberOfAgents, Mailer mailer, List<Integer> neighbors) {
        super(id, numberOfAgents, mailer, neighbors);
    }

    @Override
    public void play() {
        /* 1. Wait for play message, and read messages for neighbors*/
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
        for (Strategy neighborStrategy : neighborsStrategies.values()) {
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
        Strategy bestStrategy;

        if(neighborsStrategies.size() == 0){
            bestStrategy = random.nextBoolean() ? Strategy.COOPERATE : Strategy.DEFECT;
        } else {
            int cooperatePayoff = 0;
            int defectPayoff = 0;

            for (Strategy neighborStrategy : neighborsStrategies.values()) {
                cooperatePayoff += (neighborStrategy == Strategy.COOPERATE) ? BOTH_COOPERATE : I_COOPERATE_HE_DEFECT;
                defectPayoff += (neighborStrategy == Strategy.COOPERATE) ? I_DEFECT_HE_COOPERATE : BOTH_DEFECT;
            }

            bestStrategy = (cooperatePayoff > defectPayoff) ? Strategy.COOPERATE : Strategy.DEFECT;
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    private void sendDecisionToNeighbors(Strategy action) {
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


    private int calculatePayoff(Strategy myAction, Strategy theirAction) {
        if (myAction == Strategy.COOPERATE && theirAction == Strategy.COOPERATE) {
            return BOTH_COOPERATE;
        } else if (myAction == Strategy.COOPERATE && theirAction == Strategy.DEFECT) {
            return I_COOPERATE_HE_DEFECT;
        } else if (myAction == Strategy.DEFECT && theirAction == Strategy.COOPERATE) {
            return I_DEFECT_HE_COOPERATE;
        } else { // Both defect
            return BOTH_DEFECT;
        }
    }
}