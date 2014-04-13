package pl.edu.agh.twitter.business.paroubektweet.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "mgr", name = "paroubek_tweets")
public class ParoubekTweet {
    @Id
    private Long tweetId;
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
}
