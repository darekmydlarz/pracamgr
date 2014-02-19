var map;
var markers = [];

function initialize() {
    var haightAshbury = new google.maps.LatLng(54, 0);
    var mapOptions = {
        zoom: 6,
        center: haightAshbury
    };
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    $.getJSON("/api/geotagged/267", function(data) {
        $.each(data, function(i) {
            var cords = data[i].coordinates;
            var location = new google.maps.LatLng(cords.latitude, cords.longitude);
            addMarker(location);
        });
    });

}

// Add a marker to the map and push to the array.
function addMarker(location) {
    var marker = new google.maps.Marker({
        position: location,
        map: map
    });
    markers.push(marker);
}

// Sets the map on all markers in the array.
function setAllMap(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
    setAllMap(null);
}

// Shows any markers currently in the array.
function showMarkers() {
    setAllMap(map);
}

// Deletes all markers in the array by removing references to them.
function deleteMarkers() {
    clearMarkers();
    markers = [];
}

google.maps.event.addDomListener(window, 'load', initialize);