package pl.edu.agh.twitter.business.classifiedtweet.boundary;

import org.apache.log4j.Logger;
import pl.edu.agh.twitter.business.classifiedtweet.entity.ClassifiedTweet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ClassifiedTweetDAO {
    private Logger logger = Logger.getLogger(ClassifiedTweetDAO.class);

    @Inject
    private EntityManager em;

    public void persist(ClassifiedTweet tweet) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        em.merge(tweet);
        logger.info("PERSISTED: " + tweet);
        transaction.commit();
    }
}
