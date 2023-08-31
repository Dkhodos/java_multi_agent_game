package GameExecutor;

import ArgsSerializer.GameArguments;

public class Agent implements Runnable {
    private final int id;

    public Agent(int id, Mailer mailer){
        this.id = id;
    }

    public void run(){

    }

    public int getId(){
        return id;
    }
}
