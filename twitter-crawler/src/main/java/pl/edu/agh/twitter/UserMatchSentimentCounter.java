package pl.edu.agh.twitter;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.matchevent.MatchEventDAO;
import pl.edu.agh.twitter.entities.matchevent.MatchEvent;
import pl.edu.agh.twitter.entities.matcheventgephi.MatchEventGephiDAO;
import pl.edu.agh.twitter.entities.matcheventgephi.MatchEventGephi;
import pl.edu.agh.twitter.entities.paroubektweet.ParoubekTweetDAO;
import pl.edu.agh.twitter.entities.paroubektweet.ParoubekTweet;
import pl.edu.agh.twitter.entities.usermatchsentiment.UserMatchSentimentDAO;
import pl.edu.agh.twitter.entities.usermatchsentiment.UserMatchSentiment;

import javax.inject.Inject;
import java.util.List;

/**
 * Class witch counts sentiment for each user per each match.
 * It is crawling over all match events and counts user sentiment for that match.
 */
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
