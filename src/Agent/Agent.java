package Agent;

import Mailer.Mailer;

import java.util.List;

public abstract class Agent implements Runnable {
    private final int id;
    private final Mailer mailer;
    private final List<Integer> neighbors;

    protected Agent(int id, Mailer mailer, List<Integer> neighbors){
        this.id = id;
        this.mailer = mailer;
        this.neighbors = neighbors;
    }

    public int getId(){
        return id;
    }

    public void run(){
        if(neighbors.isEmpty()) return;
        play();
    }

    protected abstract void play();
}
