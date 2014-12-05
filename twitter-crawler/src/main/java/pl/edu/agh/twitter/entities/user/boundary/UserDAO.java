package pl.edu.agh.twitter.entities.user.boundary;

import pl.edu.agh.twitter.entities.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.entities.user.entity.User;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;

public class UserDAO {
    @Inject
    private EntityManager em;

    public User createOrGetUser(twitter4j.User user) throws TwitterException {
        User userEntity = getUser(user);
        if (userEntity != null)
            return userEntity;
        return persistUser(user);
    }

    private User persistUser(twitter4j.User user) throws TwitterException {
        final EntityTransaction transaction = em.getTransaction();
        final User userEntity = new User(user);
        em.persist(userEntity);
        transaction.commit();
        return userEntity;
    }

    private User getUser(twitter4j.User user) {
        return em.find(User.class, user.getId());
    }

    public List<User> findTopUsers(MatchEvent matchEvent, int limit) {
        Map<Long, Long> userCountMap = getTopUserMap(matchEvent, limit);
        final String usersQuery = "FROM User u WHERE u.id IN :ids";
        List<User> userEntities = em.createQuery(usersQuery, User.class)
                .setParameter("ids", userCountMap.keySet())
                .getResultList();
        for(User user : userEntities) {
            user.setTweetsNumber(userCountMap.get(user.getId()));
        }
        Collections.sort(userEntities, Collections.reverseOrder(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getTweetsNumber().compareTo(o2.getTweetsNumber());
            }
        }));
        return userEntities;
    }

    private Map<Long, Long> getTopUserMap(MatchEvent matchEvent, int limit) {
        final String idsQuery = " SELECT user_id, count(*)" +
                " FROM mgr.tweets " +
                " WHERE match_event = :matchId " +
                " GROUP BY user_id " +
                " ORDER BY count(*) DESC " +
                " LIMIT " + limit;
        @SuppressWarnings("unchecked")
        List<Object[]> items = em.createNativeQuery(idsQuery)
                .setParameter("matchId", matchEvent.getId())
                .getResultList();
        Map<Long, Long> userCountMap = new HashMap<>();
        for(Object[] row :  items) {
            Long userId = ((Number) row[0]).longValue();
            Long userTweetsNumber = ((Number) row[1]).longValue();
            userCountMap.put(userId, userTweetsNumber);
        }
        return userCountMap;
    }

    public User findByScreenName(String screenName) {
        final List<User> users = em.createNamedQuery(User.FIND_BY_SCREEN_NAME, User.class)
                .setParameter("screenName", screenName)
                .getResultList();
        if(users.isEmpty())
            return null;
        return users.get(0);
    }
}
