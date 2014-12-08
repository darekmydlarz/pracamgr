package pl.edu.agh.twitter.entities.matchevent;

import pl.edu.agh.twitter.entities.team.Team;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * DAO for entity
 */
public class MatchEventDAO {

    @Inject
    EntityManager em;

    public List<MatchEvent> fetchAll() {
        final String query = "FROM MatchEvent";
        return em.createQuery(query, MatchEvent.class).getResultList();
    }

    public List<MatchEvent> fetchAll(Team team) {
        final String query = "FROM MatchEvent WHERE homeTeam = :team OR awayTeam = :team ORDER BY startDate ASC";
        return em.createQuery(query, MatchEvent.class)
                .setParameter("team", team)
                .getResultList();
    }

    public void merge(MatchEvent matchEvent) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        em.merge(matchEvent);
        transaction.commit();
    }
}
