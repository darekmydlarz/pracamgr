var geodata = {
    'countries': 'country',
    'states': 'state',
    'counties': 'county',
    'cities': 'city'
};

function fillAll(matchId, apikey, tableId) {
    $.getJSON('http://localhost:9000/api/geodata/' + apikey + '/' + matchId, function (data) {
        var trHTML = '';
        $.each(data, function (i, item) {
            var positivness = 100 * item.positives / item.count;
            trHTML += '<tr>' +
                '<td><a href="http://maps.google.com/?q='+item.teritory+'">' + item.teritory + '</a></td>' +
                '<td class="right">' + item.count + '</td>' +
                '<td class="right">' + item.percentage.toFixed(2) + '</td>' +
                '<td class="right percentageBg" style="background-size: '+positivness+'% 100% ">' + positivness.toFixed(2) + ' %</td>' +
                '</tr>';
        });
        var $table = $('#' + tableId).find('table');
        $table.addClass('table-scroll');
        $table.find('tbody').append(trHTML);
    });
}

$(function () {
    var matchId = $("#map-canvas").data("id");
    for(key in geodata) {
        fillAll(matchId, key, geodata[key]);
    }
});