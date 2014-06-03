Number.prototype.format = function () {
    return this.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1 ");
};
Boolean.prototype.format = function () {
    return this.toString();
};
$(document).ready(function () {
    $("#matches-table").tablesorter();

    $(document).on("keyup", "#searchInput", function (e) {
        tableFilter.apply($(e.target).val().trim());
    });

    $(document).on("change", "#heatmap", function (e) {
        heatMap.toggle();
    });

    $(document).on("change", "#clustermap", function (e) {
        clusterMap.toggle();
    });


    $('#myTab').find('a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $("#measures").find('a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $("#geodataList").find('a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
    loadTopPositiveNegative();
    matchCliques();
    matchStats();
    teamCliques();
    cliquesUsersInfo();

    usersInMatches();


    google.load("visualization", "1", {packages:["corechart"]});
    google.setOnLoadCallback(charts);
});


function usersInMatches() {
    var teamId = $("#match-stats").data("team");
    if (teamId) {
        $.getJSON('http://localhost:9000/api/team/' + teamId + '/sentimentPerOccurences', function (data) {
            var trHTML = '';
            $.each(data, function(i, item) {
                trHTML += '<tr>';
                trHTML += '<td>' + item.matches + '</td><td class="right">'+item.users.format()+'</td>';
                trHTML += '</tr>';
            });
            $('#users-in-matches').find('tbody').append(trHTML);
        });
    }
}


function cliquesUsersInfo() {
    var teamId = $("#match-stats").data("team");
    if (teamId) {
        $.getJSON('http://localhost:9000/api/teams/' + teamId + '/cliquesUsers', function (data) {
            var info = '';
            $.each(data, function (i, user) {
                cliquesUsersTable(user.user, user.occurences);
                info += user.occurences + " x " + user.user.screenName + ' (' + user.positivness.toFixed(2) + '%), '
            });
            $('#cliquesUsers').html('<b>Most common users:</b> ' + info);
        });
    }

}
function cliquesUsersTable(user, occurences) {
    $.getJSON('http://localhost:9000/api/users/' + user.id + '/cliques', function (data) {
        var trHTML = '';
        var sentimentSum = 0;
        var size = Object.keys(data).length;
        for(var i = 0; i < size; ++i) {
            var item = data[i];
            sentimentSum += item.positivness;
            trHTML += '<tr><td>';
            $.each(item.users, function (j, user) {
                trHTML += user.user.screenName + ' (' + user.positivness.toFixed(2) + ' %), ';
            });
            trHTML += '</td>' +
                '<td class="right percentageBg" style="background-size: ' + item.positivness + '% 100% ">' + item.positivness.toFixed(2) + ' %</td>' +
                '</tr>';
        }
        var avgSentiment = sentimentSum / size;
        trHTML = '<tr><td><b>' + occurences + ' x ' + user.screenName + '</b></td>' +
            '<td class="right percentageBg" style="background-size: ' + avgSentiment + '% 100% "><b>' + avgSentiment.toFixed(2) + '%</b></td></tr>' + trHTML;
        $('#users-cliques').find('tbody').append(trHTML);
    });
}


function teamCliques() {
    var teamId = $("#match-stats").data("team");
    if (teamId) {
        $.getJSON('http://localhost:9000/api/teams/' + teamId + '/cliques', function (data) {
            var trHTML = '';
            $.each(data, function (i, item) {
                trHTML += '<tr>' +
                    '<td>';
                $.each(item.users, function (j, user) {
                    trHTML += user.user.screenName + ' (' + user.positivness.toFixed(2) + ' %), ';
                });
                trHTML += '</td>' +
                    '<td class="right percentageBg" style="background-size: ' + item.positivness + '% 100% ">' + item.positivness.toFixed(2) + ' %</td>' +
                    '</tr>';
            });
            $('#team-cliques').find('tbody').append(trHTML);
        });
    }
}


function matchStats() {
    var stats = ['tweets', 'users', 'positives', 'neutrals', 'negatives', 'geolocated', 'replies'];
    var teamId = $("#match-stats").data("team");
    if (teamId) {
        var teamName = $('#titleLink').data('title');
        $.getJSON("http://localhost:9000/api/team/" + teamId + "/stats", function (data) {
            var dataSize = Object.keys(data).length;
            // append headers
            var thHTML = '';
            for (var i = 0; i < dataSize; i++) {
                var opponentTeam = opponent(teamName, data[i].match);
                thHTML += '<th colspan="2">' + opponentTeam + '</th>';
            }
            var $match = $('#match-stats');
            $match.find('thead tr').append(thHTML);

            // append values
            for (var j = 0; j < stats.length; ++j) {
                var key = stats[j];
                var trHTML = '<tr>';
                trHTML += '<td class="right">' + key + '</td>';
                for (i = 0; i < dataSize; i++) {
                    var item = data[i];
                    var value = item[key];
                    var percentage = 100 * value / item['tweets']
                    trHTML += '<td class="right">' + value.format() + '</td>';
                    trHTML += '<td class="right">' + percentage.toFixed(2) + '%</td>';
                }
                trHTML += '</tr>';
                $match.find('tbody').append(trHTML);
            }
        });
    }
}

function opponent(team, matchEvent) {
    if (matchEvent.homeTeam.name === team)
        return matchEvent.awayTeam.name + " (H)";
    return matchEvent.homeTeam.name + " (A)";
}

function geodataChart() {
    columnChart('country');
    columnChart('city');
    columnChart('state');
    columnChart('county');
    function columnChart(column) {
        var teamId = $("#match-stats").data("team");
        if (teamId) {
            $.getJSON('http://localhost:9000/api/teams/' + teamId + '/geodata/' + column, function (data) {
                var countriesPercentage = {};
                $.each(data, function (key, values) {
                    var event = key;
                    $.each(values, function (i, item) {
                        countriesPercentage[item.teritory] = countriesPercentage[item.teritory] || [];
                        countriesPercentage[item.teritory][event] = item.percentage;
                    });
                });


                var headers = ['event'];
                $.each(countriesPercentage, function (key, value) {
                    headers.push(key);
                });
                var chartData = [headers];
                console.log(headers);
                $.each(data, function (key, values) {
                    var row = [key];
                    for (var i = 1; i < headers.length; ++i) {
                        row.push(countriesPercentage[headers[i]][key]);
                    }
                    chartData.push(row);
                })
                var options = {
                    title: column,
                    pointSize: 5,
                    width: 800,
                    height: 400
                };
                var googleData = google.visualization.arrayToDataTable(chartData);
                var chart = new google.visualization.LineChart(document.getElementById(column + '-chart'));
                chart.draw(googleData, options);
            });
        }
    }
}

function charts() {
    matchStatsChart();
    usersInMatchesChart();
    geodataChart();
    function usersInMatchesChart() {
        var teamId = $("#match-stats").data("team");
        if (teamId) {
            $.getJSON("http://localhost:9000/api/team/" + teamId + "/sentimentPerOccurences", function (data) {
                var chartData = [["# of matches", "# of users"]];
                $.each(data, function(i, item) {
                    var row = [item.matches, item.users];
                    chartData.push(row);
                });
                var options = {
                    title: '# of matches ',
                    pointSize: 5,
                    width: 800,
                    height: 400
                };
                var googleData = google.visualization.arrayToDataTable(chartData);
                var chart = new google.visualization.LineChart(document.getElementById('users-in-matches-chart'));
                chart.draw(googleData, options);
            });
        }

    }

    function matchStatsChart() {
        var stats = ['tweets', 'users', 'positives', 'neutrals', 'negatives', 'geolocated', 'replies'];
        var teamId = $("#match-stats").data("team");
        if (teamId) {
            var teamName = $('#titleLink').data('title');
            $.getJSON("http://localhost:9000/api/team/" + teamId + "/stats", function (data) {
                var chartData = [['event'].concat(stats)];
                var percentageData = [['event'].concat(stats)];
                var dataSize = Object.keys(data).length;

                for (var i = 0; i + 1 < dataSize; ++i) {
                    var item = data[i];
                    if (!item.includeRT) {
                        var row = [];
                        var percentageRow = [];
                        row.push(opponent(teamName, item.match) + ", " + item.match.goalResult + " " + item.match.infoResult);
                        percentageRow.push(opponent(teamName, item.match) + ", " + item.match.goalResult + " " + item.match.infoResult);
                        for (var j = 0; j < stats.length; ++j) {
                            var key = stats[j];
                            row.push(item[key]);
                            percentageRow.push(100 * item[key] / item['tweets']);
                        }
                        chartData.push(row);
                        percentageData.push(percentageRow);
                    }
                }
                var options = {
                    title: 'Match stats',
                    pointSize: 5,
                    width: 800,
                    height: 400
                };
                var googleData = google.visualization.arrayToDataTable(chartData);
                var percentageGoogleData = google.visualization.arrayToDataTable(percentageData);
                var chart = new google.visualization.LineChart(document.getElementById('match-stats-chart'));
                var percentageChart = new google.visualization.LineChart(document.getElementById('match-stats-chart-percentage'));
                chart.draw(googleData, options);
                percentageChart.draw(percentageGoogleData, options);
            });
        }
    }
}


function matchCliques() {
    var matchId = $("#map-canvas").data("id");
    if (matchId) {
        $.getJSON("http://localhost:9000/api/cliques/matches/" + matchId, function (data) {
            var trHTML = '';
            $.each(data, function (i, item) {
                trHTML += '<tr>' +
                    '<td>';
                $.each(item.users, function (j, user) {
                    trHTML += user.user.screenName + ' (' + user.positivness.toFixed(2) + ' %), ';
                });
                trHTML += '</td>' +
                    '<td class="right percentageBg" style="background-size: ' + item.positivness + '% 100% ">' + item.positivness.toFixed(2) + ' %</td>' +
                    '</tr>';
            });
            $('#tab-cliques').find('table tbody').append(trHTML);
        });
    }

}

function loadTopPositiveNegative() {
    var matchId = $("#map-canvas").data("id");
    if (matchId) {
        $.getJSON("http://localhost:9000/api/tweets/match/" + matchId + "/pos", function (data) {
            processData(data, $('#positives').find('table'));
        });
        $.getJSON("http://localhost:9000/api/tweets/match/" + matchId + "/neg", function (data) {
            processData(data, $('#negatives').find('table'));
        });
        function processData(data, $table) {
            var trHTML = '';
            $.each(data, function (i, item) {
                trHTML += '<tr>' +
                    '<td>' + item.user.screenName + '</td>' +
                    '<td>' + new Date(item.createdAt).toLocaleTimeString('pl-PL') + '</td>' +
                    '<td>' + item.text + '</td>' +
                    '<td>' + item.paroubekTweet.valence + '</td>' +
                    '</tr>';
            });
            $table.children('tbody').append(trHTML);
            $table.addClass('table-scroll');
            $table.show();
            $('#myTab').removeClass('pending');
        }
    }
}

var tableFilter = {
    text: "",

    changeVisibleMatchesNumber: function () {
        var showed = $("#matches-table").find("tbody").find("tr:visible").length;
        $("#matches-number").text(showed);
    },

    filterRows: function () {
        var data = this.text.split(" ");
        var rows = $("#matches-table").find("tbody").find("tr");
        $.each(data, function (input, value) {
            dataRows.filter(function () {
                return $(this).text().toLowerCase().indexOf(value.toLowerCase()) >= 0;
            }).show();
        });
    },

    apply: function (text) {
        this.text = text;
        var isInputEmpty = this.text == "";
        $("#matches-table").find("tbody").find("tr").toggle(isInputEmpty);
        if (!isInputEmpty)
            this.filterRows();
        this.changeVisibleMatchesNumber();
    }
}