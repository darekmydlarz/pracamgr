package pl.edu.agh.twitter.business.geodata.entity;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class GeodataDAO {
    private Logger logger = Logger.getLogger(getClass());
    @Inject
    EntityManager em;

    public void persist(Geodata geodata) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        em.persist(geodata);
        transaction.commit();
    }

    public boolean isAlredyPersisted(Long tweetId) {
        final Geodata found = em.find(Geodata.class, tweetId);
        return found != null;
    }
}
