package ReportMaker;

import Agent.AgentNetwork.*;
import Agent.PDAgent.PDStrategy;
import ArgsSerializer.*;
import Audit.*;
import Audit.Messages.AgentScoreMessage;
import Audit.Messages.RoundUpdateMessage;
import Audit.Messages.TotalScoreMessage;
import Logger.Logger;
import Mailer.Messages.*;
import Message.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ReportMaker {
    private static final Logger logger = new Logger("ReportMaker");

    final static String reportTemplateFile = System.getProperty("user.dir") + "/reports/report.template.html";

    public void generateReport(int numberOfAgents, GameType gameType,int fraction, double probability,
                               AgentNetwork network, Audit audit){
        try {
            String html = new String(Files.readAllBytes(Paths.get(reportTemplateFile)));

            html = html.replace("{{gameName}}", getGameName(gameType));
            html = html.replace("{{gameType}}", "\"" + gameType + "\"");
            html = html.replace("{{messages}}", auditToJavaScriptJsonString(audit));
            html = html.replace("{{connections}}", agentNetworkToJavaScriptJsonString(network));
            html = html.replace("{{numberOfAgents}}", String.valueOf(numberOfAgents));
            html = html.replace("{{fraction}}", String.valueOf(fraction));
            html = html.replace("{{probability}}", String.valueOf(probability));

            String reportDirectory = System.getProperty("user.dir") + "/reports";

            String reportFile = reportDirectory + "/report" + "." + gameType + ".html";
            Files.write(Paths.get(reportFile), html.getBytes());

            logger.info("Generated HTML report for " + getGameName(gameType) + ": " + reportFile);
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

            String AUDIT_OBJECT_TEMPLATE = "{type: \"%s\", sender: \"%d\", receiver: \"%d\", meta: `%s`},";

            if(message instanceof PDMessage){
                PDStrategy strategy = ((PDMessage) message).getStrategy();
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "PDMessage", sender,receiver, strategy));
            } else if(message instanceof PlayMessage){
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "PlayMessage", sender,receiver, ""));
            } else if (message instanceof BoSMessage boSMessage) {
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "BoSMessage", sender,
                        receiver, convertBoSAgentMessageToJavaScriptJson(boSMessage)));
            } else if (message instanceof RoundUpdateMessage){
                String round = String.valueOf(((RoundUpdateMessage) message).round());
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "RoundMessage", sender,receiver, round));
            } else if(message instanceof TotalScoreMessage){
                String score = String.valueOf(((TotalScoreMessage) message).score());
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "TotalScoreMessage", sender,receiver, score));
            } else if(message instanceof AgentScoreMessage agentScoreMessage){
                string.append(String.format(AUDIT_OBJECT_TEMPLATE, "AgentScoreMessage", sender,receiver,
                        convertAgentScoreMessageToJavaScriptJson(agentScoreMessage)));
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

    private String convertBoSAgentMessageToJavaScriptJson(BoSMessage message){
        String strategy = message.getStrategy().name();
        String sex = message.getAgentSex().name();

        return "{" +
                "\"strategy\": \"" + strategy + "\", " +
                "\"sex\": \"" + sex + "\"}";
    }

    private String convertAgentScoreMessageToJavaScriptJson(AgentScoreMessage message){
        int agentId = message.agentId();
        int score = message.score();

        return "{" +
                "\"agentId\": " + agentId + ", " +
                "\"score\": " + score + "}";
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
