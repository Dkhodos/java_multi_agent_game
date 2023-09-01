package ReportMaker;

import Agent.AgentNetwork.*;
import Agent.BoSAgent.*;
import ArgsSerializer.*;
import Audit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportMaker {
    final static String reportTemplateFile = System.getProperty("user.dir") + "/reports/report.template.html";

    public void generateReport(int numberOfAgents, GameType gameType, AgentNetwork network, Audit audit, BoSAgentSex[] bosAgentsSex){
        try {
            String html = new String(Files.readAllBytes(Paths.get(reportTemplateFile)));

            html = html.replace("{{gameName}}", getGameName(gameType));
            html = html.replace("{{data}}", audit.toJavaScriptJsonString());
            html = html.replace("{{connections}}", network.toJavaScriptJsonString());
            html = html.replace("{{numberOfAgents}}", String.valueOf(numberOfAgents));
            html = html.replace("{{agentsSex}}", convertAgentSexArrayToJavaScriptArray(bosAgentsSex));

            String reportDirectory = System.getProperty("user.dir") + "/reports";

            String reportFile = reportDirectory + "/report" + "." + gameType + ".html";
            Files.write(Paths.get(reportFile), html.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                return "Prisoner Dilemma";
            }
            case BoS ->  {
                return "Battle of the sexes";
            }
        }
        return "Unknown";
    }
}
