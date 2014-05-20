package pl.edu.agh.twitter.business;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.matcheventgephi.boundary.MatchEventGephiDAO;
import pl.edu.agh.twitter.business.matcheventgephi.entity.MatchEventGephi;
import pl.edu.agh.twitter.business.paroubektweet.boundary.ParoubekTweetDAO;
import pl.edu.agh.twitter.business.paroubektweet.entity.ParoubekTweet;
import pl.edu.agh.twitter.business.usermatchsentiment.boundary.UserMatchSentimentDAO;
import pl.edu.agh.twitter.business.usermatchsentiment.entity.UserMatchSentiment;

import javax.inject.Inject;
import java.util.List;

public class UserMatchSentimentCounter implements Startable {
    public static final Double VALENCE_AVG = 0.4786984978198536;
    Logger logger = Logger.getLogger(getClass());

    @Inject
    UserMatchSentimentDAO userMatchSentimentDAO;

    @Inject
    MatchEventGephiDAO matchEventGephiDAO;

    @Inject
    ParoubekTweetDAO paroubekTweetDAO;

    @Override
    public void start() {
        final List<MatchEventGephi> all = matchEventGephiDAO.allForMatch(249l);
        int size = all.size();
        int counter = 1;
        List<UserMatchSentiment> items = Lists.newArrayList();
        for(MatchEventGephi meg : all) {
            UserMatchSentiment ums = new UserMatchSentiment();
            ums.setUserId(meg.getUserId());
            ums.setMatchEventId(meg.getMatchEventId());
            countPositivesAndNegativesTo(ums);
            logger.info(counter++ + " of " + size);
            items.add(ums);
        }
        userMatchSentimentDAO.persist(items);
        logger.info("[OK]");
    }

    private void countPositivesAndNegativesTo(UserMatchSentiment ums) {
        final List<ParoubekTweet> tweets = paroubekTweetDAO.find(ums.getUserId(), ums.getMatchEventId());
        long positives = 0, negatives = 0;
        for(ParoubekTweet tweet : tweets) {
            if(tweet.getValence() > VALENCE_AVG) {
                positives++;
            } else {
                negatives++;
            }
        }
        ums.setPositives(positives);
        ums.setNegatives(negatives);
    }
}
