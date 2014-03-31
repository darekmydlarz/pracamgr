package controllers;

import models.Match;
import models.Tweet;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Application extends Controller {
    public static Result index() {
        return redirect(routes.Application.matches());
    }

    @Transactional(readOnly = true)
    public static Result matches() {
        return ok(views.html.index.render(Match.all()));
    }

    @Transactional
    public static Result countTweetsPerMatch(Long id) {
        Match match = Match.findById(id);
        match.tweetsNumber = Tweet.count(match);
        Match.save(match);
        return redirect(routes.Application.matches());
    }

    @Transactional(readOnly = true)
    public static Result geotagged(long id) {
        Match match = Match.findById(id);
        final List<Tweet> geotaggedTweets = Tweet.geotagged(match);
        return ok(views.html.geotagged.render(geotaggedTweets, match));
    }
}
