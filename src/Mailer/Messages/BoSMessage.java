package Mailer.Messages;

import Agent.BoSAgent.BoSAgentSex;
import Agent.BoSAgent.BoSStrategy;

public class BoSMessage extends MailerMessage {
    private final BoSStrategy action;
    private final BoSAgentSex agentSex;

    public BoSMessage(int agentId, BoSStrategy action, BoSAgentSex agentSex) {
        super(agentId);
        this.action = action;
        this.agentSex = agentSex;
    }

    public BoSStrategy getStrategy() {
        return action;
    }

    public BoSAgentSex getAgentSex() {
        return agentSex;
    }
}
