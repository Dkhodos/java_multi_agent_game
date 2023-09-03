package Audit.Messages;

public class AgentScoreMessage extends AuditMessage {
    private final int score;

    public AgentScoreMessage(int agentId, int score) {
        super(agentId);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
