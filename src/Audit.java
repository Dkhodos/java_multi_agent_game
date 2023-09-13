import java.util.ArrayList;
import java.util.List;

/**
 * Manages the recording of game messages for auditing purposes.
 */
public class Audit {
    private final List<RecordedMessage> recordedMessages = new ArrayList<>();

    /**
     * Records a game message with sender and receiver information.
     *
     * @param sender The ID of the message sender.
     * @param receiver The ID of the message receiver.
     * @param message The game message to be recorded.
     */
    public void recordMessage(int sender, int receiver, Message message) {
        recordedMessages.add(new RecordedMessage(sender, receiver, message));
    }

    /**
     * Retrieves the list of all recorded game messages.
     *
     * @return List of recorded game messages.
     */
    public List<RecordedMessage> getRecordedMessages(){
        return recordedMessages;
    }
}