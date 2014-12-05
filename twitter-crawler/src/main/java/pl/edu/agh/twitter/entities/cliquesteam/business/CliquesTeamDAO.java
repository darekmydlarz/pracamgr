package pl.edu.agh.twitter.entities.cliquesteam.business;

import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.cliquesteam.entity.CliquesTeam;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class CliquesTeamDAO {
    @Inject
    EntityManager em;
    Logger logger = Logger.getLogger(getClass());

    public CliquesTeam persist(CliquesTeam cliquesTeam) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        em.persist(cliquesTeam);
        transaction.commit();
        return cliquesTeam;
    }
}
