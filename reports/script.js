(async () => {
    const SPEED = 100;

    const PRISONER_SVG = `
    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" fill="#000000" height="40px" width="40px" version="1.1" id="Capa_1" viewBox="0 0 612 612" xml:space="preserve">
        <g>
          <g>
            <circle cx="259.067" cy="165.184" r="13.447"/>
            <circle cx="334.144" cy="165.184" r="13.447"/>
            <path d="M450.88,491.922c-33.106,0-60.039,26.934-60.039,60.039c0,13.356,4.387,25.703,11.792,35.686    c-2.98,1.052-6.215,1.319-9.389,0.712c-5.156-0.985-9.616-4.16-12.24-8.705l-13.3-23.051v-98.027h28.874v-77.349l34.837,34.838    c6.788,6.795,15.815,10.538,25.416,10.538c9.601,0,18.628-3.743,25.412-10.534c6.795-6.788,10.538-15.813,10.539-25.415    c0-9.601-3.743-18.629-10.536-25.415c0,0-117.827-118.516-119.33-119.846c16.94-17.145,26.651-40.226,26.944-64.441    c12.103-2.87,20.949-13.661,20.949-26.599c0-12.94-8.85-23.734-20.957-26.601c-0.4-24.333-10.037-47.153-27.287-64.401    c-2.214-2.214-4.558-4.312-6.983-6.293V0H237.632v57.066c-21.397,17.48-33.806,43.116-34.265,70.678    c-12.11,2.855-20.963,13.65-20.963,26.607c0,12.953,8.846,23.744,20.949,26.604c0.295,24.215,10.006,47.296,26.947,64.441    c-5.032,4.46-118.707,119.842-118.707,119.842c-14.013,14.014-14.013,36.815-0.001,50.826    c6.788,6.795,15.813,10.538,25.416,10.538c9.601,0,18.628-3.743,25.413-10.536l34.212-34.213v76.723h28.874v147.344H367.7v-26.539    l3.44,5.961c4.278,7.414,11.554,12.59,19.961,14.198c1.84,0.353,3.693,0.525,5.536,0.525c4.897,0,9.718-1.227,14.026-3.566    c10.653,9.627,24.759,15.5,40.214,15.5c33.105,0,60.039-26.933,60.039-60.038C510.919,518.855,483.985,491.922,450.88,491.922z     M236.895,484.86h54.02v21.346h-54.02V484.86z M356.318,538.937h-54.02v-21.346h54.02L356.318,538.937L356.318,538.937z     M356.318,506.206h-54.02V484.86h54.02L356.318,506.206L356.318,506.206z M236.895,517.591h54.02v21.346h-54.02V517.591z     M290.914,572.807c-6.942-6.858-16.486-11.073-27.01-11.073c-10.524,0-20.068,4.215-27.01,11.073v-22.486h54.02V572.807z     M263.904,573.119c12.904,0,23.708,9.597,26.368,21.417h-52.736C240.196,582.718,251,573.119,263.904,573.119z M356.318,572.809    c-6.942-6.858-16.488-11.073-27.011-11.073c-10.522,0-20.067,4.215-27.008,11.071v-22.486h54.02L356.318,572.809L356.318,572.809z     M355.675,594.536h-52.736c2.66-11.819,13.465-21.417,26.368-21.417C342.212,573.119,353.016,582.718,355.675,594.536z     M356.318,473.476h-54.02v-14.9h54.02L356.318,473.476L356.318,473.476z M296.601,289.102c-18.308,0-34.504-10.556-42.342-26.169    c13.011,6.634,27.501,10.174,42.348,10.174c14.845,0,29.332-3.539,42.343-10.172C331.106,278.547,314.907,289.102,296.601,289.102    z M257.619,285.631c10.441,9.268,24.154,14.855,38.982,14.855c14.828,0,28.542-5.587,38.986-14.855h42.624    c2.52,5.93,4.375,12.119,5.533,18.5H209.482c1.161-6.37,3.001-12.566,5.523-18.5H257.619z M392.314,291.399l25.362,25.365    l-21.098,21.1v-17.652C396.577,310.405,395.111,300.693,392.314,291.399z M385.192,335.438H208.021c0,0,0.055-18.363,0.137-19.923    h176.901C385.138,317.074,385.192,335.438,385.192,335.438z M208.021,346.823h177.172V363.9H208.021V346.823z M196.636,334.009    l-18.858-18.851l22.761-22.761c-2.579,8.94-3.902,18.274-3.902,27.814V334.009z M208.021,375.284h177.172v17.077H208.021V375.284z     M208.021,403.745h177.172v18.5H208.021V403.745z M396.577,353.962l29.149-29.149l16.513,16.516l-34.73,34.73l-10.932-10.932    V353.962z M432.07,400.622l-16.512-16.512l34.729-34.73l16.512,16.515L432.07,400.622z M474.191,408.019    c-4.638,4.643-10.802,7.201-17.361,7.201c-6.247,0-12.123-2.344-16.678-6.579l34.664-34.665c4.235,4.554,6.579,10.43,6.579,16.678    C481.395,397.212,478.837,403.377,474.191,408.019z M365.647,264.696c2.468,3.064,4.705,6.254,6.718,9.55h-26.848    c4.18-6.261,7.182-13.39,8.698-21.083c0.048-0.038,0.097-0.078,0.147-0.117c1.682,1.446,3.321,2.951,4.891,4.52    c1.951,1.951,3.835,4.017,5.61,6.156L365.647,264.696z M399.423,154.353c0,6.553-3.891,12.157-9.535,14.621v-29.243    C395.532,142.196,399.423,147.8,399.423,154.353z M249.016,40.863h95.179V55.31h-95.179L249.016,40.863L249.016,40.863z     M344.195,11.385v18.094h-95.179V11.385H344.195z M193.79,154.353c0-6.563,3.891-12.17,9.535-14.629v29.258    C197.681,166.523,193.79,160.916,193.79,154.353z M214.709,129.321c0-24.303,10.62-47.004,29.167-62.626h105.499    c1.776,1.497,3.496,3.064,5.137,4.705c15.47,15.47,23.99,36.041,23.99,57.922v50.505c0,23.149-9.813,45.214-26.967,60.745    l-0.952,0.837c-14.918,13.101-34.088,20.316-53.978,20.316c-19.89,0-39.059-7.215-53.978-20.316l-0.72-0.625    c-17.299-15.543-27.201-37.703-27.201-60.956v-50.506H214.709z M225.536,267.316c3.885-5.217,8.361-9.994,13.321-14.263    c0.047,0.037,0.093,0.075,0.139,0.112c1.516,7.693,4.515,14.821,8.693,21.083h-26.819    C222.314,271.88,223.861,269.563,225.536,267.316z M145.164,347.771l34.727,34.729l-16.513,16.512l-34.729-34.729L145.164,347.771    z M137.01,415.218c-6.559,0-12.724-2.557-17.364-7.202c-9.574-9.574-9.574-25.154,0-34.729l0.955-0.955l34.729,34.727    l-0.958,0.958C149.733,412.661,143.568,415.218,137.01,415.218z M187.94,374.449l-34.727-34.729l16.513-16.513l26.91,26.899    v15.648L187.94,374.449z M208.021,433.63h177.172v13.562H208.021V433.63z M290.914,458.576v14.9h-54.02v-14.9H290.914z     M450.88,600.615c-26.828,0-48.655-21.826-48.655-48.653c0-26.829,21.827-48.655,48.655-48.655s48.655,21.827,48.655,48.655    S477.708,600.615,450.88,600.615z"/>
            </g>
          </g>
    </svg>
    `;

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
            game.appendChild(drawAgent(i, !!agentsSex.length > 0 ? agentsSex[i] : null));
        }

        return game;
    }

    function drawAgent(id, agentSex = null){
        const agentElement = document.createElement('div');
        agentElement.dataset.id = id;
        agentElement.classList.add("agent");
        agentElement.innerHTML = `
                            <span class="number">${id}${!!agentSex ? ', ' + agentSex : ''}</span>
                            <div class="prisioner">${PRISONER_SVG}</div>
                            <div class="strategy"></div>
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
        for(const header of Object.keys(data[0])){
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
            <tr><td>Probability</td><td>${probability * 100}%</td></tr>
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

        for (const message of data){
            if(message.type === "PlayMessage"){
                clearActiveAgent();
                clearNeighborAgent();

                const currentAgent = document.querySelector(`.agent[data-id="${message.sender}"]`)
                if(currentAgent) currentAgent.classList.add("active");
            } else if( message.type === "PDMessage"){
                const activeAgentStrategyElement = document.querySelector(".agent.active .strategy");
                if(activeAgentStrategyElement) activeAgentStrategyElement.innerHTML = message.meta;

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
                const receiver = Number(message.receiver);
                const agentScore = [...document.querySelectorAll('.agent-score')][receiver];
                agentScore.innerHTML = message.meta;
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
