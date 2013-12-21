package pl.edu.agh.twitter;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import pl.edu.agh.twitter.crawler.TwitterServiceProvider;
import pl.edu.agh.twitter.model.MatchEvent;
import twitter4j.*;

import java.util.Arrays;
import java.util.List;

public class Main {
    private static TwitterStream twitterStream = TwitterServiceProvider.getTwitterStream();
    private static final String[] LANGUAGES = new String[]{"en"};
    private static Logger logger = Logger.getLogger(Main.class);
    private static TwitterDAO twitterDAO = new TwitterDAO();

    public static void main(String[] args) {
        List<MatchEvent> matchEvents = Arrays.asList(
                // 2013-12-21 16:00:00
                findMatchEvent("manchester united", "west ham"),
                findMatchEvent("fulham", "manchester city")
                // 2013-12-23 21:00:00
//                findMatchEvent("arsenal", "chelsea")
                // 2013-12-26 13:45:00
//                findMatchEvent("hull", "manchester united")
                // 2013-12-26 16:00:00
//                findMatchEvent("west ham", "arsenal"),
//                findMatchEvent("chelsea", "swansea")
                // 2013-12-26 18:30:00
//                findMatchEvent("manchester city", "liverpool")
                // 2013-12-28 16:00:00
//                findMatchEvent("norwich", "manchester united"),
//                findMatchEvent("manchester city", "crystal palace")
                // 2013-12-29 14:30:00
//                findMatchEvent("newcastle", "arsenal")
                // 2013-12-29 17:00:00
//                findMatchEvent("chelsea", "liverpool")
        );
        for (MatchEvent matchEvent : matchEvents) {
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
        for (MatchEvent matchEvent : matchEvents) {
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
            twitterDAO.parseAndPersistTweet(matchEvents, status);
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
