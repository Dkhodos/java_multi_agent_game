package ReportMaker;

import Agent.AgentNetwork.*;
import Agent.BoSAgent.*;
import Agent.PDAgent.PDStrategy;
import ArgsSerializer.*;
import Audit.*;
import Mailer.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ReportMaker {

    final static String reportTemplateFile = System.getProperty("user.dir") + "/reports/report.template.html";

    public void generateReport(int numberOfAgents, GameType gameType,int fraction, double probability,
                               AgentNetwork network, Audit audit, BoSAgentSex[] bosAgentsSex){
        try {
            String html = new String(Files.readAllBytes(Paths.get(reportTemplateFile)));

            html = html.replace("{{gameName}}", getGameName(gameType));
            html = html.replace("{{data}}", auditToJavaScriptJsonString(audit));
            html = html.replace("{{connections}}", agentNetworkToJavaScriptJsonString(network));
            html = html.replace("{{numberOfAgents}}", String.valueOf(numberOfAgents));
            html = html.replace("{{agentsSex}}", convertAgentSexArrayToJavaScriptArray(bosAgentsSex));
            html = html.replace("{{fraction}}", String.valueOf(fraction));
            html = html.replace("{{probability}}", String.valueOf(probability));

            String reportDirectory = System.getProperty("user.dir") + "/reports";

            String reportFile = reportDirectory + "/report" + "." + gameType + ".html";
            Files.write(Paths.get(reportFile), html.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String auditToJavaScriptJsonString(Audit audit){
        StringBuilder string = new StringBuilder();
        string.append("[");

        for (RecordedMessage recordedMessage: audit.getRecordedMessages()){
            Message message = recordedMessage.message();
            int sender = recordedMessage.sender();
            int receiver = recordedMessage.receiver();

            String AUDIT_OBJECT_TEMPLATE = "{type: \"%s\", sender: \"%d\", receiver: \"%d\", meta: \"%s\"},";

            if(message instanceof PDMessage){
                PDStrategy strategy = ((PDMessage) message).getStrategy();
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "PDMessage", sender,receiver, strategy));
            } else if(message instanceof PlayMessage){
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "PlayMessage", sender,receiver, ""));
            } else if (message instanceof BoSMessage) {
                BoSStrategy strategy = ((BoSMessage) message).getStrategy();
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "BoSMessage", sender,receiver, strategy));
            } else if (message instanceof RoundUpdateMessage){
                String round = String.valueOf(((RoundUpdateMessage) message).getRound());
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "RoundMessage", sender,receiver, round));
            } else if(message instanceof TotalScoreMessage){
                String score = String.valueOf(((TotalScoreMessage) message).getScore());
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "TotalScoreMessage", sender,receiver, score));
            } else if(message instanceof AgentScoreMessage){
                String score = String.valueOf(((AgentScoreMessage) message).getScore());
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "AgentScoreMessage", sender,receiver, score));
            }
        }

        string.append("]");

        return string.toString();
    }

    public String agentNetworkToJavaScriptJsonString(AgentNetwork network){
        StringBuilder string = new StringBuilder();
        string.append("{");

        String OBJECT_TEMPLATE = "%s: [%s],";

        for (Map.Entry<Integer, List<Integer>> entry: network.getNetwork().entrySet()){
            int agentId = entry.getKey();
            List<Integer> neighbors = entry.getValue();

            StringBuilder neighborsString = new StringBuilder();
            for(int neighbor : neighbors){
                neighborsString.append(neighbor).append(", ");
            }

            string.append(String.format(OBJECT_TEMPLATE, agentId, neighborsString));
        }

        string.append("}");

        return string.toString();
    }

    private String convertAgentSexArrayToJavaScriptArray(BoSAgentSex[] bosAgentsSex) {
        if (bosAgentsSex[0] == null) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < bosAgentsSex.length; i++) {
            builder.append("\"").append(bosAgentsSex[i].name()).append("\"");
            if (i < bosAgentsSex.length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private String getGameName(GameType gameType){
        switch (gameType){
            case PD -> {
                return "Prisoners Dilemma";
            }
            case BoS ->  {
                return "Battle of The Sexes";
            }
        }
        return "Unknown";
    }
}
