package GameExecutor;

import Agent.*;
import Agent.AgentNetwork.AgentNetwork;
import Agent.AgentNetwork.NetworkGenerator;
import Agent.BoSAgent.BoSAgent;
import Agent.BoSAgent.BoSAgentSex;
import ArgsSerializer.*;
import Audit.Audit;
import Mailer.*;
import ReportMaker.ReportMaker;

public class GameExecutor {
    private final Mailer mailer;
    private final GameArguments arguments;
    private final Audit audit;

    private static final int GAME_MASTER_ID = -1;

    public GameExecutor(GameArguments arguments){
        this.audit = new Audit();
        this.mailer = new Mailer(audit);
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
        int createdAgents = 0;

        for (int i = 0; i < numberOfAgents; i++) {
            BoSAgentSex bosAgentSex = createdAgents < fraction ? BoSAgentSex.WIFE : BoSAgentSex.HUSBAND;
            createdAgents++;

            agents[i] = AgentFactory.createAgent(gameType, i, numberOfAgents, mailer, network.getNeighbors(i), bosAgentSex);
            mailer.register(i);
        }

        boolean allAgentsStable;
        int rounds = -1;
        do {
            allAgentsStable = true;

            rounds++;
            audit.recordMessage(GAME_MASTER_ID, GAME_MASTER_ID, new RoundUpdateMessage(GAME_MASTER_ID, rounds));

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
        BoSAgentSex[] agentSexes = new BoSAgentSex[numberOfAgents];
        for (Agent agent : agents) {
            int personalGain = agent.getPersonalGain();
            int agentId = agent.getId();

            audit.recordMessage(GAME_MASTER_ID, agentId, new AgentScoreMessage(GAME_MASTER_ID, personalGain));
            totalGain += personalGain;

            if(agent instanceof BoSAgent bosAgent){
                agentSexes[bosAgent.getId()] = bosAgent.getAgentSex();
            }
        }

        audit.recordMessage(GAME_MASTER_ID, GAME_MASTER_ID, new TotalScoreMessage(GAME_MASTER_ID, totalGain));

        ReportMaker reportMaker = new ReportMaker();

        reportMaker.generateReport(numberOfAgents, gameType, fraction, probability, network, audit, agentSexes);

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
