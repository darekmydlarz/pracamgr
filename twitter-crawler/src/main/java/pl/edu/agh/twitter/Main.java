package pl.edu.agh.twitter;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.twitter.crawler.TwitterServiceProvider;
import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.Tweet;
import pl.edu.agh.twitter.model.UserEntity;
import twitter4j.*;

import java.util.List;

public class Main {

    private static final String[] LANGUAGES = new String[]{"en"};
    private static TwitterStream twitterStream;

    public static void main(String[] args) {
        twitterStream = TwitterServiceProvider.getTwitterStream();
        MatchEvent evertonLiverpool = findMatchEvent("everton", "liverpool");
        System.out.println(evertonLiverpool.getKeywords().size());
        consume(evertonLiverpool);
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

    private static FilterQuery filterQuery(MatchEvent ... matchEvents) {
        final FilterQuery query = new FilterQuery();
        query.language(LANGUAGES);
        query.track(getKeywords(matchEvents));
        return query;
    }

    private static void consume(MatchEvent ... matchEvents) {
        twitterStream.addListener(new MyStatusListener(matchEvents));
        twitterStream.filter(filterQuery(matchEvents));
    }

    private static String[] getKeywords(MatchEvent[] matchEvents) {
        List<String> keywords = Lists.newArrayList();
        for(MatchEvent matchEvent : matchEvents) {
            keywords.addAll(matchEvent.getKeywords());
        }
        return Iterables.toArray(keywords, String.class);
    }

    private static UserEntity createOrGetUser(User user) throws TwitterException {
        UserEntity userEntity = getUser(user);
        if (userEntity != null)
            return userEntity;
        return persistUser(user);
    }

    private static UserEntity persistUser(User user) throws TwitterException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        final UserEntity userEntity = new UserEntity(user);
        session.persist(userEntity);
        transaction.commit();
        session.close();
        return userEntity;
    }

    private static UserEntity getUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        final UserEntity userEntity = (UserEntity) session.get(UserEntity.class, user.getId());
        transaction.commit();
        session.close();
        return userEntity;
    }

    private static void persist(Tweet tweet) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(tweet);
        transaction.commit();
        session.close();
        System.out.println("Tweet " + tweet.getId() + " persisted");
    }

    public static class MyStatusListener implements StatusListener {
        private MatchEvent[] matchEvents;

        MyStatusListener(MatchEvent[] matchEvents) {
            this.matchEvents = matchEvents;
        }

        private MatchEvent getMatchEvent(String text) {
            for(MatchEvent matchEvent : matchEvents) {
                if(matchEvent.isKeywordInString(text)) {
                    return matchEvent;
                }
            }
            throw new IllegalStateException();
        }

        public void onStatus(Status status) {
            try {
                Tweet tweet = new Tweet(status, createOrGetUser(status.getUser()), getMatchEvent(status.getText()));
                persist(tweet);
            } catch (TwitterException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                System.err.println("No match event found for: " + status);
            }
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
