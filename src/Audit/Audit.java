package Audit;

import Mailer.Message;
import Mailer.PDMessage;
import Mailer.PlayMessage;
import PDAgent.PDStrategy;
import PDAgent.PDStrategy;

import java.util.ArrayList;
import java.util.List;

public class Audit {
    static String OBJECT_TEMPLATE = "{type: \"%s\", sender: \"%d\", receiver: \"%d\", strategy: \"%s\"},";
    private final List<RecordedMessage> recordedMessages = new ArrayList<>();

    public void recordMessage(int sender, int receiver, Message message) {
        recordedMessages.add(new RecordedMessage(sender, receiver, message));
    }

    public List<RecordedMessage> getRecordedMessages() {
        return recordedMessages;
    }

    public String toJavaScriptJsonString(){
        StringBuilder string = new StringBuilder();
        string.append("[");

        for (RecordedMessage message: recordedMessages){
            if(message.message() instanceof PDMessage){
                PDStrategy strategy = ((PDMessage) message.message()).getStrategy();
                string.append(String.format(OBJECT_TEMPLATE, "PDMessage", message.sender(),message.receiver(), String.valueOf(strategy)));
            } else if(message.message() instanceof PlayMessage){
                string.append(String.format(OBJECT_TEMPLATE, "PlayMessage", message.sender(),message.receiver(), ""));
            }
        }

        string.append("]");

        return string.toString();
    }
}