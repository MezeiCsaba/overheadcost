google.charts.load('current', { 'packages': ['bar'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Month');
    data.addColumn('number', 'Gas');
    for (var i = 0; i < gasChartDataList.length; i++) {
        var rowData = [];
        rowData.push(gasChartDataList[i].date);
        rowData.push(gasChartDataList[i].consumption);
        data.addRow(rowData);
    }
    var options = {
        chart: {
            title: 'Gas consumption per month [m3]',
            subtitle: 'historical data',

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
        colors: ['Orange'],
        backgroundColor: 'transparent'

    };
    var chart = new google.charts.Bar(document.getElementById('gas_chart_div'));
    chart.draw(data, google.charts.Bar.convertOptions(options));
}