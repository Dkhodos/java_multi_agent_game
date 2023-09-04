package GameExecutor;

import Agent.*;
import Agent.AgentNetwork.*;
import ArgsSerializer.*;
import Audit.Audit;
import Logger.Logger;
import Mailer.*;
import Mailer.Messages.*;
import Audit.Messages.*;

/**
 * Executes the game based on provided arguments.
 * Handles the game setup, agent initialization, and the game loop.
 */
public class GameExecutor {
    private static final int GAME_MASTER_ID = -1;
    private static final Logger logger = new Logger("GameExecutor");

    private final Mailer mailer;
    private final GameArguments arguments;
    private final Audit audit;

    /**
     * Initializes the GameExecutor with specified game arguments.
     *
     * @param arguments The arguments for the game.
     */
    public GameExecutor(GameArguments arguments){
        this.audit = new Audit();
        this.mailer = new Mailer();
        this.arguments = arguments;
    }

    /**
     * Executes the game loop and returns the results.
     *
     * @return The results of the game execution.
     * @throws InterruptedException If the game execution is interrupted.
     */
    public GameExecutorResults runGame() throws InterruptedException {
        logger.info("Running the game :)");


        /* 1. Extract game arguments  */
        int numberOfAgents = this.arguments.numberOfAgents();
        double probability = this.arguments.probability();
        GameType gameType = this.arguments.gameType();
        int fraction = this.arguments.fraction();

        /* 2. Initialize agents network */
        NetworkGenerator generator = new NetworkGenerator(probability, numberOfAgents);
        AgentNetwork network = generator.generateNetwork();
        logger.info("Initialized network");
        network.print();


        /* 3. Create agents by using a factory and register them in mailer */
        Agent[] agents = AgentFactory.createAgents(gameType, numberOfAgents, mailer, audit, network, fraction);
        logger.info("Agents created and registered to the mailer");

        /* 4. start game rounds, while first round we initialize the agents */
        logger.info("Running scenario with given params");
        int totalRounds = runGame(agents);

        /* 6. report agent personal gains (score) */
        reportAgentScores(agents);

        /* 7. get total gain */
        int totalGain = getTotalGain(agents);

        /* 8. audit total gain */
        reportTotalScore(totalGain);

        /* 9. return scores */
        return new GameExecutorResults(totalGain, totalRounds, network, audit);
    }

    /**
     * Executes multiple game rounds until all agents' strategies are stable.
     *
     * @param agents Array of game agents.
     * @return Total number of game rounds executed.
     * @throws InterruptedException If the game execution is interrupted.
     */
    private int runGame(Agent [] agents) throws InterruptedException {
        boolean allAgentsStable;

        // we start with -1 because in the first round every agent randomly select his strategy and updates his neighbors
        int rounds = -1;

        do {
            // start with an assumption that all agents are ready
            allAgentsStable = true;

            // increment round counter and audit it for the report
            rounds++;
            reportRound(rounds);
            logger.info("Running round: " + (rounds != 0 ? rounds : "init"));

            // generate new threads (easier the restarting them at the end of a round)
            Thread [] threads = createAgentThreads(agents);

            // initiate the first agent to start the round
            triggerFirstAgent();

            // start all threads
            for (Thread t : threads) t.start();

            // wait for all agents to terminate
            for (Thread t : threads) t.join();

            // check agent strategies, if one of the changed a strategy we need another round (not a stable round)
            logger.info("Round: " + rounds + " concluded, verifying agents decision stability");
            for (Agent agent : agents) {
                if (agent.hasStrategyChanged()) {
                    allAgentsStable = false;
                    break;
                }
            }
        } while (!allAgentsStable);

        logger.info("Concluded all rounds");

        // all done result the total accumulated rounds
        return rounds;
    }

    /**
     * Generates threads for each agent for concurrent execution.
     *
     * @param agents Array of game agents.
     * @return Array of threads for each agent.
     */
    private Thread [] createAgentThreads(Agent[] agents){
        Thread[] threads = new Thread[agents.length];
        for (int i = 0; i < agents.length; i++) {
            threads[i] = new Thread(agents[i]);
        }
        return threads;
    }

    /**
     * Initiates the first agent to start the game round.
     */
    private void triggerFirstAgent(){
        PlayMessage playMessage = new PlayMessage(0);
        mailer.send(0, playMessage);
        audit.recordMessage(GAME_MASTER_ID, 0, playMessage);
    }

    /**
     * Records the current game round to the audit.
     *
     * @param round Current game round.
     */
    private void reportRound(int round){
        audit.recordMessage(GAME_MASTER_ID, GAME_MASTER_ID, new RoundUpdateMessage(round));
    }

    /**
     * Records the scores of each agent to the audit.
     *
     * @param agents Array of game agents.
     */
    private void reportAgentScores(Agent[] agents){
        for (Agent agent : agents) {
            int personalGain = agent.getPersonalGain();
            int agentId = agent.getId();

            audit.recordMessage(GAME_MASTER_ID, GAME_MASTER_ID, new AgentScoreMessage(agentId, personalGain));
            logger.info("Agent " + agentId + " earned a score of " + personalGain);
        }
    }


    /**
     * Calculates the total score (gain) across all agents.
     *
     * @param agents Array of game agents.
     * @return Total score.
     */
    private int getTotalGain(Agent[] agents){
        int totalGain = 0;
        for (Agent agent : agents) {
            totalGain += agent.getPersonalGain();
        }
        return totalGain;
    }

    /**
     * Records the total score of the game to the audit.
     *
     * @param score Total game score.
     */
    private void reportTotalScore(int score){
        audit.recordMessage(GAME_MASTER_ID, GAME_MASTER_ID, new TotalScoreMessage(score));
    }
}
