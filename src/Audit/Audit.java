package Audit;

import Agent.BoSAgent.BoSStrategy;
import Mailer.BoSMessage;
import Mailer.Message;
import Mailer.PDMessage;
import Mailer.PlayMessage;
import Agent.PDAgent.PDStrategy;

import java.util.ArrayList;
import java.util.List;

public class Audit {
    static String OBJECT_TEMPLATE = "{type: \"%s\", sender: \"%d\", receiver: \"%d\", strategy: \"%s\"},";
    private final List<RecordedMessage> recordedMessages = new ArrayList<>();

    public void recordMessage(int sender, int receiver, Message message) {
        recordedMessages.add(new RecordedMessage(sender, receiver, message));
    }
    public String toJavaScriptJsonString(){
        StringBuilder string = new StringBuilder();
        string.append("[");

        for (RecordedMessage message: recordedMessages){
            if(message.message() instanceof PDMessage){
                PDStrategy strategy = ((PDMessage) message.message()).getStrategy();
                string.append(String.format(OBJECT_TEMPLATE, "PDMessage", message.sender(),message.receiver(), strategy));
            } else if(message.message() instanceof PlayMessage){
                string.append(String.format(OBJECT_TEMPLATE, "PlayMessage", message.sender(),message.receiver(), ""));
            } else if (message.message() instanceof BoSMessage) {
                BoSStrategy strategy = ((BoSMessage) message.message()).getStrategy();
                string.append(String.format(OBJECT_TEMPLATE, "BoSMessage", message.sender(),message.receiver(), strategy));
            }
        }

        string.append("]");

        return string.toString();
    }
}