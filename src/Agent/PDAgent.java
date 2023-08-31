package Agent;

import Mailer.Mailer;

import java.util.List;

public class PDAgent extends Agent {
    public PDAgent(int id, Mailer mailer, List<Integer> neighbors) {
        super(id, mailer, neighbors);
    }

    @Override
    public void play() {
        // Implement the logic specific to Battle of the Sexes
    }
}
