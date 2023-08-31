package Mailer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Logger.Logger;
import Audit.*;

/*
 * used for communication among agents
 */
public class Mailer {
	Logger logger = new Logger("Mailer");

	private final HashMap<Integer, List<Message>> map = new HashMap<>();
	private final Audit audit;

	public Mailer(Audit audit){
		this.audit = audit;
	}

	public void send(int receiver, Message m) {
		List<Message> l = map.get(receiver);

		synchronized (l) {
			l.add(m);
			audit.recordMessage(m.getSenderId(), receiver, m);
		}
	}

	public Message readOne(int receiver) {
		List<Message> l = map.get(receiver);
		if (l.isEmpty()) {
			return null;
		}

		synchronized (l) {
			Message m = l.get(0);
			l.remove(0);
			return m;
		}
	}

	public void register(int agentId) {
		List<Message> l= new ArrayList<>();
		this.map.put(agentId, l);
	}
}
