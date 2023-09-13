import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * used for communication among agents
 */
public class Mailer {
	private final HashMap<Integer, List<MailerMessage>> map = new HashMap<>();

	public void send(int receiver, MailerMessage m) {
		List<MailerMessage> l = map.get(receiver);

		synchronized (l) {
			l.add(m);
		}
	}

	public MailerMessage readOne(int receiver) {
		List<MailerMessage> l = map.get(receiver);
		if (l.isEmpty()) {
			return null;
		}

		synchronized (l) {
			MailerMessage m = l.get(0);
			l.remove(0);
			return m;
		}
	}

	public void register(int agentId) {
		List<MailerMessage> l= new ArrayList<>();
		this.map.put(agentId, l);
	}
}
