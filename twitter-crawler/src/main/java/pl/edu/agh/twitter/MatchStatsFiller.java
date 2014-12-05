package pl.edu.agh.twitter;

import pl.edu.agh.twitter.entities.matchevent.boundary.MatchEventDAO;
import pl.edu.agh.twitter.entities.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.entities.matchstats.MatchStats;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Utility class used to fill match_stats table.
 * In table there was collected data, e.g. numbers of positive tweets, numbers of replies, number of geolocated tweets.
 */
public class MatchStatsFiller implements Startable {
    @Inject
    EntityManager em;

    @Inject
    MatchEventDAO matchEventDAO;

    void persist(MatchStats matchStats) {
        final EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
        em.persist(matchStats);
        transaction.commit();
        System.out.println("PERSISTED: " + matchStats);
    }

    @Override
    public void start() {
        final List<MatchEvent> matchEvents = matchEventDAO.fetchAll();
        for(MatchEvent me : matchEvents) {
            persist(executeIncludingRT(me.getId().intValue(), true));
            persist(executeIncludingRT(me.getId().intValue(), false));
        }
    }

    private MatchStats executeIncludingRT(int matchId, boolean includeRT) {
        MatchStats ms = new MatchStats();
        ms.setTweets(tweets(matchId, includeRT));
        ms.setUsers(users(matchId, includeRT));
        ms.setPositives(positives(matchId, includeRT));
        ms.setNegatives(negatives(matchId, includeRT));
        ms.setNeutrals(neutrals(matchId, includeRT));
        ms.setGeolocated(geolocated(matchId, includeRT));
        ms.setReplies(replies(matchId, includeRT));
        ms.setIncludeRT(includeRT);
        ms.setRetweets(retweets(matchId));
        ms.setMatchId(matchId);
        return ms;
    }

    int tweets(long matchId, boolean includeRT) {
        String query = countAll() + fromClause() + whereClause(includeRT);
        return executeForMatch(query, matchId);
    }

    int users(long matchId, boolean includeRT) {
        String query = "SELECT count(DISTINCT user_id) " + fromClause() + whereClause(includeRT);
        return executeForMatch(query, matchId);
    }

    int positives(long matchId, boolean includeRT) {
        String query = countAll() +
                fromClause() + " JOIN mgr.paroubek_tweets pt ON t.id = pt.tweet_id " +
                whereClause(includeRT) +
                " AND  pt.valence != 'NaN' AND pt.valence >= 0.4786984978198536";
        return executeForMatch(query, matchId);
    }

    int negatives(long matchId, boolean includeRT) {
        String query = countAll() +
                fromClause() + " JOIN mgr.paroubek_tweets pt ON t.id = pt.tweet_id " +
                whereClause(includeRT) +
                " AND  pt.valence != 'NaN' AND pt.valence < 0.4786984978198536";
        return executeForMatch(query, matchId);
    }

    int neutrals(long matchId, boolean includeRT) {
        String query = countAll() +
                fromClause() + " JOIN mgr.paroubek_tweets pt ON t.id = pt.tweet_id " +
                whereClause(includeRT) +
                " AND pt.valence = 'NaN'";
        return executeForMatch(query, matchId);
    }

    int geolocated(long matchId, boolean includeRT) {
        String query = countAll() + fromClause() + whereClause(includeRT) +
                " AND latitude IS NOT NULL";
        return executeForMatch(query, matchId);
    }

    int replies(long matchId, boolean includeRT) {
        String query = countAll() + fromClause() + whereClause(includeRT) +
                " AND in_reply_to_user_id != -1";
        return executeForMatch(query, matchId);
    }

    int retweets(long matchId) {
        String query = countAll() + fromClause() + whereClause(true) +
                " AND text LIKE 'RT @%' ";
        return executeForMatch(query, matchId);
    }

    private int executeForMatch(String query, long matchId) {
        System.out.println(query);

        return ((Number) em.createNativeQuery(query)
                .setParameter("matchId", matchId)
                .getSingleResult()).intValue();
    }

    String countAll() {
        return " SELECT count(*) ";
    }

    String fromClause() {
        return " FROM mgr.tweets t";
    }

    String whereClause(boolean includeRT) {
        StringBuilder sb = new StringBuilder(" WHERE ");
        if(!includeRT)
            sb.append(" t.text NOT LIKE 'RT @%' AND ");
        sb.append(" t.match_event = :matchId ");
        return sb.toString();
    }
}
