package Mailer.Messages;

import Agent.PDAgent.PDStrategy;

public class PDMessage extends MailerMessage {
    private final PDStrategy action;

    public PDMessage(int agentId, PDStrategy action) {
        super(agentId);
        this.action = action;
    }

    public PDStrategy getStrategy() {
        return action;
    }
}
