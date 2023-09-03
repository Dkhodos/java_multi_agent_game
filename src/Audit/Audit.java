package Audit;

import Mailer.Messages.*;
import java.util.ArrayList;
import java.util.List;

public class Audit {
    private final List<RecordedMessage> recordedMessages = new ArrayList<>();

    public void recordMessage(int sender, int receiver, Message message) {
        recordedMessages.add(new RecordedMessage(sender, receiver, message));
    }

    public List<RecordedMessage> getRecordedMessages(){
        return recordedMessages;
    }
}