package Agent;

import Mailer.Mailer;

import java.util.List;
import java.util.Random;

public abstract class Agent implements Runnable {
    protected final int agentId;
    protected final int numberOfAgents;

    protected final Mailer mailer;
    protected final List<Integer> neighbors;

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

    public void run(){
        if(neighbors.isEmpty()) return;
        play();
    }

    protected abstract void play();

    public abstract boolean hasStrategyChanged();

    public abstract int getPersonalGain();
}
