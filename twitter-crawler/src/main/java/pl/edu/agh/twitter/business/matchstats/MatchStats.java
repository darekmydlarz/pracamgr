package pl.edu.agh.twitter.business.matchstats;

import javax.persistence.*;

@Entity
@Table(schema = "mgr")
public class MatchStats {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long matchId;
    private boolean includeRT;
    private long tweets;
    private long users;
    private long positives;
    private long neutrals;
    private long negatives;
    private long geolocated;
    private long replies;
    private long retweets;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public boolean isIncludeRT() {
        return includeRT;
    }

    public void setIncludeRT(boolean includeRT) {
        this.includeRT = includeRT;
    }

    public long getTweets() {
        return tweets;
    }

    public void setTweets(long tweets) {
        this.tweets = tweets;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public long getPositives() {
        return positives;
    }

    public void setPositives(long positives) {
        this.positives = positives;
    }

    public long getNeutrals() {
        return neutrals;
    }

    public void setNeutrals(long neutrals) {
        this.neutrals = neutrals;
    }

    public long getNegatives() {
        return negatives;
    }

    public void setNegatives(long negatives) {
        this.negatives = negatives;
    }

    public long getGeolocated() {
        return geolocated;
    }

    public void setGeolocated(long geolocated) {
        this.geolocated = geolocated;
    }

    public long getReplies() {
        return replies;
    }

    public void setReplies(long replies) {
        this.replies = replies;
    }

    public long getRetweets() {
        return retweets;
    }

    public void setRetweets(long retweets) {
        this.retweets = retweets;
    }

    @Override
    public String toString() {
        return "MatchStats{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", includeRT=" + includeRT +
                ", tweets=" + tweets +
                ", users=" + users +
                ", positives=" + positives +
                ", neutrals=" + neutrals +
                ", negatives=" + negatives +
                ", geolocated=" + geolocated +
                ", replies=" + replies +
                ", retweets=" + retweets +
                '}';
    }
}
