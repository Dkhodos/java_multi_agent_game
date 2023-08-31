package Agent;

import Mailer.Mailer;

import java.util.List;

public class BoSAgent extends Agent {
    private final int fraction;

    public BoSAgent(int id, Mailer mailer, List<Integer> neighbors, int fraction) {
        super(id, mailer, neighbors);
        this.fraction = fraction;
    }

    @Override
    public void play() {
        // Implement the logic specific to Battle of the Sexes
    }
}
