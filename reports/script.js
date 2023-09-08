(async () => {
    const SPEED = 100;

    function drawBoard(){
        const root = document.getElementById('root');
        root.innerHTML = "";

        const board = document.createElement('div');
        board.className = 'board';

        const row1 = document.createElement("div");
        row1.className = 'row-1'

        const row2 = document.createElement("div");
        row2.className = 'row-2'

        row1.appendChild(drawGame());
        row1.appendChild(drawGameState())
        row2.appendChild(drawAudit());
        row2.appendChild(drawNetwork())

        board.appendChild(row1);
        board.appendChild(row2);

        root.appendChild(board)
    }

    function drawGame(){
        const game = document.createElement("div");
        game.className = 'game';

        for (let i = 0; i < numberOfAgents; i++) {
            if(gameType === "PD"){
                game.appendChild(drawPBAgent(i));
            } else {
                game.appendChild(drawBoSAgent(i, "husband"));
            }
        }

        return game;
    }

    function drawPBAgent(id){
        return drawAgent(id , PRISONER_SVG);
    }

    function drawBoSAgent(id, sex){
        return drawAgent(id, sex === 'wife' ? WIFE_SVG : HUSBAND_SVG);
    }

    function drawAgent(id, svg){
        const agentElement = document.createElement('div');
        agentElement.dataset.id = id;
        agentElement.classList.add("agent");
        agentElement.innerHTML = `
                            <span class="number">${id}</span>
                            <div class="chrachter">${svg}</div>
                            <div class="strategy">?</div>
                            `
        return agentElement;
    }

    function drawNetwork(){
        const networksElement = document.createElement('div');
        networksElement.className = 'networks';

        networksElement.innerHTML = `
        <table>
          <thead>
            <tr>
              <th>Agent ID</th>
              <th>Neighbors</th>
              <th>score</th>
            </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
        `
        const tableRows = networksElement.querySelector('tbody');

        for (const [key, values] of Object.entries(connections)){
            tableRows.innerHTML += `<tr>
                <td>${key}</td>
                <td>${values.join(", ")}</td>
                <td class="agent-score">Culculating...</td>
            </tr>`
        }

        return networksElement
    }

    function drawAudit(){
        const messageBoard = document.createElement("div");
        messageBoard.className = "messageBoard"

        messageBoard.innerHTML = `
        <table>
          <thead>
            <tr></tr>
          </thead>
          <tbody>
          </tbody>
        </table>
        `;

        const thead = messageBoard.querySelector('thead tr');
        for(const header of Object.keys(messages[0])){
            thead.innerHTML += `<td>${header}</td>`
        }

        return messageBoard;
    }

    function drawAuditRow(message) {
        const tbody = document.querySelector(".messageBoard tbody")

        const tr = document.createElement('tr');
        for (const item of Object.values(message)){
            tr.innerHTML += `<td>${item === "-1" ? "System" : item}</td>`
        }

        tbody.appendChild(tr);

        const messageBoard = document.querySelector(".messageBoard");
        messageBoard.scrollTop = Number.MAX_SAFE_INTEGER;
    }

    function drawGameState(){
        const gameState = document.createElement('div');
        gameState.className = "game-state";

        gameState.innerHTML = `
        <table>
          <thead>
            <tr>
              <th>State</th>
              <th>Value</th>
            </tr>
          </thead>
          <tbody>
            <tr><td>Number of Agents</td><td>${numberOfAgents}</td></tr>
            <tr><td>Probability of connection</td><td>${probability * 100}%</td></tr>
            ${fraction ? "<tr><td>Fraction</td><td>" + fraction + "</td></tr>" : ""}
            <tr><td>Rounds</td><td class="round">Initializing...</td></tr>
            <tr><td>Total Social Welfare (SW)</td><td class="score">Culculating...</td></tr>
          </tbody>
        </table>
        <div class="legend">
            <header class="sub-title">Legend: </header>
            <div class="legend-info"><span class="legend-name">Waiting</span><span class="legend-block" style="background: red"></span></div>
            <div class="legend-info"><span class="legend-name">Active</span><span class="legend-block" style="background: green"></span></div>
            <div class="legend-info"><span class="legend-name">Notified Neigbor</span><span class="legend-block" style="background: blue"></span></div>
            <div class="legend-info"><span class="legend-name">Done</span><span class="legend-block" style="background: black"></span></div>
        </div>
        `

        return gameState;
    }

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    function sortByScore(){
        const sortFunction = (agent1, agent2) => {
            const score1 = Number(agent1.querySelector(".agent-score").innerHTML);
            const score2 = Number(agent2.querySelector(".agent-score").innerHTML);

            return score2 - score1;
        }

        const elements = document.querySelectorAll(".networks tbody tr");

        const root = document.querySelector(".networks tbody");

        [...elements].sort(sortFunction).forEach(e => root.appendChild(e));
    }

    async function play(){
        const clearActiveAgent = () => {
            const activeAgent = document.querySelector(".agent.active");
            if(activeAgent) activeAgent.classList.remove("active");
        }

        const clearNeighborAgent = () => {
            document.querySelectorAll(".agent.neighbor").forEach(e => e.classList.remove("neighbor"))
        }

        const setAgentsDone = () => {
            document.querySelectorAll(".agent").forEach(e => e.classList.add("done"));
        }

        for (const message of messages){
            if(message.type === "PlayMessage"){
                clearActiveAgent();
                clearNeighborAgent();

                const currentAgent = document.querySelector(`.agent[data-id="${message.receiver}"]`)
                if(currentAgent) currentAgent.classList.add("active");
            } else if( message.type === "PDMessage"){
                const activeAgentStrategyElement = document.querySelector(".agent.active .strategy");
                activeAgentStrategyElement.innerHTML = message.meta;

                const neighborAgent = document.querySelector(`.agent[data-id="${message.receiver}"]`);
                if(neighborAgent) neighborAgent.classList.add("neighbor");
            } else if(message.type === "RoundMessage"){
                const roundElement = document.querySelector('.round');
                if(message.meta !== "0"){
                    roundElement.innerHTML = "" + message.meta;
                }
            } else if(message.type === "TotalScoreMessage"){
                const scoreElement = document.querySelector(".score");
                scoreElement.innerHTML = message.meta;
            } else if (message.type === "AgentScoreMessage"){
                const meta = JSON.parse(message.meta);
                const agentScoreElement = [...document.querySelectorAll('.agent-score')][meta.agentId];
                agentScoreElement.innerHTML = meta.score;
            } else if (message.type === "BoSMessage") {
                const meta = JSON.parse(message.meta);
                const activeAgentStrategyElement = document.querySelector(".agent.active .strategy");
                if(activeAgentStrategyElement) activeAgentStrategyElement.innerHTML = meta.strategy;

                const activeAgentCharacterElement = document.querySelector(".agent.active .chrachter");
                if(activeAgentCharacterElement) activeAgentCharacterElement.innerHTML = meta.sex === "WIFE" ? WIFE_SVG : HUSBAND_SVG;

                const neighborAgent = document.querySelector(`.agent[data-id="${message.receiver}"]`);
                if(neighborAgent) neighborAgent.classList.add("neighbor");
            }

            drawAuditRow(message);

            await sleep(SPEED);
        }

        clearActiveAgent();
        setAgentsDone();
        clearNeighborAgent();
        sortByScore();
    }

    drawBoard();
    await play();
})();
