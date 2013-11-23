package pl.edu.agh.twitter;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.Tweet;
import pl.edu.agh.twitter.model.UserEntity;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

public class TweetDAO implements Runnable {
    private final Status status;
    private final MatchEvent[] matchEvents;

    private Logger logger = Logger.getLogger(TweetDAO.class);

    public TweetDAO(MatchEvent[] matchEvents, Status status) {
        this.matchEvents = matchEvents;
        this.status = status;
    }

    @Override
    public void run() {
        try {
            Tweet tweet = new Tweet(status, createOrGetUser(status.getUser()), getMatchEvent(status.getText()));
            persist(tweet);
            logger.info("Tweet " + tweet.getId() + " persisted");
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            logger.error("No match event found for: " + status);
        }
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

    private MatchEvent getMatchEvent(String text) {
        for(MatchEvent matchEvent : matchEvents) {
            if(matchEvent.isKeywordInString(text)) {
                return matchEvent;
            }
        }
        throw new IllegalStateException();
    }


    private static void persist(Tweet tweet) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(tweet);
        transaction.commit();
        session.close();
    }
}
