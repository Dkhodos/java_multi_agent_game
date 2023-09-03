package Mailer.Messages;

import Message.Message;

public abstract class MailerMessage implements Message {
    protected final int from;

    protected MailerMessage(int agentId){
        this.from = agentId;
    }

    public int getSenderId(){
        return from;
    }
}
