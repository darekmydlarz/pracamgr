package pl.edu.agh.twitter.business.cliquesteamuser.business;

import org.apache.log4j.Logger;
import pl.edu.agh.twitter.business.cliquesteamuser.entity.CliquesTeamUser;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class CliquesTeamUserDAO {

    @Inject
    EntityManager em;
    Logger logger = Logger.getLogger(getClass());

    public void persist(List<CliquesTeamUser> items) {
        final EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        final int size = items.size();
        for (CliquesTeamUser CliquesTeamUser : items) {
            em.persist(CliquesTeamUser);
            logger.info("PERSIST: " + items.indexOf(CliquesTeamUser) + " of " + size);
        }
        transaction.commit();
    }

    public List<CliquesTeamUser> findAll() {
        final String query = "FROM CliquesTeamUser";
        return em.createQuery(query, CliquesTeamUser.class).getResultList();
    }

    public void merge(CliquesTeamUser teamUser) {
        final EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        em.merge(teamUser);
        transaction.commit();
    }
}
