measures = ["inDegree", "outDegree", "weightedInDegree", "weightedOutDegree", "eccentricity", "closenessCentrality", "betweennessCentrality", "authority", "hub", "clusteringCoefficient", "eigenvectorCentrality"];

headers = measures.concat();
headers.unshift("date");

chartData = [headers];
rows = [];
processedCounter = 0;

google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(execute);

function execute() {
    var matches = $("#team-list").find('li');
    processedSize = matches.length * measures.length;
    matches.each(function (matchIndex) {
        var matchId = $(this).data('id');
        var matchDate = $(this).data('date');
        var index = rows.push({"date": matchDate}) - 1;
        $.each(measures, function (i, measure) {
            $.getJSON("http://localhost:9000/api/users/sentiment/" + matchId + "," + measure, function (data) {
                var positives = 0, negatives = 0;
                $.each(data, function (i, item) {
                    positives += item.positives;
                    negatives += item.negatives;
                });
                rows[index][measure] = positives / (positives + negatives);
                processedCounter++;
                if (processedCounter == processedSize) {
                    drawChart();
                }
            });
        });
    });
}

function drawChart() {
    for(i in rows) {
        var row = rows[i];
        var chartRow = [
            row["date"],
            row["inDegree"],
            row["outDegree"],
            row["weightedInDegree"],
            row["weightedOutDegree"],
            row["eccentricity"],
            row["closenessCentrality"],
            row["betweennessCentrality"],
            row["authority"],
            row["hub"],
            row["clusteringCoefficient"],
            row["eigenvectorCentrality"]
        ];
        chartData.push(chartRow);
    }

    console.log(chartData);
    var data = google.visualization.arrayToDataTable(chartData);
    var options = {
        title: 'Company Performance',
        pointSize: 4,
        vAxis: {format:'#,###%', maxValue: 1, minValue: 0, gridlines: {count: 11}}
    };

    var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}


