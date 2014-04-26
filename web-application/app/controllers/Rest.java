package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Match;
import models.Sentiment;
import models.SentimentInTime;
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
    public static Result geotagged(Long matchId) {
        final List<Tweet> tweets = Tweet.geotagged(Match.findById(matchId));
        final JsonNode jsonNode = Json.toJson(tweets);
        return ok(jsonNode);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Transactional(readOnly = true)
    public static Result tweet(Long tweetId) {
        final Tweet tweet = Tweet.find(tweetId);
        final JsonNode jsonNode = Json.toJson(tweet);
        return ok(jsonNode).as("application/json");
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Transactional(readOnly = true)
    public static Result topSentiment(Long matchId, String sentimentName) {
        final Match match = Match.findById(matchId);
        final Sentiment sentiment = Sentiment.valueOf(sentimentName.toUpperCase());
        final List<Tweet> tweets = Tweet.topWithSentiment(match, sentiment, 500);
        final JsonNode jsonNode = Json.toJson(tweets);
        return ok(jsonNode).as("application/json");
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Transactional(readOnly = true)
    public static Result sentimentInTime(Long matchId) {
        final List<SentimentInTime> sentimentInTimeList = Tweet.getSentimentInTime(matchId);
        final JsonNode jsonNode = Json.toJson(sentimentInTimeList);
        return ok(jsonNode).as("application/json");
    }
}
