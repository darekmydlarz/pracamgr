google.load("visualization", "1", {packages: ["corechart"]});
google.setOnLoadCallback(drawChart);

var sentimentInTime;

function drawChart() {
    $.getJSON("http://localhost:9000/api/tweets/sentiment/725", function (data) {
        sentimentInTime = data;
        redrawChart();
    });
}

function redrawChart() {
    var positives = 0,
        negatives = 0,
        linechartDataArray = [["Time", "Positivness"]];

    $.each(sentimentInTime, function (i, item) {
        positives += item.positives;
        negatives += item.negatives;
        var positivness = item.positives / (item.positives + item.negatives);
        linechartDataArray.push([item.dateTime.slice(11, 16), positivness]);    // slice to get only time (HH:MM)
    });

    var chartData = google.visualization.arrayToDataTable([
        ['Sentiment for ' + (positives + negatives) + ' tweets', 'Value'],
        ['Positive', positives],
        ['Negative', negatives]
    ]);

    var chart = new google.visualization.PieChart(document.getElementById('piechart'));
    chart.draw(chartData, {
        title: 'Overall sentiment',
        pieStartAngle: 180
    });

    var lineChartData = google.visualization.arrayToDataTable(linechartDataArray);
    var lineChart = new google.visualization.AreaChart(document.getElementById('linechart'));
    lineChart.draw(lineChartData, {
        title: "Sentiment in time",
        vAxis: {format:'#,###%'}
    });
}
