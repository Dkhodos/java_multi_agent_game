package Agent;

import Mailer.Mailer;

import java.util.List;

public class BoSAgent extends Agent {
    private final int fraction;

    public BoSAgent(int id,int numberOfAgents, Mailer mailer, List<Integer> neighbors, int fraction) {
        super(id, numberOfAgents, mailer, neighbors);
        this.fraction = fraction;
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
