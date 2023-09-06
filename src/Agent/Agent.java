package Agent;

import Audit.Audit;
import Mailer.*;
import Mailer.Messages.MailerMessage;
import Mailer.Messages.PlayMessage;

import java.util.List;
import java.util.Random;

public abstract class Agent implements Runnable {
    protected final int agentId;
    protected final int numberOfAgents;

    protected final Mailer mailer;
    protected final List<Integer> neighbors;
    protected final Audit audit;
    protected boolean strategyChanged = true; // To track if the strategy changed in the last round
    static protected final Random random = new Random();

    /**
     * Initializes the Agent with specified attributes and dependencies.
     *
     * @param agentId Unique ID of the agent.
     * @param numberOfAgents Total number of agents.
     * @param mailer Communication interface for the agent.
     * @param audit Audit instance for logging activities.
     * @param neighbors List of neighboring agents.
     */
    protected Agent(int agentId, int numberOfAgents, Mailer mailer, Audit audit, List<Integer> neighbors){
        this.agentId = agentId;
        this.numberOfAgents = numberOfAgents;
        this.mailer = mailer;
        this.audit = audit;
        this.neighbors = neighbors;
    }

    /**
     * Retrieves the unique ID of the agent.
     *
     * @return The agent's ID.
     */
    public int getId(){
        return agentId;
    }


    /**
     * Checks if the agent's strategy changed in the last round.
     *
     * @return true if the strategy changed, false otherwise.
     */
    public boolean hasStrategyChanged(){
        return strategyChanged;
    }


    /**
     * Executes the main loop of the agent.
     * Reads messages from neighbors, picks a strategy, sends decisions, and triggers the next agent.
     */
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

    /**
     * Retrieves the agent's personal gain.
     *
     * @return The personal gain value.
     */
    public abstract int getPersonalGain();

    /**
    *  Print out the agents personal gain and strategy
    * */
    public abstract void reportStatus();


    /**
     * Reads messages from the agent's neighbors.
     */
    protected void readMessages(){
        while (true){
            MailerMessage message = mailer.readOne(agentId);
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


    /**
     * Triggers the next agent to play its turn.
     */
    protected void triggerNextAgent(){
        int nextAgentId = agentId + 1;
        if(nextAgentId == numberOfAgents){
            return;
        }

        PlayMessage playMessage = new PlayMessage(nextAgentId);
        mailer.send(nextAgentId, playMessage);
        audit.recordMessage(agentId, nextAgentId, playMessage);
    }

    /**
     * Handles incoming messages from neighbors.
     *
     * @param message The incoming message to handle.
     */
    protected abstract void handleMessage(MailerMessage message);


    /**
     * Picks the best strategy based on the current game state.
     */
    protected abstract void pickStrategy();

    /**
     * Sends the agent's decision to its neighbors.
     */
    protected abstract void sendDecisionToNeighbors();
}
