package Mailer.Messages;

import Agent.BoSAgent.*;

/**
 * Represents a message specific to the Battle of the Sexes (BoS) game.
 */
public class BoSMessage extends MailerMessage {
    private final BoSStrategy action;
    private final BoSAgentSex agentSex;

    /**
     * Initializes the BoS message with a sender ID, strategy, and agent gender (sex).
     *
     * @param agentId ID of the sending agent.
     * @param action Chosen strategy for the BoS game.
     * @param agentSex Gender (sex) of the agent.
     */
    public BoSMessage(int agentId, BoSStrategy action, BoSAgentSex agentSex) {
        super(agentId);
        this.action = action;
        this.agentSex = agentSex;
    }


    /**
     * Retrieves the chosen strategy of the agent for the BoS game.
     *
     * @return Chosen strategy.
     */
    public BoSStrategy getStrategy() {
        return action;
    }

    /**
     * Retrieves the gender (sex) of the agent.
     *
     * @return Gender (sex) of the agent.
     */
    public BoSAgentSex getAgentSex() {
        return agentSex;
    }
}
