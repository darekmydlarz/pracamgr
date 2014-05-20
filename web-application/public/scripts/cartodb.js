function fillCartoDBMap(matchId) {
    cartodb.createVis('cartomap', 'http://dmydlarzstudent.cartodb.com/api/v2/viz/70912e20-e030-11e3-b910-0e230854a1cb/viz.json', {
        center_lat: 51.32,
        center_lon: 0.5,
        zoom: 5
    })
    .done(function (vis, layers) {
        // layer 0 is the base layer, layer 1 is cartodb layer
        // setInteraction is disabled by default
        layers[1].setInteraction(true);
        layers[1].on('featureOver', function (e, pos, latlng, data) {
                cartodb.log.log(e, pos, latlng, data);
            }
        );

        var query = "SELECT * FROM allgeodata_sentiment WHERE match_event = '" + matchId + "'";
        layers[1].setQuery(query);

        // you can get the native map to work with it
        // depending if you use google maps or leaflet
        map = vis.getNativeMap();

        // now, perform any operations you need
        // map.setZoom(3)
        // map.setCenter(new google.maps.Latlng(...))
    })
    .error(function (err) {
        console.log(err);
    });
}
$(function () {
    var matchId = $("#map-canvas").data("id");
    fillCartoDBMap(matchId);
});