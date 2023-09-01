package Audit;

import Agent.BoSAgent.BoSStrategy;
import Mailer.*;
import Agent.PDAgent.PDStrategy;

import java.util.ArrayList;
import java.util.List;

public class Audit {
    static String OBJECT_TEMPLATE = "{type: \"%s\", sender: \"%d\", receiver: \"%d\", meta: \"%s\"},";
    private final List<RecordedMessage> recordedMessages = new ArrayList<>();

    public void recordMessage(int sender, int receiver, Message message) {
        recordedMessages.add(new RecordedMessage(sender, receiver, message));
    }
    public String toJavaScriptJsonString(){
        StringBuilder string = new StringBuilder();
        string.append("[");

        for (RecordedMessage recordedMessage: recordedMessages){
            Message message = recordedMessage.message();
            int sender = recordedMessage.sender();
            int receiver = recordedMessage.receiver();

            if(message instanceof PDMessage){
                PDStrategy strategy = ((PDMessage) message).getStrategy();
                string.append(String.format(OBJECT_TEMPLATE, "PDMessage", sender,receiver, strategy));
            } else if(message instanceof PlayMessage){
                string.append(String.format(OBJECT_TEMPLATE, "PlayMessage", sender,receiver, ""));
            } else if (message instanceof BoSMessage) {
                BoSStrategy strategy = ((BoSMessage) message).getStrategy();
                string.append(String.format(OBJECT_TEMPLATE, "BoSMessage", sender,receiver, strategy));
            } else if (message instanceof RoundUpdateMessage){
                String round = String.valueOf(((RoundUpdateMessage) message).getRound());
                string.append(String.format(OBJECT_TEMPLATE, "RoundMessage", sender,receiver, round));
            } else if(message instanceof TotalScoreMessage){
                String score = String.valueOf(((TotalScoreMessage) message).getScore());
                string.append(String.format(OBJECT_TEMPLATE, "TotalScoreMessage", sender,receiver, score));
            } else if(message instanceof AgentScoreMessage){
                String score = String.valueOf(((AgentScoreMessage) message).getScore());
                string.append(String.format(OBJECT_TEMPLATE, "AgentScoreMessage", sender,receiver, score));
            }
        }

        string.append("]");

        return string.toString();
    }
}