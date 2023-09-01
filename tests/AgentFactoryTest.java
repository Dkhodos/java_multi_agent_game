
import Agent.*;
import Agent.AgentNetwork.AgentNetwork;
import Agent.BoSAgent.BoSAgentSex;
import Audit.Audit;
import Agent.BoSAgent.BoSAgent;
import Mailer.Mailer;
import ArgsSerializer.GameType;
import Agent.PDAgent.PDAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgentFactoryTest {
     Audit audit;
     Mailer mailer;
    AgentNetwork agentNetwork;

     @BeforeEach
     void setUp() {
         audit = new Audit();
         mailer = new Mailer(audit);
         agentNetwork = new AgentNetwork(1) {
             @Override
             public List<Integer> getNeighbors(int agentId) {
                 return List.of(0, 1); // Mock implementation for simplicity
             }
         };
     }

    @Test
    void testCreatePDAgent() {
        Agent agent = AgentFactory.createAgent(GameType.PD, 1,2, mailer, null, BoSAgentSex.WIFE);
        assertTrue(agent instanceof PDAgent, "Expected instance of PDAgent");
    }

    @Test
    void testCreateBoSAgent() {
        Agent agent = AgentFactory.createAgent(GameType.BoS, 1,2, mailer, null, BoSAgentSex.WIFE);
        assertTrue(agent instanceof BoSAgent, "Expected instance of BoSAgent");
    }

    @Test
    void testInvalidGameType() {
        assertThrows(IllegalArgumentException.class, () -> AgentFactory.createAgent(null, 1,2, mailer, null, BoSAgentSex.WIFE), "Expected IllegalArgumentException for null GameType");
    }

    @Test
    void testCreatePDAgents() {
        Agent[] agents = AgentFactory.createAgents(GameType.PD, 5, mailer, agentNetwork, 0);
        assertEquals(5, agents.length, "Expected 5 PDAgents");

        for (Agent agent : agents) {
            assertTrue(agent instanceof PDAgent, "Expected all agents to be instances of PDAgent");
        }
    }

    @Test
    void testCreateBoSAgents() {
        int numberOfAgents = 6;
        int maxWivesCount = 3; // Expected 3 wives and 3 husbands
        Agent[] agents = AgentFactory.createAgents(GameType.BoS, numberOfAgents, mailer, agentNetwork, maxWivesCount);
        assertEquals(numberOfAgents, agents.length, "Expected 6 BoSAgents");

        int wifeCount = 0, husbandCount = 0;
        for (Agent agent : agents) {
            assertTrue(agent instanceof BoSAgent, "Expected all agents to be instances of BoSAgent");
            BoSAgent boSAgent = (BoSAgent) agent;
            if (boSAgent.getAgentSex() == BoSAgentSex.WIFE) wifeCount++;
            else husbandCount++;
        }

        assertEquals(maxWivesCount, wifeCount, "Expected 3 WIFE BoSAgents");
        assertEquals(numberOfAgents - maxWivesCount, husbandCount, "Expected 3 HUSBAND BoSAgents");
    }
}
