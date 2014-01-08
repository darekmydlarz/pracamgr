package pl.edu.agh.twitter.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.agh.twitter.HibernateUtil;
import pl.edu.agh.twitter.model.*;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;

public class TwitterDAO {

    private Logger logger = Logger.getLogger(TwitterDAO.class);

    public void parseAndPersistTweet(List<MatchEvent> matchEvents, Status status) {
        try {
            Tweet tweet = new Tweet(status, createOrGetUser(status.getUser()), getMatchEvent(matchEvents, status.getText()));
            persistTweet(tweet);
            logger.info("Tweet " + tweet.getId() + " persisted");
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            logger.error("No match event found for: " + status);
        }
    }

    private UserEntity createOrGetUser(User user) throws TwitterException {
        UserEntity userEntity = getUser(user);
        if (userEntity != null)
            return userEntity;
        return persistUser(user);
    }

    private UserEntity persistUser(User user) throws TwitterException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        final UserEntity userEntity = new UserEntity(user);
        session.persist(userEntity);
        transaction.commit();
        session.close();
        return userEntity;
    }

    private UserEntity getUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        final UserEntity userEntity = (UserEntity) session.get(UserEntity.class, user.getId());
        session.close();
        return userEntity;
    }

    private MatchEvent getMatchEvent(List<MatchEvent> matchEvents, String text) {
        for(MatchEvent matchEvent : matchEvents) {
            if(matchEvent.isKeywordInString(text)) {
                return matchEvent;
            }
        }
        throw new IllegalStateException();
    }


    private void persistTweet(Tweet tweet) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(tweet);
        transaction.commit();
        session.close();
    }

    public Competition createOrGetCompetition(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String query = "FROM Competition WHERE lower(name) LIKE :name";
            Competition competition = (Competition) session.createQuery(query).setParameter("name", "%" + name.toLowerCase() + "%").list().get(0);
            return competition;
        } catch (HibernateException | IndexOutOfBoundsException e) {
            if(session != null)
                session.close();
            return new Competition(name);
        }
    }

    public Team createOrGetTeam(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String query = "FROM Team WHERE lower(name) LIKE :name";
            Team team = (Team) session.createQuery(query).setParameter("name", "%" + name.toLowerCase() + "%").list().get(0);
            return team;
        } catch (HibernateException | IndexOutOfBoundsException e) {
            if(session != null)
                session.close();
            return new Team(name);
        }
    }
}
