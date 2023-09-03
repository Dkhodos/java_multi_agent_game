package Audit;

import Message.Message;

public record RecordedMessage(int sender, int receiver, Message message) {
}
