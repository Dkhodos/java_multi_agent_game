package Mailer;

import PDAgent.PDStrategy;
import PDAgent.PDStrategy;

public class PDMessage extends Message {
    private final PDStrategy action;

    public PDMessage(int agentId, PDStrategy action) {
        super(agentId);
        this.action = action;
    }

    public PDStrategy getStrategy() {
        return action;
    }
}
