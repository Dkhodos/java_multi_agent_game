package Audit.Messages;

public record AgentScoreMessage(int agentId, int score) implements AuditMessage {
}
