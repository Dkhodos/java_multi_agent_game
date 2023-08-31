package Audit;

import Mailer.Message;

public record RecordedMessage(int sender, int receiver, Message message) {
}
