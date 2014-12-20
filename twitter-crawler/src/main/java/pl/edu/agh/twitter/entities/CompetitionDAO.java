package pl.edu.agh.twitter.entities;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * DAO for entity
 */
public class CompetitionDAO {
    @Inject
    private EntityManager em;

    public Competition createOrGet(String name) {
        Competition competition = get(name);
        if (competition == null) {
            final EntityTransaction transaction = em.getTransaction();
            competition = new Competition(name);
            em.persist(competition);
            transaction.commit();
        }
        return competition;
    }

    private Competition get(String name) {
        final EntityTransaction transaction = em.getTransaction();
        final String query = "FROM Competition WHERE lower(name) LIKE :name";
        List<Competition> competitionList = em.createQuery(query, Competition.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getResultList();
        transaction.commit();
        return competitionList.isEmpty() ? null : competitionList.get(0);
    }
}
