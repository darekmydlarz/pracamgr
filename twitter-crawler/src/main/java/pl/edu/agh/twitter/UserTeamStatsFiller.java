package pl.edu.agh.twitter;

import ch.lambdaj.Lambda;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.matchevent.MatchEventDAO;
import pl.edu.agh.twitter.entities.matchevent.MatchEvent;
import pl.edu.agh.twitter.entities.team.TeamDAO;
import pl.edu.agh.twitter.entities.team.Team;
import pl.edu.agh.twitter.entities.userteamstats.UserTeamStats;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

/**
 * It counts how many positive and negative tweets a particular user had posted about each team
 */
public class UserTeamStatsFiller implements Startable {

    Logger logger = Logger.getLogger(getClass());

    @Inject
    MatchEventDAO matchEventDAO;

    @Inject
    TeamDAO teamDAO;

    @Inject
    EntityManager em;

    List<UserTeamStats> findForTeam(Team team) {
        String q = "FROM UserTeamStats WHERE teamId = :teamId";
        return em.createQuery(q, UserTeamStats.class)
                .setParameter("teamId", team.getId())
                .getResultList();
    }

    void mergeAll(List<UserTeamStats> utsList) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        for(UserTeamStats uts : utsList) {
            em.merge(uts);
            System.out.printf("MERGE %d of %d\n", utsList.indexOf(uts), utsList.size());
        }
        transaction.commit();
    }

    void countPositivesTo(UserTeamStats uts, String matchIds) {
        String query = " SELECT count(*) " +
                " FROM mgr.paroubek_tweets_positives " +
                " WHERE user_id = ?1 " +
                "     and match_event IN ("+matchIds+") ";
        BigInteger counted = (BigInteger) em.createNativeQuery(query)
                .setParameter(1, uts.getUserId())
                .getSingleResult();
        uts.setPositives(counted.intValue());
    }

    void countNegativesTo(UserTeamStats uts, String matchIds) {
        String query = " SELECT count(*) " +
                " FROM mgr.paroubek_tweets_negatives " +
                " WHERE user_id = ?1 " +
                "     and match_event IN ("+matchIds+") ";
        BigInteger counted = (BigInteger) em.createNativeQuery(query)
                .setParameter(1, uts.getUserId())
                .getSingleResult();
        uts.setNegatives(counted.intValue());
    }

    @Override
    public void start() {
        fillSentimentValues();
    }

    private void fillSentimentValues() {
        final String[] teams = {"Arsenal", "Manchester United", "Manchester City", "Chelsea"};
        for(String name : teams) {
            final Team team = teamDAO.get(name);
            System.out.println(team);
            final List<UserTeamStats> utsList = findForTeam(team);
            List<MatchEvent> matchEvents = matchEventDAO.fetchAll(team);
            String matchIds = getMatchIds(matchEvents);
            int count = 0;
            int size = utsList.size();
            for(UserTeamStats uts : utsList) {
                countPositivesTo(uts, matchIds);
                countNegativesTo(uts, matchIds);
                if(count % 10 == 0)
                    logger.info("DONE "+ count + " of "+ size +" == " + uts);
                ++count;
            }
            mergeAll(utsList);
        }
    }

    private void countMatchesForUser() {
        final String[] teams = {"Arsenal", "Manchester United", "Manchester City", "Chelsea"};
        for(String name : teams) {
            final Team team = teamDAO.get(name);
            final List<MatchEvent> matchEvents = matchEventDAO.fetchAll(team);
            findAndPersistStats(team.getId(), matchEvents);
        }
    }

    private void findAndPersistStats(Long teamId, List<MatchEvent> matchEvents) {
        String matchEventsIds = getMatchIds(matchEvents);
        final String query = " SELECT user_id, count(distinct match_event) " +
                " FROM mgr.tweets " +
                " where match_event IN  (" + matchEventsIds + ")" +
                " GROUP BY user_id " +
                " ORDER BY count(distinct match_event) DESC  ";
        System.out.println(query);
        Iterator<Object[]> it = em.createNativeQuery(query).getResultList().iterator();
        List<UserTeamStats> utsList = Lists.newArrayList();
        while(it.hasNext()) {
            Object[] row = it.next();
            long userId = ((Number) row[0]).longValue();
            long matches = ((Number) row[1]).longValue();
            UserTeamStats uts = new UserTeamStats(userId, teamId, matches);
            utsList.add(uts);
        }
        persistAll(utsList);
    }

    private void persistAll(List<UserTeamStats> utsList) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();
        for(UserTeamStats uts : utsList) {
            em.persist(uts);
            System.out.printf("PERSIST %d of %d\n", utsList.indexOf(uts), utsList.size());
        }
        transaction.commit();
    }

    private String getMatchIds(List<MatchEvent> matchEvents) {
        List<String> ids = Lists.newArrayList();
        for(MatchEvent me : matchEvents) {
            ids.add(me.getId().toString());
        }
        System.out.println(ids);
        return Lambda.join(ids);
    }
}
