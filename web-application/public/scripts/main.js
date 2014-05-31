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
    cliques();
    matchStats();
});


function matchStats() {
    var stats = ['includeRT', 'tweets', 'users', 'positives', 'neutrals', 'negatives', 'geolocated', 'replies', 'retweets'];
    var teamId = $("#match-stats").data("team");
    if(teamId) {
        $.getJSON("http://localhost:9000/api/team/" + teamId + "/stats", function (data) {
            // append headers
            var thHTML = '';
            $.each(data, function(i, matchStats) {
                var m = matchStats.match;
                thHTML += '<th>' + m.homeTeam.name + ' - ' + m.awayTeam.name + '</th>';
            });
            var $match = $('#match-stats');
            $match.find('thead tr').append(thHTML);
            // append values
           for(key in stats) {
               var trHTML = '<tr>';
               for(i in data) {
                   trHTML += '<td class="pull-right">' + data[i][stats[key]] + '</td>';
               }
               trHTML += '</tr>';
               $match.find('tbody').append(trHTML);
           }
        });
    }

}

function cliques() {
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
                    '<td class="align-right">' + item.positivness.toFixed(2) + ' %</td>' +
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