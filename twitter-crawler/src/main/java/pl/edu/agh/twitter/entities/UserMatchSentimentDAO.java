package pl.edu.agh.twitter.entities;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * DAO for entity
 */
public class UserMatchSentimentDAO {
    @Inject
    EntityManager em;
    Logger logger = Logger.getLogger(getClass());

    public void persist(List<UserMatchSentiment> items) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        final int size = items.size();
        for(UserMatchSentiment userMatchSentiment : items) {
            em.persist(userMatchSentiment);
            logger.info("PERSIST: " + items.indexOf(userMatchSentiment) + " of " + size);
        }
        transaction.commit();
    }
}
