package Mailer;

public abstract class Message {
    protected final int from;

    protected Message(int agentId){
        this.from = agentId;
    }

    public int getSenderId(){
        return from;
    }
}
