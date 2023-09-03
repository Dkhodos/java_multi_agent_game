package Agent.BoSAgent;

import Agent.Agent;
import Audit.Audit;
import Mailer.*;
import Mailer.Messages.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoSAgent extends Agent implements BoSPayoff {
    private final BoSAgentSex agentSex;
    private BoSStrategy strategy;
    private final Map<Integer, BoSNeighborData> neighborsData = new HashMap<>();

    public BoSAgent(int id, int numberOfAgents, Mailer mailer, Audit audit,List<Integer> neighbors, BoSAgentSex agentSex) {
        super(id, numberOfAgents, mailer, audit, neighbors);
        this.agentSex = agentSex;
    }

    @Override
    public int getPersonalGain(){
        int totalGain = 0;
        for (BoSNeighborData neighborData : neighborsData.values()) {
            totalGain += calculatePayoff(agentSex, strategy, neighborData.strategy());
        }
        return totalGain;
    }

    public BoSAgentSex getAgentSex() {
        return agentSex;
    }

    protected void handleMessage(MailerMessage message) {
        if(message instanceof BoSMessage bosMessage){
            BoSNeighborData neighborData = new BoSNeighborData(bosMessage.getStrategy(), bosMessage.getAgentSex());
            neighborsData.put(bosMessage.getSenderId(), neighborData);
        }
    }

    protected void pickStrategy() {
        BoSStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = pickRandomStrategy(random);
        } else {
            bestStrategy = pickBestStrategy(neighborsData, agentSex, strategy);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    protected void sendDecisionToNeighbors() {
        for (int neighborId : neighbors) {
            BoSMessage boSMessage = new BoSMessage(agentId, strategy, agentSex);
            mailer.send(neighborId, boSMessage);
            audit.recordMessage(agentId, neighborId, boSMessage);
        }
    }
}
