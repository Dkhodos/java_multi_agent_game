package Audit.Messages;

import Mailer.Messages.Message;

public abstract class AuditMessage implements Message {
    protected final int from;

    protected AuditMessage(int agentId){
        this.from = agentId;
    }

    public int getSenderId(){
        return from;
    }
}
