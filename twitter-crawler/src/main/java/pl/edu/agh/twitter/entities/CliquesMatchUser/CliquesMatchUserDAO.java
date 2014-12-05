package pl.edu.agh.twitter.entities.CliquesMatchUser;

import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class CliquesMatchUserDAO {

    @Inject
    EntityManager em;
    Logger logger = Logger.getLogger(getClass());

    public void persist(List<CliquesMatchUser> items) {
        final EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        final int size = items.size();
        for (CliquesMatchUser item : items) {
            em.persist(item);
            logger.info("PERSIST: " + items.indexOf(item) + " of " + size);
        }
        transaction.commit();
    }

    public List<CliquesMatchUser> findAll() {
        final String query = "FROM CliquesMatchUser";
        return em.createQuery(query, CliquesMatchUser.class)
                .getResultList();
    }

    public void merge(CliquesMatchUser matchUser) {
        final EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        em.merge(matchUser);
        transaction.commit();
    }
}
