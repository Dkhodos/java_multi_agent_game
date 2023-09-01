package BoSAgent;

import Agent.Agent;
import Mailer.Mailer;
import PDAgent.PDStrategy;

import java.util.List;

public class BoSAgent extends Agent {

    private final int fraction;
    private BosStrategy strategy;

    public BoSAgent(int id,int numberOfAgents, Mailer mailer, List<Integer> neighbors, int fraction) {
        super(id, numberOfAgents, mailer, neighbors);
        this.fraction = fraction;

        strategy = random.nextBoolean() ? BosStrategy.THEATRE : BosStrategy.FOOTBALL;
    }

    @Override
    public void play() {
        // Implement the logic specific to Battle of the Sexes
    }

    @Override
    public boolean hasStrategyChanged() {
        return false;
    }

    @Override
    public int getPersonalGain(){
        return 0;
    }
}
