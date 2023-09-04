package Mailer.Messages;

/**
 * Represents a play message in the game.
 */
public class PlayMessage extends MailerMessage {

    /**
     * Initializes the play message with a sender ID.
     *
     * @param agentId ID of the sending agent.
     */
    public PlayMessage(int agentId) {
        super(agentId);
    }
}
