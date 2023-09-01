(async () => {
    const SPEED = 500;

    function drawBoard(){
        const root = document.getElementById('root');
        root.innerHTML = "";

        const board = document.createElement('div');
        board.className = 'board';

        const game = document.createElement("div");
        game.className = 'game';

        for (let i = 0; i < numberOfAgents; i++) {
            game.appendChild(drawAgent(i));
        }

        const networks = drawNetwork();

        const messageBoard = drawAudit();

        const row1 = document.createElement("div");
        row1.className = 'row'

        const row2 = document.createElement("div");
        row2.className = 'row'

        row1.appendChild(game);
        row1.appendChild(networks);
        row2.appendChild(messageBoard);

        board.appendChild(row1);
        board.appendChild(row2);

        root.appendChild(board)
    }

    function drawAgent(id){
        const agentElement = document.createElement('div');
        agentElement.dataset.id = id;
        agentElement.classList.add("agent");
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
            tr.innerHTML += `<td>${item}</td>`
        }

        tbody.appendChild(tr);

        const messageBoard = document.querySelector(".messageBoard");
        messageBoard.scrollTop = Number.MAX_SAFE_INTEGER;
    }

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    async function play(){
        for (const item of data){
            if(item.type === "PlayMessage"){
                const activeAgent = document.querySelector(".agent.active");
                if(activeAgent) activeAgent.classList.remove("active");
                document.querySelectorAll(".agent.neighbor").forEach(e => e.classList.remove("neighbor"))

                const currentAgent = document.querySelector(`.agent[data-id="${item.sender}"]`)
                if(currentAgent) currentAgent.classList.add("active");
            } else {
                const activeAgent = document.querySelector(".agent.active");
                if(activeAgent) activeAgent.innerHTML = item.strategy;

                const neighborAgent = document.querySelector(`.agent[data-id="${item.receiver}"]`);
                if(neighborAgent) neighborAgent.classList.add("neighbor");
            }

            drawAuditRow(item);

            await sleep(SPEED);
        }

        const activeAgent = document.querySelector(".agent.active");
        if(activeAgent) activeAgent.classList.remove("active");

        document.querySelectorAll(".agent.neighbor").forEach(e => e.classList.remove("neighbor"))
    }

    drawBoard();
    await play();
})();
