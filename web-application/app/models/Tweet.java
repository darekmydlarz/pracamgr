package models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "mgr", name = "tweets")
public class Tweet implements Serializable {
    @Id
    public Long id;

    @Column(length = 140)
    public String text;

    public Date createdAt;

    public String source;

    public Long inReplyToStatusId;

    public Long inReplyToUserId;

    public int retweetCount;

    public int favouriteCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "match_event")
    public Match matchEvent;

    @Embedded
    public Coordinates coordinates;

    public static List<Tweet> some() {
        return JPA.em().createQuery("FROM Tweet", Tweet.class)
                .setMaxResults(100)
                .getResultList();
    }

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
                .setMaxResults(100)
                .getResultList();
    }

    public static Tweet find(Long id) {
        return JPA.em().createQuery("FROM Tweet WHERE id = :id", Tweet.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
