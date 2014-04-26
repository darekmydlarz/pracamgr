package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.Lists;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
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

    @SuppressWarnings("unchecked")
    public static List<SentimentInTime> getSentimentInTime(Long matchId) {
        final String query = " 	SELECT to_char(created_at, 'yyyy-MM-dd HH24:MI') as time,  " +
                " 	  count(nullif(valence > 0.4786984978198536, false)) as positives, " +
                " 	  count(nullif(valence <= 0.4786984978198536, false)) as negatives " +
                " 	FROM mgr.paroubek_tweets a JOIN mgr.tweets b ON a.tweet_id = b.id AND b.match_event = :matchId " +
                " 	WHERE valence != 'NaN' " +
                " 	GROUP BY time " +
                " 	ORDER BY time ";

        final List<Object[]> rows = (List<Object[]>) JPA.em().createNativeQuery(query)
                .setParameter("matchId", matchId)
                .getResultList();

        final List<SentimentInTime> resultList = Lists.newArrayList();
        for (Object[] row : rows) {
            final String dateTime = (String) row[0];
            final BigInteger positives = (BigInteger) row[1];
            final BigInteger negatives = (BigInteger) row[2];
            SentimentInTime sit = new SentimentInTime(dateTime, positives.intValue(), negatives.intValue());
            resultList.add(sit);
        }
        return resultList;
    }

}
