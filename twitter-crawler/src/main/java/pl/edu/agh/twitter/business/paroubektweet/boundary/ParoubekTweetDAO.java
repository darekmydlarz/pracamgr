package pl.edu.agh.twitter.business.paroubektweet.boundary;

import org.apache.log4j.Logger;
import pl.edu.agh.twitter.business.paroubektweet.entity.ParoubekTweet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

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
}
