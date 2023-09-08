
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
         mailer = new Mailer();
         agentNetwork = new AgentNetwork(1) {
             @Override
             public List<Integer> getNeighbors(int agentId) {
                 return List.of(0, 1); // Mock implementation for simplicity
             }
         };
     }

    @Test
    void testCreatePDAgent() {
        Agent agent = AgentFactory.createAgent(GameType.PD, 1,2, mailer,audit, null, 0f);
        assertTrue(agent instanceof PDAgent, "Expected instance of PDAgent");
    }

    @Test
    void testCreateBoSAgent() {
        Agent agent = AgentFactory.createAgent(GameType.BoS, 1,2, mailer,audit, null, 0.5f);
        assertTrue(agent instanceof BoSAgent, "Expected instance of BoSAgent");
    }

    @Test
    void testInvalidGameType() {
        assertThrows(IllegalArgumentException.class, () -> AgentFactory.createAgent(null, 1,2, mailer,audit, null, 0f), "Expected IllegalArgumentException for null GameType");
    }

    @Test
    void testCreatePDAgents() {
        Agent[] agents = AgentFactory.createAgents(GameType.PD, 5, mailer,audit, agentNetwork, 0);
        assertEquals(5, agents.length, "Expected 5 PDAgents");

        for (Agent agent : agents) {
            assertTrue(agent instanceof PDAgent, "Expected all agents to be instances of PDAgent");
        }
    }

    @Test
    void testCreateBoSAgentsAllWives() {
        Agent[] agents = AgentFactory.createAgents(GameType.BoS, 5, mailer,audit, agentNetwork, 1f);
        assertEquals(5, agents.length, "Expected 5 PDAgents");

        for (Agent agent : agents) {
            assertTrue(agent instanceof BoSAgent, "Expected all agents to be instances of BoSAgent");
            assertEquals(BoSAgentSex.WIFE, ((BoSAgent) agent).getAgentSex(), "Expected all agents to be wives");
        }
    }

    @Test
    void testCreateBoSAgentsAllHusbands() {
        Agent[] agents = AgentFactory.createAgents(GameType.BoS, 5, mailer,audit, agentNetwork, 0f);
        assertEquals(5, agents.length, "Expected 5 PDAgents");

        for (Agent agent : agents) {
            assertTrue(agent instanceof BoSAgent, "Expected all agents to be instances of BoSAgent");
            assertEquals(BoSAgentSex.HUSBAND, ((BoSAgent) agent).getAgentSex(), "Expected all agents to be husbands");
        }
    }
}
