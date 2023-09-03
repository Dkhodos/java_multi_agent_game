package Mailer.Messages;

public abstract class MailerMessage {
    protected final int from;

    protected MailerMessage(int agentId){
        this.from = agentId;
    }

    public int getSenderId(){
        return from;
    }
}
