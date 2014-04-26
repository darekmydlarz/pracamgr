package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "mgr", name = "tweets")
public class Tweet implements Serializable {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    @Column(length = 140)
    public String text;

    @JsonSerialize(using = ToStringSerializer.class)
    public Date createdAt;

    @JsonIgnore
    public String source;

    @JsonIgnore
    public Long inReplyToStatusId;

    @JsonIgnore
    public Long inReplyToUserId;

    @JsonIgnore
    public int retweetCount;

    @JsonIgnore
    public int favouriteCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "match_event")
    public Match matchEvent;

    @Embedded
    public Coordinates coordinates;

    @OneToOne
    @JoinColumn(name = "id")
    public ParoubekTweet paroubekTweet;

    public static Long count(Match match) {
        final String query = "SELECT COUNT(t) FROM Tweet t WHERE matchEvent = :match";
        return (Long) JPA.em().createQuery(query)
                .setParameter("match", match)
                .getSingleResult();
    }

    public static List<Tweet> geotagged(Match match) {
        final String query = "FROM Tweet T WHERE matchEvent = :match AND coordinates.longitude IS NOT NULL";
        return JPA.em().createQuery(query, Tweet.class)
                .setParameter("match", match)
//                .setMaxResults(100)
                .getResultList();
    }

    public static Tweet find(Long id) {
        return JPA.em().createQuery("FROM Tweet WHERE id = :id", Tweet.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public static List<Tweet> topWithSentiment(Match match, Sentiment sentiment, int limit) {
        if(sentiment == Sentiment.POS)
            return topPositive(match, limit);
        return topNegative(match, limit);
    }

    private static List<Tweet> topNegative(Match match, int limit) {
        final String query = "FROM Tweet t " +
                "WHERE matchEvent = :match " +
                "  AND t.paroubekTweet.valence < :avgValence " +
                "ORDER BY t.paroubekTweet.valence ASC";
        return JPA.em().createQuery(query, Tweet.class)
                .setParameter("match", match)
                .setParameter("avgValence", Sentiment.AVERAGE_VALENCE)
                .setMaxResults(limit)
                .getResultList();
    }

    private static List<Tweet> topPositive(Match match, int limit) {
        final String query = "FROM Tweet t " +
                "WHERE matchEvent = :match " +
                "  AND t.paroubekTweet.valence >= :avgValence " +
                "  AND t.paroubekTweet.valence != 'NaN' " +
                "ORDER BY t.paroubekTweet.valence DESC";
        return JPA.em().createQuery(query, Tweet.class)
                .setParameter("match", match)
                .setParameter("avgValence", Sentiment.AVERAGE_VALENCE)
                .setMaxResults(limit)
                .getResultList();
    }
}
