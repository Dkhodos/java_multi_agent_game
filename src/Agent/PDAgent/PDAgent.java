package Agent.PDAgent;

import Agent.Agent;
import Audit.Audit;
import Mailer.*;
import Mailer.Messages.MailerMessage;
import Mailer.Messages.PDMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDAgent extends Agent implements PBPayoff {
    private final Map<Integer, PDStrategy> neighborsStrategies = new HashMap<>();
    private PDStrategy strategy;

    public PDAgent(int id, int numberOfAgents, Mailer mailer, Audit audit,List<Integer> neighbors) {
        super(id, numberOfAgents, mailer, audit, neighbors);
    }

    public int getPersonalGain() {
        int totalGain = 0;
        for (PDStrategy neighborStrategy : neighborsStrategies.values()) {
            totalGain += calculatePayoff(strategy, neighborStrategy);
        }
        return totalGain;
    }

    @Override
    protected void handleMessage(MailerMessage message) {
        if(message instanceof PDMessage pdMessage){
            neighborsStrategies.put(pdMessage.getSenderId(), pdMessage.getStrategy());
        }
    }

    protected void pickStrategy() {
        PDStrategy bestStrategy;

        if(strategy == null){
            bestStrategy = pickRandomStrategy(random);
        } else {
            bestStrategy = pickBestStrategy(neighborsStrategies);
        }

        strategyChanged = (strategy != bestStrategy);
        strategy = bestStrategy;
    }

    protected void sendDecisionToNeighbors() {
        for (int neighborId : neighbors) {
            PDMessage pdMessage = new PDMessage(getId(), strategy);
            mailer.send(neighborId, pdMessage);
            audit.recordMessage(agentId, neighborId, pdMessage);
        }
    }
}