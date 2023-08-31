package GameExecutor;

import Agent.*;
import AgentNetwork.*;
import ArgsSerializer.*;
import Mailer.*;

public class GameExecutor {
    private final Mailer mailer;
    private final GameArguments arguments;

    public GameExecutor(GameArguments arguments){
        this.mailer = new Mailer();
        this.arguments = arguments;
    }

    public GameExecutorResults runGame() throws InterruptedException {
        int numberOfAgents = this.arguments.numberOfAgents();
        double probability = this.arguments.probability();
        GameType gameType = this.arguments.gameType();
        int fraction = this.arguments.fraction();

        NetworkGenerator generator = new NetworkGenerator(probability, numberOfAgents);
        AgentNetwork network = generator.generateNetwork();

        Agent[] agents = new Agent[numberOfAgents];
        for (int i = 0; i < numberOfAgents; i++) {
            agents[i] = AgentFactory.createAgent(gameType, i,numberOfAgents, mailer, network.getNeighbors(i), fraction);
            mailer.register(i);
        }

        boolean allAgentsStable;
        int rounds = 0;
        do {
            allAgentsStable = true;
            rounds++;
            Thread [] threads = getThreads(agents);

            mailer.send(0, new PlayMessage(0));

            for (Thread t : threads) {
                t.start();
            }

            // wait for all agents to terminate
            for (Thread t : threads) {
                t.join();
            }

            for (Agent agent : agents) {
                if (agent.hasStrategyChanged()) {
                    allAgentsStable = false;
                    break;
                }
            }
        } while (!allAgentsStable);

        int totalGain = 0;
        for (Agent agent : agents) {
            totalGain += agent.getPersonalGain();
        }

        return new GameExecutorResults(totalGain, rounds);
    }

    private Thread [] getThreads(Agent[] agents){
        Thread[] threads = new Thread[agents.length];
        for (int i = 0; i < agents.length; i++) {
            threads[i] = new Thread(agents[i]);
        }
        return threads;
    }
}
