package pl.edu.agh.twitter.business.user.boundary;

import pl.edu.agh.twitter.business.matchevent.entity.MatchEvent;
import pl.edu.agh.twitter.business.user.entity.UserEntity;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;

public class UserDAO {
    @Inject
    private EntityManager em;

    public UserEntity createOrGetUser(User user) throws TwitterException {
        UserEntity userEntity = getUser(user);
        if (userEntity != null)
            return userEntity;
        return persistUser(user);
    }

    private UserEntity persistUser(User user) throws TwitterException {
        final EntityTransaction transaction = em.getTransaction();
        final UserEntity userEntity = new UserEntity(user);
        em.persist(userEntity);
        transaction.commit();
        return userEntity;
    }

    private UserEntity getUser(User user) {
        return em.find(UserEntity.class, user.getId());
    }

    public List<UserEntity> findTopUsers(MatchEvent matchEvent, int limit) {
        Map<Long, Long> userCountMap = getTopUserMap(matchEvent, limit);
        final String usersQuery = "FROM UserEntity u WHERE u.id IN :ids";
        List<UserEntity> userEntities = em.createQuery(usersQuery, UserEntity.class)
                .setParameter("ids", userCountMap.keySet())
                .getResultList();
        for(UserEntity user : userEntities) {
            user.setTweetsNumber(userCountMap.get(user.getId()));
        }
        Collections.sort(userEntities, Collections.reverseOrder(new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity o1, UserEntity o2) {
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
}
