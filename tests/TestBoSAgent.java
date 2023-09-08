import Agent.BoSAgent.*;
import Audit.Audit;
import Mailer.Mailer;
import Mailer.Messages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoSAgent {
    Mailer mailer;
    Audit audit;
    List<Integer> neighbors;
    int agentId;
    int neighborId;
    @BeforeEach
    public void setup() {
        mailer = new Mailer();
        audit = new Audit();
        neighbors = List.of(1);
        agentId = 0;
        neighborId = neighbors.get(0);
    }

    @Test
    public void testAgentStuckWithoutPlayMessage() throws InterruptedException {
        BoSAgent agent = createAgentWithArguments();

        Thread t = new Thread(agent);

        t.start();

        t.join(300);

        assertNull(mailer.readOne(neighborId));
    }

    @Test
    public void testAgentRunsAndSendsMessageToNeighbor() throws InterruptedException {
        BoSAgent agent = createAgentWithArguments();

        Thread t = new Thread(agent);

        mailer.send(agentId, new PlayMessage(agentId));

        t.start();
        t.join();

        MailerMessage message1 = mailer.readOne(neighborId);
        assertNotNull(message1);
        assertTrue(message1 instanceof BoSMessage);
        assertEquals(message1.getSenderId(), agentId);

        MailerMessage message2 = mailer.readOne(neighborId);
        assertNotNull(message2);
        assertTrue(message2 instanceof PlayMessage);
        assertEquals(message1.getSenderId(), agentId);
    }

    @Test
    public void testAgentDoesNotSentMessageTwice() throws InterruptedException {
        BoSAgent agent = createAgentWithArguments();

        Thread t1 = new Thread(agent);

        mailer.send(agentId, new PlayMessage(agentId));

        t1.start();
        t1.join();

        MailerMessage message1 = mailer.readOne(neighborId);
        assertNotNull(message1);


        Thread t2 = new Thread(agent);
        mailer.send(agentId, new PlayMessage(agentId));

        t2.start();
        t2.join();

        MailerMessage message2 = mailer.readOne(neighborId);
        assertNotNull(message2);
        assertTrue(message2 instanceof PlayMessage);
        assertEquals(message2.getSenderId(), neighborId);
    }

    @Test
    public void testAgentSexHusbandWhenFractionIsZero(){
        BoSAgent agent = createAgentWithArguments(0);
        assertEquals(BoSAgentSex.HUSBAND, agent.getAgentSex());
    }

    @Test
    public void testAgentSexHusbandWhenFractionIsOne(){
        BoSAgent agent = createAgentWithArguments(1);
        assertEquals(BoSAgentSex.WIFE, agent.getAgentSex());
    }


    private BoSAgent createAgentWithArguments(float fraction){
        BoSAgent agent = new BoSAgent(0, 2, mailer, audit, neighbors, fraction);
        mailer.register(agentId);
        mailer.register(neighborId);

        return agent;
    }

    private BoSAgent createAgentWithArguments(){
        return createAgentWithArguments(0.5f);
    }
}
