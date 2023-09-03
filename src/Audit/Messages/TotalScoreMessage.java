package Audit.Messages;

public class TotalScoreMessage extends AuditMessage {
    private final int score;

    public TotalScoreMessage(int agentId, int score) {
        super(agentId);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
