package pl.edu.agh.twitter.dao;

import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.Team;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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
}
