package pl.edu.agh.twitter.dao;

import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.UserEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.*;

public class UserDAO {
    @Inject
    private EntityManager em;

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
