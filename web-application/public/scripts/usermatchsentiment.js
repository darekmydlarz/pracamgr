$(function () {
    var matchId = $("#map-canvas").data("id");
    $('#measures').find('li a').each(function () {
        var measure = $(this).attr('href').substring(1);

        $.getJSON("http://localhost:9000/api/users/sentiment/" + matchId + "," + measure, function (data) {
            var trHTML = '';
            $.each(data, function (i, item) {
                var positivness = 100 * item.positives / (item.negatives + item.positives);
                trHTML += '<tr>' +
                    '<td>' + item.user.id + '</td>' +
                    '<td>' + item.user.screenName + '</td>' +
                    '<td class="right">' + item.meg[measure].toFixed(4) + '</td>' +
                    '<td class="right percentageBg" style="background-size: '+positivness+'% 100% ">' + positivness.toFixed(2) + ' %</td>' +
                    '</tr>';
            });
            $("#" + measure).find('tbody').append(trHTML);
        });
    });
})