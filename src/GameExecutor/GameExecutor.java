package GameExecutor;

import Agent.*;
import Agent.AgentNetwork.*;
import Agent.BoSAgent.*;
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
        /* 1. Extract game arguments  */
        int numberOfAgents = this.arguments.numberOfAgents();
        double probability = this.arguments.probability();
        GameType gameType = this.arguments.gameType();
        int fraction = this.arguments.fraction();

        /* 2. Initialize agents network */
        NetworkGenerator generator = new NetworkGenerator(probability, numberOfAgents);
        AgentNetwork network = generator.generateNetwork();

        Agent[] agents = new Agent[numberOfAgents];
        int createdAgents = 0;

        /* 3. Create agents by using a factory and register them in mailer */
        for (int i = 0; i < numberOfAgents; i++) {
            BoSAgentSex bosAgentSex = BoSAgentSex.WIFE;
            if (fraction != 0){
                bosAgentSex = createdAgents < fraction ? BoSAgentSex.WIFE : BoSAgentSex.HUSBAND;
                createdAgents++;
            }

            agents[i] = AgentFactory.createAgent(gameType, i, numberOfAgents, mailer, network.getNeighbors(i), bosAgentSex);
            mailer.register(i);
        }

        /* 4. start game rounds, while first round we initialize the agents */
        int totalRounds = runGame(agents);


        /* 5. calculate gains */
        int totalGain = 0;
        BoSAgentSex[] agentSexes = new BoSAgentSex[numberOfAgents];
        for (Agent agent : agents) {
            int personalGain = agent.getPersonalGain();
            int agentId = agent.getId();

            reportAgentScore(agentId, personalGain);
            totalGain += personalGain;

            if(agent instanceof BoSAgent bosAgent){
                agentSexes[bosAgent.getId()] = bosAgent.getAgentSex();
            }
        }

        /* 6. manage reports */
        reportTotalScore(totalGain);
        ReportMaker reportMaker = new ReportMaker();
        reportMaker.generateReport(numberOfAgents, gameType, fraction, probability, network, audit, agentSexes);

        /* 7. return scores */
        return new GameExecutorResults(totalGain, totalRounds);
    }

    private int runGame(Agent [] agents) throws InterruptedException {
        boolean allAgentsStable;
        int rounds = -1;

        do {
            // start with an assumption that all agents are ready
            allAgentsStable = true;

            // increment round counter and audit it for the report
            rounds++;
            reportRound(rounds);

            // generate new threads (easier the restarting them at the end of a round)
            Thread [] threads = getThreads(agents);

            // initiate the first agent to start the round
            triggerFirstAgent();

            // start all threads
            for (Thread t : threads) {
                t.start();
            }

            // wait for all agents to terminate
            for (Thread t : threads) {
                t.join();
            }

            // check agent strategies, if one of the changed a strategy we need another round (not a stable round)
            for (Agent agent : agents) {
                if (agent.hasStrategyChanged()) {
                    allAgentsStable = false;
                    break;
                }
            }
        } while (!allAgentsStable);

        // all done result the total accumulated rounds
        return rounds;
    }

    private Thread [] getThreads(Agent[] agents){
        Thread[] threads = new Thread[agents.length];
        for (int i = 0; i < agents.length; i++) {
            threads[i] = new Thread(agents[i]);
        }
        return threads;
    }

    private void triggerFirstAgent(){
        mailer.send(0, new PlayMessage(0));
    }

    private void reportRound(int round){
        audit.recordMessage(GAME_MASTER_ID, GAME_MASTER_ID, new RoundUpdateMessage(GAME_MASTER_ID, round));
    }

    private void reportAgentScore(int agentId, int score){
        audit.recordMessage(GAME_MASTER_ID, agentId, new AgentScoreMessage(GAME_MASTER_ID, score));
    }

    private void reportTotalScore(int score){
        audit.recordMessage(GAME_MASTER_ID, GAME_MASTER_ID, new TotalScoreMessage(GAME_MASTER_ID, score));
    }
}
