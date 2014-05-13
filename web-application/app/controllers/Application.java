package controllers;

import com.google.common.collect.Lists;
import models.Match;
import models.Team;
import models.Tweet;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.List;

public class Application extends Controller {
    public static final List<String> MEASURES = Lists.newArrayList(
        "inDegree", "outDegree", "weightedInDegree", "weightedOutDegree", "eccentricity", "closenessCentrality", "betweennessCentrality", "authority", "hub", "clusteringCoefficient", "eigenvectorCentrality");

    @Transactional(readOnly = true)
    public static Result index() {
        return ok(index.render(Match.all()));
    }

    @Transactional(readOnly = true)
    public static Result details(long id) {
        Match match = Match.findById(id);
        final List<Tweet> geotaggedTweets = Tweet.geotagged(match);
        return ok(details.render(geotaggedTweets, match, MEASURES));
    }

    @Transactional(readOnly = true)
    public static Result team(String name) {
        Team team = Team.find(name);
        List<Match> matches = Match.find(team);
        return ok(teamView.render(team, matches));
    }
}
