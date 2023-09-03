package Agent;

import Mailer.*;

import java.util.List;
import java.util.Random;

public abstract class Agent implements Runnable {
    protected final int agentId;
    protected final int numberOfAgents;

    protected final Mailer mailer;
    protected final List<Integer> neighbors;
    protected boolean strategyChanged = true; // To track if the strategy changed in the last round
    static protected final Random random = new Random();

    protected Agent(int agentId, int numberOfAgents, Mailer mailer, List<Integer> neighbors){
        this.agentId = agentId;
        this.numberOfAgents = numberOfAgents;
        this.mailer = mailer;
        this.neighbors = neighbors;
    }

    public int getId(){
        return agentId;
    }

    public boolean hasStrategyChanged(){
        return strategyChanged;
    }

    public void run(){
        if(neighbors.isEmpty()) return;

        /* 1. Wait for play message, and read messages for neighbors */
        readMessages();

        /* 2. Pick the best strategy based on neighbors' decisions */
        pickStrategy();

        /* 3. Send my decision to my neighbors (no point in sending data if nothing changed)*/
        if(strategyChanged) sendDecisionToNeighbors();

        /* 4. trigger next agent */
        triggerNextAgent();
    }

    protected void readMessages(){
        while (true){
            Message message = mailer.readOne(agentId);
            if(message instanceof PlayMessage){
                return;
            }

            handleMessage(message);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void triggerNextAgent(){
        int nextAgentId = agentId + 1;
        if(nextAgentId == numberOfAgents){
            return;
        }

        mailer.send(nextAgentId, new PlayMessage(nextAgentId));
    }
    protected abstract void handleMessage(Message message);

    protected abstract void pickStrategy();
    protected abstract void sendDecisionToNeighbors();

    public abstract int getPersonalGain();
}
