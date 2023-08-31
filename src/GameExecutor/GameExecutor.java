package GameExecutor;

import Agent.Agent;
import AgentNetwork.AgentNetwork;
import ArgsSerializer.GameArguments;
import AgentNetwork.NetworkGenerator;
import Agent.AgentFactory;
import ArgsSerializer.GameType;
import Mailer.Mailer;

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
        double probability = this.arguments.probability();
        GameType gameType = this.arguments.gameType();
        int fraction = this.arguments.fraction();

        NetworkGenerator generator = new NetworkGenerator(probability, numberOfAgents);
        AgentNetwork network = generator.generateNetwork();

        Agent[] agents = new Agent[numberOfAgents];
        Thread[] threads = new Thread[numberOfAgents];
        for (int i = 0; i < numberOfAgents; i++) {
            agents[i] = AgentFactory.createAgent(gameType, i, mailer, network.getNeighbors(i), fraction);
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
