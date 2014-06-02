package pl.edu.agh.twitter.business;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.Startable;
import pl.edu.agh.twitter.business.matchevent.boundary.MatchEventDAO;
import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
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

    @Inject
    MatchEventDAO matchEventDAO;

    @Override
    public void start() {
        final List<MatchEvent> matchEvents = matchEventDAO.fetchAll();
        for(MatchEvent me : matchEvents) {
            System.out.println(me);
            System.out.println(matchEvents.indexOf(me) + " of " + matchEvents.size());
            final List<MatchEventGephi> matchEventGephiList = matchEventGephiDAO.allForMatch(me.getId());
            List<UserMatchSentiment> umsList = Lists.newArrayList();
            int counter = 0;
            int size = matchEventGephiList.size();
            for (MatchEventGephi meg : matchEventGephiList) {
                UserMatchSentiment ums = new UserMatchSentiment();
                ums.setUserId(meg.getUserId());
                ums.setMatchEventId(meg.getMatchEventId());
                countPositivesAndNegativesTo(ums);
                System.out.println("DONE "+ ++counter + " of " + size + "; " + ums);
                umsList.add(ums);
            }
            userMatchSentimentDAO.persist(umsList);
            logger.info("[OK]");
        }
    }

    private void countPositivesAndNegativesTo(UserMatchSentiment ums) {
        final List<ParoubekTweet> tweets = paroubekTweetDAO.find(ums.getUserId(), ums.getMatchEventId());
        long positives = 0, negatives = 0;
        for(ParoubekTweet tweet : tweets) {
            if(tweet.isPositive()) {
                positives++;
            } else {
                negatives++;
            }
        }
        ums.setPositives(positives);
        ums.setNegatives(negatives);
    }
}
