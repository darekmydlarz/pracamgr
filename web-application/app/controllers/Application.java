package controllers;

import models.Match;
import models.Tweet;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.List;

public class Application extends Controller {
    @Transactional(readOnly = true)
    public static Result index() {
        return ok(index.render(Match.all()));
    }

    @Transactional(readOnly = true)
    public static Result details(long id) {
        Match match = Match.findById(id);
        final List<Tweet> geotaggedTweets = Tweet.geotagged(match);
        return ok(details.render(geotaggedTweets, match));
    }
}
