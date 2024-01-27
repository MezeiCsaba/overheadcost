google.charts.load('current', { 'packages': ['bar'] });
google.charts.setOnLoadCallback(drawChart);


function drawChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Month');
        data.addColumn('number', 'Buy');
        data.addColumn('number', 'Sell');
        data.addColumn('number', 'Solar');
    data.addColumn('number', 'CalcCons');

    for (var i = 0; i < chartDataLists.length; i++) {
        var rowData = [];
        rowData.push(chartDataLists[i].date);
            rowData.push(chartDataLists[i].t180);
            rowData.push(chartDataLists[i].t280);
            rowData.push(chartDataLists[i].solar);
        rowData.push(chartDataLists[i].consumption);
        data.addRow(rowData);
    }

    var options = {
        chart: {
            title: 'Electricity consumption per month [kWh]',
            subtitle: 'Buy, sell, solar, consumption',
        },
        titleTextStyle: {
            color: 'white'
        },
        bars: 'vertical',
        vAxis: { format: 'decimal' },
        vAxis: { textStyle: { fontSize: 11, color: 'white' } },
        hAxis: { textStyle: { fontSize: 11, color: 'white' } },
        chartArea: { backgroundColor: 'transparent' },
        bar: { groupWidth: '85%' },
        legend: { position: 'none' },
        height: 300,
        width: 480,
        fontSize: 12,
        colors: ['Crimson', 'ForestGreen', 'Gold', 'DodgerBlue'],

        backgroundColor: 'transparent'
    };

    var chart = new google.charts.Bar(document.getElementById('chart_div'));
    chart.draw(data, google.charts.Bar.convertOptions(options));
};



