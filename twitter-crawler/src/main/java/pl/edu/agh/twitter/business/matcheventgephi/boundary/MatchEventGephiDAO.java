package pl.edu.agh.twitter.business.matcheventgephi.boundary;

import org.apache.log4j.Logger;
import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.business.matcheventgephi.entity.MatchEventGephi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class MatchEventGephiDAO {
    @Inject
    private EntityManager em;
    private Logger logger = Logger.getLogger(getClass());

    public void persistAll(List<MatchEventGephi> items) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        final int size = items.size();
        logger.info(size + " items persisting");
        for(MatchEventGephi matchEventGephi : items)
            em.persist(matchEventGephi);
        transaction.commit();
        logger.info("[OK]");
    }

    public List<MatchEventGephi> findTop10(MatchEvent matchEvent, String columnName) {
        final String query = "FROM MatchEventGephi WHERE matchEventId = :meId ORDER BY " + columnName + " DESC";
        return em.createQuery(query, MatchEventGephi.class)
                .setParameter("meId", matchEvent.getId())
                .setMaxResults(10)
                .getResultList();
    }

    public List<MatchEventGephi> all() {
        final String query = "FROM MatchEventGephi";
        return em.createQuery(query, MatchEventGephi.class).getResultList();
    }

    public List<MatchEventGephi> allForMatch(Long matchEventId) {
        final String query = "FROM MatchEventGephi meg WHERE meg.matchEventId = :matchEventId";
        return em.createQuery(query, MatchEventGephi.class)
                .setParameter("matchEventId", matchEventId)
                .getResultList();
    }
}
