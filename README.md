# Game Simulation

This project provides a simulation of various game strategies based on specified scenarios and agent interactions. Agents can communicate, make decisions, and react based on the actions of other agents. The project aims to study the dynamics of agent interactions and strategies in different settings.
![Maze](static/reports_image.png)

## Project Structure:

- 📁 **.github** - GitHub actions for automatic test (CI).
- 📁 **.idea** - IntelliJ project files.
- 📁 **reports** - HTML report template, and the folder that the temp reports are generated for.
- 📁 **src** - the actual Java code.
- 📁 **static** - a resource folder, mostly for the readme and the reports.
- 📁 **tests** - tests folder, with its own test data.
- 📄 **java_multi_agent_game.iml** - class path dependencies (for IntelliJ projects).

## Features:

- **Agent Interactions**: Agents can communicate, share strategies, and make decisions based on the state of the game and the actions of other agents.
- **Network Generation**: Ability to generate agent networks based on specified probabilities.
- **Audit Logging**: Record and track all agent messages and decisions for verification and analysis.
- **Strategy Implementations**: Implement various game strategies like Prisoner's Dilemma and Battle of the Sexes.
- **Game Execution**: Simulate multiple rounds of games until agent strategies stabilize.
- **Exception Handling**: Handle various exceptions, such as invalid game settings or not providing enough arguments.
- **Report Generation**: Generates an HTML report for both "Battle of the Sexes (BoS)" and "Prisoners Dilemma (PD)".
- **Message Communication**: Ability for agents to send and receive various types of messages.
- **Testing**: Comprehensive unit tests for different parts of the project.

## Classes and Components:

- **Logger**: Manages logging functionalities.
- **GameExecutor**: Handles the game simulation's execution flow.
- **ReportMaker**: Responsible for generating reports.
- **AgentNetwork**: Represents the network of agents and their connections.
- **Audit**: Manages the recording of agent messages and decisions.
- **ArgsSerializer**: Handles serialization of game arguments.
- **Mailer**: Handles agent communication.
- **AgentFactory**: Provides factory methods to create agents.
- **GameArguments**: Holds the game simulation settings.
- **Agent**: Represents individual agents with unique strategies and decision-making capabilities.
- **NetworkGenerator**: Generates agent networks.
- **Main**: The main entry point of the application.
- **GameExecutorResults**: Contains the results of a game execution.
- **Various Messages**: Classes like `PDMessage`, `MailerMessage`, `PlayMessage`, `BoSMessage`, and others represent different types of messages exchanged between agents.
- **Enums**: Enums such as `GameType`, `BoSAgentSex`, `BoSStrategy`, and `PDStrategy` represent various categories and strategies in the simulation.
- **Interfaces and Records**: Components like `PBPayoff`, `BoSPayoff`, and `BoSNeighborData` help to define structured data and expected behaviors.

## Usage:

### Main Program:

- The Main program expects specified game arguments to run the simulation.

  #### In Intelij IDE:
- Click the green run arrow in `src/Main.java` with this config:
  ![IntelliJ Runner](static/run_intelij.png)

  #### In Eclipse IDE:

- Set up the run configuration to include the necessary game arguments.

  #### In shell (mac/ linux):

- Run:
  ```shell
  chmod +x compile_and_run.sh
  ```
- Then, run:
  ```shell
  ./compile_and_run.sh <number_of_agents:int> <probability_of_connection:double> <PD: gameType>
  ```
  or
  ```shell
  ./compile_and_run.sh <number_of_agents:int> <probability_of_connection:double> <BoS: gameType> <friction:double>
  ```
  
## Logging
The projects include logging. You can use `Settings.DEBUG=true` to get logs of the entire process.
## Tests:

### **NetworkGeneratorTest**:
- `testGenerateNetwork`: Verifies the generation of an agent network with a specified probability and number of agents.
- `testGenerateNetworkWithZeroProbability`: Ensures the network generation with zero connection probability results in no agent connections.
- `testGenerateNetworkWithFullProbability`: Validates that a network with full connection probability connects all agents.

### **AgentNetworkTest**:
- `testConnect`: Validates the connection between two agents in the network.
- `testLock`: Checks if the network is locked, preventing any new connections.

### **ArgsSerializerTest**:
- `testNoArguments`: Validates the behavior when no arguments are provided.
- `testTwoArguments`: Ensures correct behavior with two arguments.
- `testValidPDGameWithThreeArguments`: Validates a valid PD game setup with three arguments.
- `testInvalidBoSGameWithThreeArguments`: Checks for errors in an invalid BoS game setup with three arguments.
- `testValidBoSGameWithFourArguments`: Validates a valid BoS game setup with four arguments.
- `testInvalidGameType`: Ensures correct behavior with an invalid game type.

### **AgentFactoryTest**:
- `testCreatePDAgent`: Checks the creation of a PD Agent.
- `testCreateBoSAgent`: Validates the creation of a BoS Agent.
- `testInvalidGameType`: Ensures that an exception is thrown for an invalid game type.
- `testCreatePDAgents`: Validates the creation of multiple PD Agents.
- `testCreateBoSAgents`: Checks

### **TestPDAgent**:
- `testAgentStuckWithoutPlayMessage`: Validates that the agent does not send any message without receiving a `PlayMessage`.
- `testAgentRunsAndSendsMessageToNeighbor`: Confirms that upon receiving a `PlayMessage`, the agent sends its strategy to its neighbor.
- `testAgentDoesNotSentMessageTwice`: Ensures the agent does not send duplicate messages to its neighbor.
- `testAgentPicksDefectWhenNeighborCooperates`: Validates that the agent picks DEFECT when its neighbor chooses COOPERATE.
- `testAgentPicksDefectWhenNeighborDefects`: Confirms the agent sticks to DEFECT when its neighbor also defects.

### **TestBoSAgent**:
- `testAgentStuckWithoutPlayMessage`: Validates that the agent does not send any message without receiving a `PlayMessage`.
- `testAgentRunsAndSendsMessageToNeighbor`: Confirms that upon receiving a `PlayMessage`, the agent sends its strategy to its neighbor based on its assigned sex (HUSBAND/WIFE).
- `testAgentDoesNotSentMessageTwice`: Ensures the agent does not send duplicate messages to its neighbor.

## Github CI
- Check `.github/workflows/tests.yaml`.

## Notes:

- This project simulates various game strategies in a network of agents. Different agents can have different strategies and the aim is to see how strategies evolve over time based on agent interactions.

