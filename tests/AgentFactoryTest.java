
import Agent.*;
import BoSAgent.BoSAgent;
import Mailer.Mailer;
import ArgsSerializer.GameType;
import PDAgent.PDAgent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgentFactoryTest {

    @Test
    void testCreatePDAgent() {
        Agent agent = AgentFactory.createAgent(GameType.PD, 1,2, new Mailer(), null, 0);
        assertTrue(agent instanceof PDAgent, "Expected instance of PDAgent");
    }

    @Test
    void testCreateBoSAgent() {
        Agent agent = AgentFactory.createAgent(GameType.BoS, 1,2, new Mailer(), null, 0);
        assertTrue(agent instanceof BoSAgent, "Expected instance of BoSAgent");
    }

    @Test
    void testInvalidGameType() {
        assertThrows(IllegalArgumentException.class, () -> AgentFactory.createAgent(null, 1,2, new Mailer(), null, 0), "Expected IllegalArgumentException for null GameType");
    }
}
