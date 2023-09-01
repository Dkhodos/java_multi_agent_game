package Mailer;

public class RoundUpdateMessage extends Message {
    private final int round;

    public RoundUpdateMessage(int agentId, int round) {
        super(agentId);
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}
