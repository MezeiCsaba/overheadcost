function drawInfoChart() {
    const canvas = document.getElementById(sourceCanvas);
    const parentDiv = canvas.parentElement;
    const ctx = canvas.getContext("2d");

    const parentWidth = parentDiv.offsetWidth;
    const parentHeight = 600;

    const maxWidth = parentWidth * 0.9;
    const maxHeight = parentHeight * 0.5;
    const columnSpaceWidth = maxWidth * 0.98 / chartDataList.length;

    canvas.width = maxWidth;
    canvas.height = Math.max(maxHeight, 200);

    const startY = canvas.height - 20;
    const maxY = Math.max.apply(null, chartDataList.map((item) => item.consumption));
    const maxYvalue = Math.ceil(maxY / pitchY);
    const stepY = canvas.height / maxYvalue - 15;

    const labelPoint = (p) => {
        const offsetx = 10;
        const offsety = 5;
        const ptxt = p.z + "";
        ctx.textAlign = "end";
        ctx.fillStyle = ptxt.includes("%") ? "SpringGreen" : "white";
        ctx.font = ptxt.includes("%") ? "12px Arial" : "11px Arial";
        ctx.fillText(ptxt, p.x + offsetx, p.y + offsety);
    };

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const drawAxis = () => {
        ctx.beginPath();
        ctx.lineWidth = 3;
        ctx.strokeStyle = "gray";
        ctx.moveTo(20, startY + 1);
        ctx.lineTo(maxWidth - 10, startY + 1);
        ctx.stroke();
    };

    const drawGrid = () => {
        ctx.strokeStyle = "gray";
        ctx.lineWidth = 1;
        for (let i = 1; i <= maxYvalue; i++) {
            ctx.beginPath();
            const p = { x: 5, y: startY - i * stepY, z: i * pitchY };
            ctx.moveTo(20, startY - i * stepY);
            ctx.lineTo(maxWidth - 10, startY - i * stepY);
            ctx.stroke();
            labelPoint(p);
        }
    };

    const drawBars = () => {
        ctx.strokeStyle = barColor;
        ctx.lineWidth = columnSpaceWidth / 1.75;
        let px = 0.5;
        let startXCoord = 20;

        for (let i = 0; i < chartDataList.length; i++) {
            ctx.beginPath();
            ctx.moveTo(startXCoord + columnSpaceWidth * px + i * columnSpaceWidth, startY);
            ctx.lineTo(
                startXCoord + columnSpaceWidth * px + i * columnSpaceWidth,
                startY - (chartDataList[i].consumption * stepY) / pitchY
            );
            ctx.stroke();

            let p = { x: startXCoord + columnSpaceWidth * px + i * columnSpaceWidth, y: startY + 10, z: chartDataList[i].date };
            labelPoint(p);

            if (sourceCanvas === "electcanvas") {
                p = {
                    x: startXCoord + columnSpaceWidth * px + i * columnSpaceWidth,
                    y: startY - 25,
                    z: chartDataList[i].solar + "%",
                };
                labelPoint(p);
            }
        }
    };

    const drawTitle = () => {
        ctx.textAlign = "start";
        ctx.fillStyle = "white";
        ctx.font = "14px Arial";
        ctx.fillText(titleText, 10, 20);
    };

    drawAxis();
    drawGrid();
    drawBars();
    drawTitle();
}

window.addEventListener('resize', drawInfoChart);
