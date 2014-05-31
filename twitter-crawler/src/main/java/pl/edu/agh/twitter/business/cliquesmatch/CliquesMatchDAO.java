package pl.edu.agh.twitter.business.cliquesmatch;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class CliquesMatchDAO {
    @Inject
    EntityManager em;
    Logger logger = Logger.getLogger(getClass());

    public CliquesMatch persist(CliquesMatch cliquesMatch) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        em.persist(cliquesMatch);
        transaction.commit();
        return cliquesMatch;
    }
}
