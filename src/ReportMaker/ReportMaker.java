package ReportMaker;

import AgentNetwork.*;
import ArgsSerializer.*;
import Audit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportMaker {
    final static String reportTemplateFile = System.getProperty("user.dir") + "/reports/report.template.html";

    public void generateReport(int numberOfAgents, GameType gameType, AgentNetwork network, Audit audit){
        try {
            String html = new String(Files.readAllBytes(Paths.get(reportTemplateFile)));

            html = html.replace("{{gameName}}", gameType.toString());
            html = html.replace("{{data}}", audit.toJavaScriptJsonString());
            html = html.replace("{{connections}}", network.toJavaScriptJsonString());
            html = html.replace("{{numberOfAgents}}", String.valueOf(numberOfAgents));

            String reportDirectory = System.getProperty("user.dir") + "/reports";

            String reportFile = reportDirectory + "/report" + "." + gameType + ".html";
            Files.write(Paths.get(reportFile), html.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
