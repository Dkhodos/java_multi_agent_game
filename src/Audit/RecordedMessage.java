package Audit;

import Mailer.Messages.Message;

public record RecordedMessage(int sender, int receiver, Message message) {
}
