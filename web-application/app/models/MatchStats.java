package models;

import play.db.jpa.JPA;

import javax.persistence.*;

@Entity
@Table(schema = "mgr", name = "match_stats")
public class MatchStats {
    @Id
    public long id;
    @ManyToOne
    @JoinColumn(name = "match_id")
    public Match match;
    public boolean includeRT;
    public long tweets;
    public long users;
    public long positives;
    public long neutrals;
    public long negatives;
    public long geolocated;
    public long replies;
    public long retweets;

    public static MatchStats findByMatchId(long matchId, boolean includeRT) {
        String query = "FROM MatchStats WHERE match.id = :matchId AND includeRT = :include";
        return JPA.em().createQuery(query, MatchStats.class)
                .setParameter("matchId", matchId)
                .setParameter("include", includeRT)
                .getSingleResult();
    }
}