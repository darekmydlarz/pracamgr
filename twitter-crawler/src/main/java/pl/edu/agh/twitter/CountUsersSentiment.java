package pl.edu.agh.twitter;

import ch.lambdaj.Lambda;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.twitter.entities.UserTeamStats;
import pl.edu.agh.twitter.sentiment.Sentiment;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;

/**
 * Utility class to count each user sentiment in different teams matches.
 * It counts how many particular users were tweeting in positive and negative way about each team
 * Then it saves it to UserTeamStats entity table
 */
public class CountUsersSentiment implements Startable {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    EntityManager em;

    /**
     * teamId -> <matchId, matchId, ...>
     * teams matches ids connected with teams ids
     */
    Map<Long, Integer[]> teamMatches = ImmutableMap.<Long, Integer[]>builder()
            .put(158l, new Integer[]{266, 258, 255, 715, 714, 244, 611, 547, 404, 176})     // arsenal
            .put(408l, new Integer[]{267, 2680, 255, 720, 709, 243, 674, 589, 445})     // chelsea
            .put(447l, new Integer[]{265, 256, 249, 725, 699, 197, 612, 567, 486})     // united
            .put(508l, new Integer[]{260, 259, 254, 715, 704, 198, 694, 609, 526})     // city
            .build();

    List<UserTeamStats> utsList(int offset, int limit) {
        return em.createQuery("FROM UserTeamStats", UserTeamStats.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public void start() {
        int size = 2_620_347,
            limit = 1_000;
        for(int offset = 0; offset < size; offset += limit) {
            logger.info("Offset {} of {}", offset, size);
            final List<UserTeamStats> utsList = utsList(offset, limit);
            logger.info("Found");
            for(UserTeamStats uts : utsList){
                uts.setPositives(countSentiment(uts, Sentiment.POS));
                uts.setNegatives(countSentiment(uts, Sentiment.NEG));
            }
            logger.info("Merging...");
            mergeAll(utsList);
        }
    }

    private void mergeAll(List<UserTeamStats> utsList) {
        final EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive())
            transaction.begin();

        for (UserTeamStats uts : utsList) {
            em.merge(uts);
        }

        transaction.commit();
    }

    int countSentiment(UserTeamStats uts, Sentiment sentiment) {
        String matchIds = join(teamMatches.get(uts.getTeamId()));
        String query = " SELECT count(*) " +
                " FROM mgr.paroubek_tweets_sentiment " +
                "   where user_id = :userId " +
                "   and sentiment = :sentiment " +
                "   and match_event IN ("+matchIds+")";

        final Number count = (Number) em.createNativeQuery(query)
                .setParameter("userId", uts.getUserId())
                .setParameter("sentiment", sentiment.name())
                .getSingleResult();
        return count.intValue();
    }

    private String join(Integer[] matchIds) {
        return Lambda.join(matchIds);
    }
}
