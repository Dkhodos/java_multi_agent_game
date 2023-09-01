
import Agent.*;
import Audit.Audit;
import BoSAgent.BoSAgent;
import Mailer.Mailer;
import ArgsSerializer.GameType;
import PDAgent.PDAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgentFactoryTest {
     Audit audit;
     Mailer mailer;

     @BeforeEach
     void setUp() {
         audit = new Audit();
         mailer = new Mailer(audit);
     }

    @Test
    void testCreatePDAgent() {
        Agent agent = AgentFactory.createAgent(GameType.PD, 1,2, mailer, null, 0);
        assertTrue(agent instanceof PDAgent, "Expected instance of PDAgent");
    }

    @Test
    void testCreateBoSAgent() {
        Agent agent = AgentFactory.createAgent(GameType.BoS, 1,2, mailer, null, 0);
        assertTrue(agent instanceof BoSAgent, "Expected instance of BoSAgent");
    }

    @Test
    void testInvalidGameType() {
        assertThrows(IllegalArgumentException.class, () -> AgentFactory.createAgent(null, 1,2, mailer, null, 0), "Expected IllegalArgumentException for null GameType");
    }
}
