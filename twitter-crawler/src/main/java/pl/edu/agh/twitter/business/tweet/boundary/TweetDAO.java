package pl.edu.agh.twitter.business.tweet.boundary;


import ch.lambdaj.Lambda;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.business.tweet.entity.Tweet;
import pl.edu.agh.twitter.business.user.boundary.UserDAO;
import pl.edu.agh.twitter.business.user.entity.UserEntity;
import pl.edu.agh.twitter.socialnetwork.SourceTargetWeight;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class TweetDAO {
    private Logger logger = Logger.getLogger(getClass());

    @Inject
    EntityManager em;

    @Inject
    private UserDAO userDAO;

    public void parseAndPersist(MatchEvent matchEvent, Status status) {
        try {
            final UserEntity user = userDAO.createOrGetUser(status.getUser());
            Tweet tweet = new Tweet(status, user, matchEvent);
            persist(tweet);
            logger.info("Tweet " + tweet.getId() + " persisted");
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            logger.error("No match event found for: " + status);
        }
    }

    private void persist(Tweet tweet) {
        final EntityTransaction transaction = em.getTransaction();
        em.persist(tweet);
        transaction.commit();
    }

    @SuppressWarnings("unchecked")
    public List<Tweet> getTweets(UserEntity userEntity, MatchEvent matchEvent) {
        final String query = "FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match";
        return em.createQuery(query)
                .setParameter("user", userEntity)
                .setParameter("match", matchEvent)
                .getResultList();
    }

    public long getTweetsNumber(UserEntity userEntity, MatchEvent matchEvent) {
        final String query = "SELECT COUNT(t) FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match";
        return ((Number) em.createQuery(query)
                .setParameter("user", userEntity)
                .setParameter("match", matchEvent)
                .getSingleResult()
        ).longValue();
    }

    public long getGeotaggedNumber(UserEntity userEntity, MatchEvent matchEvent) {
        final String query = "SELECT COUNT(t) FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match AND " +
                "t.coordinates.latitude IS NOT NULL ";
        return ((Number) em.createQuery(query)
                .setParameter("user", userEntity)
                .setParameter("match", matchEvent)
                .getSingleResult()
                ).longValue();
    }

    @SuppressWarnings("unchecked")
    public List<SourceTargetWeight> getUsersRepliesRelations(Collection<UserEntity> users) {
        List<Long> usersIds = Lambda.extract(users, on(UserEntity.class).getId());
        final String query = "SELECT NEW pl.edu.agh.twitter.socialnetwork.SourceTargetWeight(t.user.id, t.inReplyToUserId, COUNT(t)) " +
                " FROM Tweet t " +
                " WHERE t.user.id IN :ids AND t.inReplyToUserId IN :ids " +
                " GROUP BY t.user.id, t.inReplyToUserId ";

        return em.createQuery(query)
                .setParameter("ids", usersIds)
                .getResultList();
    }
}
