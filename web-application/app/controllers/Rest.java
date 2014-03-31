package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Match;
import models.Tweet;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Rest extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    @Transactional(readOnly = true)
    public static Result geotagged(Long id) {
        final List<Tweet> tweets = Tweet.geotagged(Match.findById(id));
        final JsonNode jsonNode = Json.toJson(tweets);
        return ok(jsonNode);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Transactional(readOnly = true)
    public static Result tweet(Long id) {
        final Tweet tweet = Tweet.find(id);
        final JsonNode jsonNode = Json.toJson(tweet);
        return ok(jsonNode);
    }
}
