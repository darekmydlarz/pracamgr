package pl.edu.agh.twitter.entities;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * DAO for entity
 */
public class UserGroupDAO {
    @Inject
    private EntityManager em;
    private Logger logger = Logger.getLogger(getClass());

    public void persist(UserGroup userGroup) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        em.persist(userGroup);
        transaction.commit();
    }

    public void persistAll(List<UserGroup> items) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        final int size = items.size();
        for(UserGroup userGroup : items) {
            em.persist(userGroup);
            logger.info("PERSIST: " + items.indexOf(userGroup) + " of " + size);
        }
        transaction.commit();
    }
}
