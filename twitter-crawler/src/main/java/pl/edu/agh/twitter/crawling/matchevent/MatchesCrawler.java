package pl.edu.agh.twitter.crawling.matchevent;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import pl.edu.agh.twitter.HibernateUtil;
import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.business.tweet.boundary.TweetDAO;
import twitter4j.*;

import java.util.Arrays;
import java.util.List;

public class MatchesCrawler {
    private static TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
    private static final String[] LANGUAGES = new String[]{"en"};
    private static Logger logger = Logger.getLogger(MatchesCrawler.class);
    private static TweetDAO tweetDAO = new TweetDAO();

    public static void main(String[] args) {
        List<MatchEvent> matchEvents = Arrays.asList(
            findMatchEvent("aston villa", "manchester united")
        );
        for(MatchEvent matchEvent : matchEvents) {
            logger.info(matchEvent.getKeywords().size() + ":" + matchEvent.getKeywords());
        }
        consume(matchEvents);
    }

    public static MatchEvent findMatchEvent(String homeTeam, String awayTeam) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        final String queryString = "FROM MatchEvent e WHERE lower(e.homeTeam.name) LIKE :homeTeam AND lower(e.awayTeam.name) LIKE :awayTeam";
        Query query = session.createQuery(queryString);
        final List items = query.setParameter("homeTeam", "%" + homeTeam.toLowerCase() + "%").setParameter("awayTeam", "%" + awayTeam.toLowerCase() + "%").list();
        MatchEvent matchEvent = (MatchEvent) items.get(0);
        session.close();
        return matchEvent;
    }

    private static FilterQuery filterQuery(List<MatchEvent> matchEvents) {
        final FilterQuery query = new FilterQuery();
        query.language(LANGUAGES);
        query.track(getKeywords(matchEvents));
        return query;
    }

    private static void consume(List<MatchEvent> matchEvents) {
        twitterStream.addListener(new MyStatusListener(matchEvents));
        twitterStream.filter(filterQuery(matchEvents));
    }

    private static String[] getKeywords(List<MatchEvent> matchEvents) {
        List<String> keywords = Lists.newArrayList();
        for(MatchEvent matchEvent : matchEvents) {
            keywords.addAll(matchEvent.getKeywords());
        }
        return Iterables.toArray(keywords, String.class);
    }

    public static class MyStatusListener implements StatusListener {
        private List<MatchEvent> matchEvents;

        MyStatusListener(List<MatchEvent> matchEvents) {
            this.matchEvents = matchEvents;
        }

        public void onStatus(Status status) {
            tweetDAO.parseAndPersist(getMatchEvent(status), status);
        }

        private MatchEvent getMatchEvent(Status status) {
            for(MatchEvent matchEvent : matchEvents)
                if(matchEvent.isKeywordInString(status.getText()))
                    return matchEvent;
            throw new IllegalStateException();
        }

        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        }

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        }

        public void onException(Exception ex) {
            ex.printStackTrace();
        }

        @Override
        public void onScrubGeo(long arg0, long arg1) {
        }

        @Override
        public void onStallWarning(StallWarning arg0) {
        }
    }

    ;
}
