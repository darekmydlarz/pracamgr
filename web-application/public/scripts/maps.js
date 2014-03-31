var map;
var markerClusterer;
var markers = [];
var infoWindow;

function getMarkers(matchId, markerClusterer) {
    console.log("/api/geotagged/" + matchId)
    $.getJSON("/api/geotagged/" + matchId, function (tweets) {
        for(i in tweets) {
            var cords = tweets[i].coordinates;
            addMarker({
                position: new google.maps.LatLng(cords.latitude, cords.longitude),
                id: tweets[i].id,
                map: map
            });
        };
        markerClusterer.addMarkers(markers);
    });
}
function initialize() {
    var matchId = $("#map-canvas").data("id");
    var center = new google.maps.LatLng(54, 0);
    var options = {
        zoom: 6,
        center: center
    };
    map = new google.maps.Map(document.getElementById("map-canvas"), options);
    markerClusterer = new MarkerClusterer(map);
    infoWindow = new google.maps.InfoWindow();
    getMarkers(matchId, markerClusterer);
}

// Add a marker to the map and push to the array.
function addMarker(options) {
    var marker = new google.maps.Marker(options);
    markers.push(marker);
    google.maps.event.addListener(marker, 'click', function() {
        refreshTweetTooltip(marker.get("id")),
        infoWindow.setContent(infoContent);
        infoWindow.open(map, marker);
    })
}

var infoContent;

function refreshTweetTooltip(id) {
    $.getJSON("/api/tweet/" + id, function (tweet) {
        infoContent = "<h5>" + tweet.text + "</h5>"
            + "by <strong>" + tweet.user.name + "</strong> "
            + "at <strong>" + new Date(tweet.createdAt).toLocaleString() + "</strong>";
        console.log("DARIO2.2" + Date.now());
    });
}


google.maps.event.addDomListener(window, 'load', initialize);