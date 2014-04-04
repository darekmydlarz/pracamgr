var clusterMap;
var heatMap;

function ClusterMap(tweets, map) {
    var data = [];
    var instance = new MarkerClusterer(map, [],  {
        gridSize: 50,
        maxZoom: 15
    });
    var infoWindow = new google.maps.InfoWindow();
    init(tweets);

    function init() {
        for (var i in tweets) {
            var cords = tweets[i].coordinates;
            addMarker({
                position: new google.maps.LatLng(cords.latitude, cords.longitude),
                id: tweets[i].id,
                map: map
            });
        }
        instance.addMarkers(data);
        instance.clearMarkers();
    }

    function addMarker(options) {
        var marker = new google.maps.Marker(options);
        data.push(marker);
        google.maps.event.addListener(marker, 'click', function () {
            refreshTweetTooltip(marker);
        })
    }

    function refreshTweetTooltip(marker) {
        var id = marker.get("id");
        $.getJSON("/api/tweets/" + id, function (tweet) {
            var info = buildTooltipText(tweet);
            infoWindow.setContent(info);
            infoWindow.open(map, marker);
        });
    }

    function buildTooltipText(tweet) {
        return tweet.text + "<br />" +
            "<small>" + tweet.user.name + ", " +new Date(tweet.createdAt).toLocaleTimeString('pl-PL') + "</small>";
    }

    this.toggle = function () {
        if (instance.getMarkers().length > 0)
            instance.clearMarkers();
        else
            instance.addMarkers(data);
    }
}

function HeatMap(tweets, map) {
    var data = [];
    var instance = new google.maps.visualization.HeatmapLayer({
        dissipating: true,
        opacity: 1,
        maxIntensity: 100,
        data: data
    });
    init(tweets);

    function init() {
        for (var i in tweets) {
            data.push(
                new google.maps.LatLng(tweets[i].coordinates.latitude, tweets[i].coordinates.longitude)
            );
        }
        instance.setData(data);
    }

    this.toggle = function() {
        instance.setMap(instance.getMap() ? null : map);
    }
}

function initialize() {
    var matchId = $("#map-canvas").data("id");
    var map = new google.maps.Map(document.getElementById("map-canvas"), {
        zoom: 6,
        center: new google.maps.LatLng(54, 0)
    });
    initMaps(matchId, map);
}

function initMaps(matchId, map) {
    $.getJSON("/api/tweets/geotagged/" + matchId, function (tweets) {
        clusterMap = new ClusterMap(tweets, map);
        heatMap = new HeatMap(tweets, map);
    });
}

google.maps.event.addDomListener(window, 'load', initialize);