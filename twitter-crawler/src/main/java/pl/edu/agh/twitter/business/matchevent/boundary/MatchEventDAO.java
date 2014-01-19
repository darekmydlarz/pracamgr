package pl.edu.agh.twitter.business.matchevent.boundary;

import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.business.team.entity.Team;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

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
        em.merge(matchEvent);
        transaction.commit();
    }
}
