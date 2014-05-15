var date = "date";
var inDegree = "inDegree";
var outDegree = "outDegree";
var weightedInDegree = "weightedInDegree";
var weightedOutDegree = "weightedOutDegree";
var eccentricity = "eccentricity";
var closenessCentrality = "closenessCentrality";
var betweennessCentrality = "betweennessCentrality";
var authority = "authority";
var hub = "hub";
var clusteringCoefficient = "clusteringCoefficient";
var eigenvectorCentrality = "eigenvectorCentrality";

var measures = [inDegree, outDegree, weightedInDegree, weightedOutDegree, eccentricity, closenessCentrality,
    betweennessCentrality, authority, hub, clusteringCoefficient, eigenvectorCentrality];

var dataRows = [];
var chartOptions = {
    pointSize: 5,
    vAxis: {format:'#,###%', maxValue: 0.7, minValue: 0.2, gridlines: {count: 7}}
};

google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(execute);

function execute() {
    var matches = $("#team-list").find('li');
    processedSize = matches.length * measures.length;
    var processedCounter = 0;
    matches.each(function (matchIndex) {
        var matchId = $(this).data('id');
        var matchDate = $(this).data(date);
        var index = dataRows.push({"date": matchDate}) - 1;
        $.each(measures, function (i, measure) {
            $.getJSON("http://localhost:9000/api/users/sentiment/" + matchId + "," + measure, function (data) {
                var sentimentSum = 0;
                $.each(data, function (i, item) {
                    var itemSentiment = item.positives / (item.positives + item.negatives);
                    sentimentSum += itemSentiment;
                });
                dataRows[index][measure] = sentimentSum / data.length;
                processedCounter++;
                if (processedCounter == processedSize) {
                    drawChart();
                }
            });
        });
    });
}

function drawInDegreesChart() {
    var chartData = [
        [date, inDegree, weightedInDegree]
    ];
    for (i in dataRows) {
        var dataRow = dataRows[i];
        var chartRow = [
            dataRow[date],
            dataRow[inDegree],
            dataRow[weightedInDegree]
        ];
        chartData.push(chartRow);
    }

    var chartDataTable = google.visualization.arrayToDataTable(chartData);
    var chart = new google.visualization.LineChart(document.getElementById('in_degrees_chart'));
    chartOptions.title = 'Sentiment for top 10 users with highest in degrees values';
    chart.draw(chartDataTable, chartOptions);
}

function drawOutDegreesChart() {
    var chartData = [
        [date, outDegree, weightedOutDegree]
    ];
    for (i in dataRows) {
        var dataRow = dataRows[i];
        var chartRow = [
            dataRow[date],
            dataRow[outDegree],
            dataRow[weightedOutDegree]
        ];
        chartData.push(chartRow);
    }

    var chartDataTable = google.visualization.arrayToDataTable(chartData);
    var chart = new google.visualization.LineChart(document.getElementById('out_degrees_chart'));
    chartOptions.title = 'Sentiment for top 10 users with highest out degrees values';
    chart.draw(chartDataTable, chartOptions);
}

function drawDistancesChart() {
    var chartData = [
        [date, betweennessCentrality, closenessCentrality, eccentricity]
    ];
    for (i in dataRows) {
        var dataRow = dataRows[i];
        var chartRow = [
            dataRow[date],
            dataRow[betweennessCentrality],
            dataRow[closenessCentrality],
            dataRow[eccentricity]
        ];
        chartData.push(chartRow);
    }

    var chartDataTable = google.visualization.arrayToDataTable(chartData);
    var chart = new google.visualization.LineChart(document.getElementById('distances_chart'));
    chartOptions.title = 'Sentiment for top 10 users with highest distances values';
    chart.draw(chartDataTable, chartOptions);
}
function drawNeighborhoodChart() {
    var chartData = [
        [date, clusteringCoefficient, eigenvectorCentrality]
    ];
    for (i in dataRows) {
        var dataRow = dataRows[i];
        var chartRow = [
            dataRow[date],
            dataRow[clusteringCoefficient],
            dataRow[eigenvectorCentrality]
        ];
        chartData.push(chartRow);
    }

    var chartDataTable = google.visualization.arrayToDataTable(chartData);
    var chart = new google.visualization.LineChart(document.getElementById('neighborhood_chart'));
    chartOptions.title = 'Sentiment for top 10 users with highest clustering, eigenvector values';
    chart.draw(chartDataTable, chartOptions);
}
function drawAuthorityChart() {
    var chartData = [
        [date, authority, hub]
    ];
    for (i in dataRows) {
        var dataRow = dataRows[i];
        var chartRow = [
            dataRow[date],
            dataRow[authority],
            dataRow[hub]
        ];
        chartData.push(chartRow);
    }

    var chartDataTable = google.visualization.arrayToDataTable(chartData);
    var chart = new google.visualization.LineChart(document.getElementById('authority_chart'));
    chartOptions.title = 'Sentiment for top 10 users with highest authority, hub values';
    chart.draw(chartDataTable, chartOptions);
}
function drawChart() {
    drawInDegreesChart();
    drawOutDegreesChart();
    drawDistancesChart();
    drawNeighborhoodChart();
    drawAuthorityChart();
}


