package Mailer;

import PDAgent.Strategy;

public class PDMessage extends Message {
    private final Strategy action;

    public PDMessage(int agentId, Strategy action) {
        super(agentId);
        this.action = action;
    }

    public Strategy getStrategy() {
        return action;
    }
}
