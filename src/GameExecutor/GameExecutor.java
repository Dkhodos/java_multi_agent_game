package GameExecutor;

import ArgsSerializer.GameArguments;

import java.util.List;

public class GameExecutor {
    private final Mailer mailer;
    private final GameArguments arguments;

    public GameExecutor(GameArguments arguments){
        this.mailer = new Mailer();
        this.arguments = arguments;
    }

    public void runGame() throws InterruptedException {
        int numberOfAgents = this.arguments.numberOfAgents();

        Agent[] agents = new Agent[numberOfAgents];
        Thread[] threads = new Thread[numberOfAgents];
        for (int i = 0; i < numberOfAgents; i++) {
            agents[i] = new Agent(i + 1, mailer);
            threads[i] = new Thread(agents[i]);
            mailer.subscribe(agents[i].getId());
        }

        // run agents as threads
        for (Thread t : threads) {
            t.start();
        }
        // wait for all agents to terminate
        for (Thread t : threads) {
            t.join();
        }
    }
}
