/**
 * Represents a generic mailer message.
 */
public abstract class MailerMessage implements Message {
    protected final int from;

    /**
     * Initializes the message with a sender ID.
     *
     * @param agentId ID of the sending agent.
     */
    protected MailerMessage(int agentId){
        this.from = agentId;
    }

    /**
     * Retrieves the sender ID of the message.
     *
     * @return ID of the sending agent.
     */
    public int getSenderId(){
        return from;
    }
}
