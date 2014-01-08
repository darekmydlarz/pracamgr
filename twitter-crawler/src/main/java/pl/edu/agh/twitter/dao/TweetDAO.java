package pl.edu.agh.twitter.dao;


import ch.lambdaj.Lambda;
import pl.edu.agh.twitter.model.MatchEvent;
import pl.edu.agh.twitter.model.Tweet;
import pl.edu.agh.twitter.model.UserEntity;
import pl.edu.agh.twitter.pojo.SourceTargetWeight;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class TweetDAO {
    @Inject
    EntityManager em;

    @Deprecated
    public List<Tweet> getTweets() {
        final String query = "FROM Tweet";
        return em.createQuery(query, Tweet.class).setMaxResults(100).getResultList();
    }

    public long getTweetsNumber(UserEntity userEntity, MatchEvent matchEvent) {
        final String query = "SELECT COUNT(t) FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match";
        return ((Number) em.createQuery(query)
                .setParameter("user", userEntity)
                .setParameter("match", matchEvent)
                .getSingleResult()
        ).longValue();
    }

    public long getGeotaggedNumber(UserEntity userEntity, MatchEvent matchEvent) {
        final String query = "SELECT COUNT(t) FROM Tweet t WHERE t.user = :user AND t.matchEvent = :match AND " +
                "t.coordinates.latitude IS NOT NULL ";
        return ((Number) em.createQuery(query)
                .setParameter("user", userEntity)
                .setParameter("match", matchEvent)
                .getSingleResult()
                ).longValue();
    }

    @SuppressWarnings("unchecked")
    public List<SourceTargetWeight> getUsersRepliesRelations(Collection<UserEntity> users) {
        List<Long> usersIds = Lambda.extract(users, on(UserEntity.class).getId());
        final String query = "SELECT NEW pl.edu.agh.twitter.pojo.SourceTargetWeight(t.user.id, t.inReplyToUserId, COUNT(t)) " +
                " FROM Tweet t " +
                " WHERE t.user.id IN :ids AND t.inReplyToUserId IN :ids " +
                " GROUP BY t.user.id, t.inReplyToUserId ";

        return em.createQuery(query)
                .setParameter("ids", usersIds)
                .getResultList();
    }
}
