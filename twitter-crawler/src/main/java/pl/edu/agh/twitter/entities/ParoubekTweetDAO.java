package pl.edu.agh.twitter.entities;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * DAO for entity
 */
public class ParoubekTweetDAO {
    Logger logger = Logger.getLogger(getClass());

    @Inject
    EntityManager em;

    @Deprecated
    public void persist(ParoubekTweet paroubekTweet) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        em.persist(paroubekTweet);
        transaction.commit();
    }

    public void persistAll(List<ParoubekTweet> paroubekTweets) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        for (ParoubekTweet paroubekTweet : paroubekTweets) {
            em.persist(paroubekTweet);
        }
        transaction.commit();
    }

    public List<ParoubekTweet> find(Long userId, Long matchEventId) {
        final String query = "FROM ParoubekTweet pt WHERE pt.tweet.user.id = :userId AND pt.tweet.matchEvent.id = :matchEventId";
        return em.createQuery(query, ParoubekTweet.class)
                .setParameter("userId", userId)
                .setParameter("matchEventId", matchEventId)
                .getResultList();
    }

    public List<ParoubekTweet> findForTeamCliques(Long userId, Long matchEventId) {
        final String query = "FROM ParoubekTweet pt " +
                " WHERE pt.tweet.user.id = :userId AND " +
                " pt.tweet.inReplyToUserId != -1 AND " +
                " pt.tweet.matchEvent.id = :matchEventId";
        return em.createQuery(query, ParoubekTweet.class)
                .setParameter("userId", userId)
                .setParameter("matchEventId", matchEventId)
                .getResultList();
    }
}
