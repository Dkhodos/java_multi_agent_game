import Agent.PDAgent.*;
import Audit.Audit;
import Mailer.Mailer;
import Mailer.Messages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestPDAgent {
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
        PDAgent agent = createAgentWithArguments();

        Thread t = new Thread(agent);

        t.start();

        t.join(300);

        assertNull(mailer.readOne(neighborId));
    }

    @Test
    public void testAgentRunsAndSendsMessageToNeighbor() throws InterruptedException {
        PDAgent agent = createAgentWithArguments();

        Thread t = new Thread(agent);

        mailer.send(agentId, new PlayMessage(agentId));

        t.start();
        t.join();

        MailerMessage message1 = mailer.readOne(neighborId);
        assertNotNull(message1);
        assertTrue(message1 instanceof PDMessage);
        assertEquals(message1.getSenderId(), agentId);

        MailerMessage message2 = mailer.readOne(neighborId);
        assertNotNull(message2);
        assertTrue(message2 instanceof PlayMessage);
        assertEquals(message1.getSenderId(), agentId);
    }

    @Test
    public void testAgentDoesNotSentMessageTwice() throws InterruptedException {
        PDAgent agent = createAgentWithArguments();

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
    public void testAgentPicksDefectWhenNeighborCooperates() throws InterruptedException{
        testPickedStrategyByAgent(PDStrategy.COOPERATE, PDPayoff.I_DEFECT_HE_COOPERATE);
    }

    @Test
    public void testAgentPicksDefectWhenNeighborDefects() throws InterruptedException{
        testPickedStrategyByAgent(PDStrategy.DEFECT, PDPayoff.BOTH_DEFECT);
    }


    private PDAgent createAgentWithArguments(){
        PDAgent agent = new PDAgent(0, 2, mailer, audit, neighbors);
        mailer.register(agentId);
        mailer.register(neighborId);

        return agent;
    }

    private void testPickedStrategyByAgent(PDStrategy neighborStrategy, int expectedGain) throws InterruptedException {
        PDAgent agent = createAgentWithArguments();

        // let agent pick his random strategy
        mailer.send(agentId, new PlayMessage(agentId));
        Thread t1 = new Thread(agent);
        t1.start();
        t1.join();

        // send the agent that neighbor picked neighborStrategy
        mailer.send(agentId, new PDMessage(neighborId, neighborStrategy));

        // restart agent pick his best strategy
        mailer.send(agentId, new PlayMessage(agentId));
        Thread t2 = new Thread(agent);
        t2.start();
        t2.join();

        // strategy remained or change to DEFECT
        assertEquals(agent.getStrategy(), PDStrategy.DEFECT);
        assertEquals(agent.getPersonalGain(), expectedGain);
    }
}
