
function GeoCoding() {
    var matchId = $("#map-canvas").data("id");
    this.data = null;
}

function codeLatLng() {
//    var input = document.getElementById('latlng').value;
//    var latlngStr = input.split(',', 2);
//    var lat = parseFloat(latlngStr[0]);
//    var lng = parseFloat(latlngStr[1]);
    var geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(40.730885, -73.997383);
    geocoder.geocode({'latLng': latlng}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            console.log(results);
        } else {
            alert('Geocoder failed due to: ' + status);
        }
    });
}