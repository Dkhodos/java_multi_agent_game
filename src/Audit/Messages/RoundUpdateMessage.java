package Audit.Messages;

public class RoundUpdateMessage extends AuditMessage {
    private final int round;

    public RoundUpdateMessage(int agentId, int round) {
        super(agentId);
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}
