package models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(schema = "mgr", name = "users_match_sentiment")
public class UserMatchSentiment implements Serializable {
    @Id
    public Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "match_event_id")
    public Match matchEvent;

    public Long positives;
    public Long negatives;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false),
            @JoinColumn(name = "match_event_id", referencedColumnName = "matchEventId", insertable = false, updatable = false)
    })
    public MatchEventsGephi meg;

    public static List<UserMatchSentiment> findTop(Long matchId, String column) {
        final String query = "FROM UserMatchSentiment ums " +
                " WHERE ums.matchEvent.id = :matchId " +
                "   AND positives + negatives > 0" +
                " ORDER BY meg." + column + " DESC";
        return JPA.em().createQuery(query, UserMatchSentiment.class)
                .setParameter("matchId", matchId)
                .setMaxResults(10)
                .getResultList();
    }
}
