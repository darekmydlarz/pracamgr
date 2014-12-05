package pl.edu.agh.twitter.entities.classifiedtweet.boundary;

import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.classifiedtweet.entity.ClassifiedTweet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

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

    public List<ClassifiedTweet> all() {
        final String query = "FROM ClassifiedTweet";
        return em.createQuery(query, ClassifiedTweet.class).getResultList();
    }
}
