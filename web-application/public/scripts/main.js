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
});

function loadTopPositiveNegative() {
    var matchId = $("#map-canvas").data("id");
    if (matchId) {
        $.getJSON("http://localhost:9000/api/tweets/match/" + matchId + "/pos", function (data) {
            processData(data, $('#positives table'));
        });
        $.getJSON("http://localhost:9000/api/tweets/match/" + matchId + "/neg", function (data) {
            processData(data, $('#negatives table'));
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