$(document).ready(function () {
    $("#matches-table").tablesorter();

    $(document).on("keyup", "#searchInput", function (e) {
        tableFilter.apply($(e.target).val().trim());
    });
});

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
            rows.filter(function () {
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