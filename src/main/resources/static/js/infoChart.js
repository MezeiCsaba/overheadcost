function drawInfoChart() {

    const maxWidth = 900;
    const columnSpaceWidth = (maxWidth / chartDataList.length);
    const startY = 290;
    const maxY = Math.max.apply(null, chartDataList.map(function (item) {
        return item.consumption;
    }));
    const maxYvalue = Math.ceil(maxY / pitchY);
    const stepY = 250 / maxYvalue;

    const labelPoint = (p) => {
        const offsetx = 10;
        const offsety = 3;
        let ptxt = p.z + "";
        ctx.textAlign = "end";
        ctx.fillStyle = (ptxt.includes("%")) ? "SpringGreen" : "white";
        ctx.font = (ptxt.includes("%")) ? "12px Arial" :  "11px Arial";
        ctx.fillText(ptxt, p.x + offsetx, p.y + offsety);
    };

    const canvas = document.getElementById(sourceCanvas);
    const ctx = canvas.getContext("2d");

    ctx.beginPath();
    ctx.moveTo(20, 20);
    ctx.lineWidth = 3;
    ctx.strokeStyle = "gray";
    ctx.moveTo(20, startY + 1);
    ctx.lineTo(950, startY + 1);
    ctx.stroke();

    // diagram
    ctx.beginPath();
    ctx.strokeStyle = "gray";
    ctx.lineWidth = 1;
    for (var i = 1; i <= maxYvalue; i++) {
        ctx.beginPath();
        const p = { x: 5, y: startY - i * stepY, z: i * pitchY }
        ctx.moveTo(20, startY - i * stepY);
        ctx.lineTo(950, startY - i * stepY);
        ctx.stroke();
        labelPoint(p);

    }

    //consumption data columns
    ctx.strokeStyle = barColor;
    ctx.lineWidth = columnSpaceWidth / 1.75;

    let px = 0.75;

    for (var i = 0; i < chartDataList.length; i++) {
        ctx.beginPath();
        ctx.moveTo(25 + columnSpaceWidth * px + i * columnSpaceWidth, startY);
        ctx.lineTo(25 + columnSpaceWidth * px + i * columnSpaceWidth, startY - chartDataList[i].consumption * stepY / pitchY);
        ctx.stroke();
        let p = { x: 28 + columnSpaceWidth * px + i * columnSpaceWidth, y: startY + 15, z: chartDataList[i].date };
        labelPoint(p);
        if (sourceCanvas == "electcanvas") {
            p = { x: 28 + columnSpaceWidth * px + i * columnSpaceWidth, y: startY - 15, z: chartDataList[i].solar + '%' };
            labelPoint(p);
        }
    }

    ctx.textAlign = "start";
    ctx.fillStyle = "white";
    ctx.font = "14px Arial";
    ctx.fillText(titleText, 1, 12);

}