package Audit;

import Mailer.Messages.MailerMessage;

public record RecordedMessage(int sender, int receiver, MailerMessage message) {
}
