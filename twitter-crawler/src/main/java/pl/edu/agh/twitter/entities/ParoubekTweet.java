package pl.edu.agh.twitter.entities;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * Mapped DB table
 */
@Entity
@Table(schema = "mgr", name = "paroubek_tweets")
public class ParoubekTweet {
    public static final double VALENCE_AVG = 0.4786984978198536;
    @Id
    private Long tweetId;

    @OneToOne
    @JoinColumn(name = "tweetId", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Tweet tweet;

    private String text;
    private Double valence;

    public ParoubekTweet() {
    }

    public ParoubekTweet(Long tweetId, String text, Double valence) {
        this.tweetId = tweetId;
        this.text = text;
        this.valence = valence;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getValence() {
        return valence;
    }

    public void setValence(Double valence) {
        this.valence = valence;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public boolean isPositive() {
        return valence > VALENCE_AVG;
    }

    @Override
    public String toString() {
        return "ParoubekTweet{" +
                "tweetId=" + tweetId +
                ", text='" + text + '\'' +
                ", valence=" + valence +
                '}';
    }
}
