google.load("visualization", "1", {packages: ["corechart"]});
google.setOnLoadCallback(drawChart);

var sentimentInTime;

function drawChart() {
    var matchId = $("#map-canvas").data("id");
    $.getJSON("http://localhost:9000/api/tweets/sentiment/" + matchId, function (data) {
        sentimentInTime = data;
        redrawChart();
    });
}

function redrawLineChart(dataArray) {
    var chartData = google.visualization.arrayToDataTable(dataArray);
    var container = document.getElementById('linechart');
    var parent = $("#linechart").parent();
    parent.addClass("active");
    var chart = new google.visualization.AreaChart(container);
    google.visualization.events.addListener(chart, 'ready', function () {
        parent.removeClass("active");
    });

    chart.draw(chartData, {
        title: "Sentiment in time",
        vAxis: {format:'#,###%', maxValue: 1, minValue: 0, gridlines: {count: 11}}
    });
}
function redrawOverallChart(positives, negatives) {
    var chartData = google.visualization.arrayToDataTable([
        ['Sentiment for ' + (positives + negatives) + ' tweets', 'Value'],
        ['Positive', positives],
        ['Negative', negatives]
    ]);

    var container = document.getElementById('piechart');
    var parent = $("#piechart").parent();
    parent.addClass("active");
    var chart = new google.visualization.PieChart(container);
    google.visualization.events.addListener(chart, 'ready', function () {
        parent.removeClass("active");
    });

    chart.draw(chartData, {
        title: 'Overall sentiment',
        pieStartAngle: 180
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

    redrawOverallChart(positives, negatives);
    redrawLineChart(linechartDataArray);
}
