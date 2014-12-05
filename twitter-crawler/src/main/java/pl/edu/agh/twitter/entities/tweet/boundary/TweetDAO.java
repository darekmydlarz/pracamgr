package pl.edu.agh.twitter.entities.tweet.boundary;


import ch.lambdaj.Lambda;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pl.edu.agh.twitter.entities.Relationship;
import pl.edu.agh.twitter.entities.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.entities.tweet.entity.Tweet;
import pl.edu.agh.twitter.entities.user.boundary.UserDAO;
import pl.edu.agh.twitter.entities.user.entity.User;
import pl.edu.agh.twitter.socialnetwork.SourceTargetWeight;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class TweetDAO {
    private Logger logger = Logger.getLogger(getClass());

    @Inject
    EntityManager em;

    @Inject
    private UserDAO userDAO;

    public List<Tweet> findAll(int offset, int limit) {
        final String query = "FROM Tweet t WHERE t.matchEvent IS NOT NULL";
        return em.createQuery(query, Tweet.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long countGeotagged(MatchEvent me) {
        final EntityTransaction transaction = em.getTransaction();
        final String query = "SELECT count(*) FROM Tweet t WHERE matchEvent = :me AND coordinates.latitude IS NOT NULL";
        return ((Number) em.createQuery(query)
                .setParameter("me", me)
                .getSingleResult()).longValue();
    }

    public void parseAndPersist(MatchEvent matchEvent, Status status) {
        try {
            final User user = userDAO.createOrGetUser(status.getUser());
            Tweet tweet = new Tweet(status, user, matchEvent);
            persist(tweet);
            logger.info("Tweet " + tweet.getId() + " persisted");
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            logger.error("No match event found for: " + status);
        }
    }

    private void persist(Tweet tweet) {
        final EntityTransaction transaction = em.getTransaction();
        em.persist(tweet);
        transaction.commit();
    }

    @SuppressWarnings("unchecked")
    public List<Tweet> getTweets(User user, MatchEvent matchEvent) {
        final String query = "FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match";
        return em.createQuery(query)
                .setParameter("user", user)
                .setParameter("match", matchEvent)
                .getResultList();
    }

    public List<Tweet> getTweetsLimitOffset(int limit, int offset) {
        final String query = "FROM Tweet t ORDER BY t.id";
        return em.createQuery(query, Tweet.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long getTweetsNumber(User user, MatchEvent matchEvent) {
        final String query = "SELECT COUNT(t) FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match";
        return ((Number) em.createQuery(query)
                .setParameter("user", user)
                .setParameter("match", matchEvent)
                .getSingleResult()
        ).longValue();
    }

    public long getGeotaggedNumber(User user, MatchEvent matchEvent) {
        final String query = "SELECT COUNT(t) FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match AND " +
                "t.coordinates.latitude IS NOT NULL ";
        return ((Number) em.createQuery(query)
                .setParameter("user", user)
                .setParameter("match", matchEvent)
                .getSingleResult()
        ).longValue();
    }

    public List<Tweet> getGeotagged() {
        final String query = "FROM Tweet t WHERE t.coordinates.latitude IS NOT NULL AND " +
                " t.coordinates.latitude != 0 ";
                // " AND t.id NOT IN (SELECT g.tweetId FROM Geodata g) ";
        return em.createQuery(query, Tweet.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<SourceTargetWeight> getUsersRepliesRelations(Collection<User> users) {
        List<Long> usersIds = Lambda.extract(users, on(User.class).getId());
        final String query = "SELECT NEW pl.edu.agh.twitter.socialnetwork.SourceTargetWeight(t.user.id, t.inReplyToUserId, COUNT(t)) " +
                " FROM Tweet t " +
                " WHERE t.user.id IN :ids AND t.inReplyToUserId IN :ids " +
                " GROUP BY t.user.id, t.inReplyToUserId ";

        return em.createQuery(query)
                .setParameter("ids", usersIds)
                .getResultList();
    }

    public List<Tweet> getWithoutRetweets(int offset, int length) {
        final String query = "FROM Tweet WHERE text NOT LIKE 'RT%'";
        return em.createQuery(query, Tweet.class)
                .setFirstResult(offset)
                .setMaxResults(length)
                .getResultList();
    }

    public List<Tweet> getWithEmoticonsAndPattern(int offset, int length, String pattern) {
        final String query = " FROM Tweet t WHERE (" +
                " t.text LIKE '%:)%' OR " +
                " t.text LIKE '%:D%' OR " +
                " t.text LIKE '%:(%' OR " +
                " t.text LIKE '%;)%' OR " +
                " t.text LIKE '%:-)%' OR " +
                " t.text LIKE '%:P%' OR " +
                " t.text LIKE '%=)%' OR " +
                " t.text LIKE '%(:%' OR " +
                " t.text LIKE '%;-)%' OR " +
                " (t.text LIKE '%:/%' AND t.text NOT LIKE '%://%') OR " +
                " t.text LIKE '%XD%' OR " +
                " t.text LIKE '%=D%' OR " +
                " t.text LIKE '%:o%' OR " +
                " t.text LIKE '%=]%' OR " +
                " t.text LIKE '%;D%' OR " +
                " t.text LIKE '%:]%' OR " +
                " t.text LIKE '%:-(%' OR " +
                " t.text LIKE '%=/%' OR " +
                " t.text LIKE '%=(%') " +
                " AND t.text LIKE :pattern" +
                " AND t.text NOT LIKE 'RT%' ";

        return em.createQuery(query, Tweet.class)
                .setParameter("pattern", pattern)
                .setFirstResult(offset)
                .setMaxResults(length)
                .getResultList();
    }

    public List<Tweet> getWithEmoticons(int offset, int length) {
        final String query = " FROM Tweet t WHERE (" +
                " t.text LIKE '%:)%' OR " +
                " t.text LIKE '%:D%' OR " +
                " t.text LIKE '%:(%' OR " +
                " t.text LIKE '%;)%' OR " +
                " t.text LIKE '%:-)%' OR " +
                " t.text LIKE '%:P%' OR " +
                " t.text LIKE '%=)%' OR " +
                " t.text LIKE '%(:%' OR " +
                " t.text LIKE '%;-)%' OR " +
                " (t.text LIKE '%:/%' AND t.text NOT LIKE '%://%') OR " +
                " t.text LIKE '%XD%' OR " +
                " t.text LIKE '%=D%' OR " +
                " t.text LIKE '%:o%' OR " +
                " t.text LIKE '%=]%' OR " +
                " t.text LIKE '%;D%' OR " +
                " t.text LIKE '%:]%' OR " +
                " t.text LIKE '%:-(%' OR " +
                " t.text LIKE '%=/%' OR " +
                " t.text LIKE '%=(%') " +
                " AND t.text NOT LIKE 'RT%' ";

        return em.createQuery(query, Tweet.class)
                .setFirstResult(offset)
                .setMaxResults(length)
                .getResultList();
    }

    public List<Tweet> getAllWithEmoticons() {
        return getWithEmoticons(0, Integer.MAX_VALUE);
    }



    public List<Relationship> getRelationships(MatchEvent matchEvent) {
        final String query = " SELECT " +
                "   u1.screen_name as author, " +
                "   u2.screen_name as receiver, " +
                "   count(*) as weight " +
                " FROM " +
                "   mgr.tweets t JOIN " +
                "   mgr.users u1 ON t.user_id = u1.id JOIN " +
                "   mgr.users u2 ON t.in_reply_to_user_id = u2.id " +
                " WHERE " +
                "   in_reply_to_user_id != -1 AND " +
                "   match_event = :matchId " +
                " GROUP BY " +
                "   u1.screen_name, " +
                "   u2.screen_name " +
                " HAVING count(*) >= 3 " +
                " ORDER BY count(*) DESC   ";

        @SuppressWarnings("unchecked")
        Iterator<Object[]> rowsIterator = em.createNativeQuery(query)
                .setParameter("matchId", matchEvent.getId())
                .getResultList().iterator();

        List<Relationship> relationshipList = Lists.newArrayList();
        while(rowsIterator.hasNext()) {
            Object[] row = rowsIterator.next();
            Relationship relationship = new Relationship((String) row[0], (String) row[1], ((Number) row[2]).intValue());
            relationshipList.add(relationship);
        }

        return relationshipList;
    }



    public List<Relationship> getRelationships(List<MatchEvent> matchEvents) {
        final String query = " SELECT " +
                "   u1.screen_name as author, " +
                "   u2.screen_name as receiver, " +
                "   count(*) as weight " +
                " FROM " +
                "   mgr.tweets t JOIN " +
                "   mgr.users u1 ON t.user_id = u1.id JOIN " +
                "   mgr.users u2 ON t.in_reply_to_user_id = u2.id " +
                " WHERE " +
                "   in_reply_to_user_id != -1 AND " +
                "   match_event IN  " + matchesIds(matchEvents.iterator()) +
                " GROUP BY " +
                "   u1.screen_name, " +
                "   u2.screen_name " +
                " HAVING count(*) >= 3 " +
                " ORDER BY count(*) DESC   ";

        System.out.println(query);

        @SuppressWarnings("unchecked")
        Iterator<Object[]> rowsIterator = em.createNativeQuery(query)
                .getResultList().iterator();

        List<Relationship> relationshipList = Lists.newArrayList();
        while(rowsIterator.hasNext()) {
            Object[] row = rowsIterator.next();
            Relationship relationship = new Relationship((String) row[0], (String) row[1], ((Number) row[2]).intValue());
            relationshipList.add(relationship);
        }

        return relationshipList;
    }

    private String matchesIds(Iterator<MatchEvent> it) {
        List<Long> ids = Lists.newArrayList();
        while(it.hasNext()) {
            ids.add(it.next().getId());
        }
        return "(" + StringUtils.join(ids, ",") + ")";
    }
}
