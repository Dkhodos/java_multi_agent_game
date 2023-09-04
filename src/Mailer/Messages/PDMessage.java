package Mailer.Messages;

import Agent.PDAgent.PDStrategy;

public class PDMessage extends MailerMessage {
    private final PDStrategy action;

    /**
     * Initializes the PD message with a sender ID and strategy.
     *
     * @param agentId ID of the sending agent.
     * @param action Chosen strategy for the PD game.
     */
    public PDMessage(int agentId, PDStrategy action) {
        super(agentId);
        this.action = action;
    }

    /**
     * Retrieves the chosen strategy of the agent for the PD game.
     *
     * @return Chosen strategy.
     */
    public PDStrategy getStrategy() {
        return action;
    }
}
