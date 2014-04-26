package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "mgr", name = "paroubek_tweets")
public class ParoubekTweet implements Serializable {
    @Id @JsonIgnore
    @Column(name = "tweet_id")
    public long tweetId;

    public Double valence;

    @Transient
    public Sentiment sentiment;

    @PostLoad
    private void postLoad() {
        if(Double.isNaN(valence))
            valence = null;
        sentiment = Sentiment.getByValence(valence);
    }
}
